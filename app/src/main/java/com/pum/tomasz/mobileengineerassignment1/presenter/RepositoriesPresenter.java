package com.pum.tomasz.mobileengineerassignment1.presenter;

import com.pum.tomasz.mobileengineerassignment1.frontend.FetchSquareRepositoriesUsecase;
import com.pum.tomasz.mobileengineerassignment1.model.RepositoryItem;
import com.pum.tomasz.mobileengineerassignment1.view.RepositoriesView;


import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by tomasz on 13.10.2017.
 */

public class RepositoriesPresenter implements Presenter<RepositoriesView> {

    private Disposable getRepositoriesSubscription;
    private Disposable clickItemSubscription;
    private RepositoriesView repositoriesView;
    private FetchSquareRepositoriesUsecase fetchSquareRepositoriesUsecase;

    private List<RepositoryItem> repositoriesCollection;
    private RepositoryItem selectedItem;

    public RepositoriesPresenter(FetchSquareRepositoriesUsecase fetchSquareRepositoriesUsecase) {
        this.fetchSquareRepositoriesUsecase = fetchSquareRepositoriesUsecase;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onStart() {
        getRepositories();
    }

    @Override
    public void onStop() {
        if (getRepositoriesSubscription != null && !getRepositoriesSubscription.isDisposed()) {
            getRepositoriesSubscription.dispose();
        }

        if(clickItemSubscription != null && !clickItemSubscription.isDisposed()) {
            clickItemSubscription.dispose();
        }
    }

    @Override
    public void onPause() {

    }

    @Override
    public void attachView(RepositoriesView view) {
        this.repositoriesView = view;
    }

    public void onRefresh() {
        getRepositories();
    }


    private void getRepositories() {
        if (repositoriesCollection != null && repositoriesView != null) {
            repositoriesView.showRepositories(repositoriesCollection);
            return;
        } else {
            repositoriesView.showLoading();
        }
        getRepositoriesSubscription = fetchSquareRepositoriesUsecase.execute()
                .subscribeOn(Schedulers.io())
                .onErrorReturn(new Function<Throwable, List<RepositoryItem>>() {
                    @Override
                    public List<RepositoryItem> apply(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        repositoriesView.showError();
                        return null;
                    }

                })
                .subscribe(new Consumer<List<RepositoryItem>>() {
                    @Override
                    public void accept(List<RepositoryItem> repositoryItems) throws Exception {
                        if (repositoryItems != null) {
                            if (repositoryItems != null && repositoryItems.size() > 0) {
                                repositoriesCollection = repositoryItems;
                                repositoriesView.showRepositories(repositoryItems);
                            } else {
                                repositoriesView.showEmpty();
                            }
                        }
                    }
                });
    }

    public void filterRepositories(final String charSequence) {
        if(repositoriesCollection != null && repositoriesCollection.size()!=0) {
            final List<RepositoryItem> filteredRepositoriesList = new ArrayList<>();
            Observable.fromIterable(repositoriesCollection)
                    //.filter((repositoryItem) -> repositoryItem.getName().contains(charSequence))
//                    .filter(new Function<RepositoryItem, Boolean>() {
//                        @Override
//                        public Boolean apply(RepositoryItem repositoryItem) throws Exception {
//                            return repositoryItem.getName().toLowerCase().contains(charSequence.toLowerCase());
//                        }
//                    })
                    .filter(new Predicate<RepositoryItem>() {
                        @Override
                        public boolean test(RepositoryItem repositoryItem) throws Exception {
                            return repositoryItem.getName().toLowerCase().contains(charSequence.toLowerCase());
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<RepositoryItem>() {

                        @Override
                        public void onError(Throwable e) {
                            repositoriesView.showError();
                        }

                        @Override
                        public void onComplete() {
                            repositoriesView.showRepositories(filteredRepositoriesList);
                        }

                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(RepositoryItem repositoryItem) {
                            filteredRepositoriesList.add(repositoryItem);
                        }
                    });
        }

    }

    public void setClickItemSubscription(Disposable subscription) {
        clickItemSubscription = subscription;
    }

    public Disposable getClickItemSubscription() {
        return clickItemSubscription;
    }

    public void setSelectedItem(RepositoryItem selectedItem) {
        this.selectedItem = selectedItem;
        repositoriesView.showRepositoryDetail(selectedItem);
    }
}

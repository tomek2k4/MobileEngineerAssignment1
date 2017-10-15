package com.pum.tomasz.mobileengineerassignment1.presenter;

import com.pum.tomasz.mobileengineerassignment1.frontend.FetchSquareRepositoriesUsecase;
import com.pum.tomasz.mobileengineerassignment1.model.RepositoryItem;
import com.pum.tomasz.mobileengineerassignment1.view.RepositoriesView;

import java.util.List;

import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by tomasz on 13.10.2017.
 */

public class RepositoriesPresenter implements Presenter<RepositoriesView> {

    private Subscription getRepositoriesSubscription;
    private Subscription clickItemSubscription;
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
        if (getRepositoriesSubscription != null && !getRepositoriesSubscription.isUnsubscribed()) {
            getRepositoriesSubscription.unsubscribe();
        }

        if(clickItemSubscription != null && !clickItemSubscription.isUnsubscribed()) {
            clickItemSubscription.unsubscribe();
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
        repositoriesCollection = null;
        getRepositories();
    }


    private void getRepositories() {
        if (repositoriesCollection != null && repositoriesView != null) {
            repositoriesView.showRepositories(repositoriesCollection);
        } else {
            repositoriesView.showLoading();
        }
        getRepositoriesSubscription = fetchSquareRepositoriesUsecase.execute()
                .subscribeOn(Schedulers.io())
                .onErrorReturn(new Func1<Throwable, List<RepositoryItem>>() {
                    @Override
                    public List<RepositoryItem> call(Throwable throwable) {
                        throwable.printStackTrace();
                        repositoriesView.showError();
                        return null;
                    }
                })
                .subscribe(new Action1<List<RepositoryItem>>() {
                    @Override
                    public void call(List<RepositoryItem> repositoryItems) {
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


    public void setClickItemSubscription(Subscription subscription) {
        clickItemSubscription = subscription;
    }

    public Subscription getClickItemSubscription() {
        return clickItemSubscription;
    }

    public void setSelectedItem(RepositoryItem selectedItem) {
        this.selectedItem = selectedItem;
        repositoriesView.showRepositoryDetail(selectedItem);
    }
}

package com.pum.tomasz.mobileengineerassignment1.presenter;

import com.pum.tomasz.mobileengineerassignment1.model.RepositoryItem;
import com.pum.tomasz.mobileengineerassignment1.view.RepositoryView;

/**
 * Created by tomasz on 15.10.2017.
 */

public class RepositoryPresenter implements Presenter<RepositoryView> {

    private RepositoryView repositoryView;
    private RepositoryItem repositoryItem;


    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void attachView(RepositoryView view) {
        repositoryView = view;
    }

    public void setRepositoryItem(RepositoryItem repositoryItem) {
        this.repositoryItem = repositoryItem;
        repositoryView.showRepository(repositoryItem);
    }

    public RepositoryItem getRepositoryItem() {
        return repositoryItem;
    }
}

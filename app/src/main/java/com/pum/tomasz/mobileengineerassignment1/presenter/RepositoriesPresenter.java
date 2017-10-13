package com.pum.tomasz.mobileengineerassignment1.presenter;

import com.pum.tomasz.mobileengineerassignment1.frontend.FetchSquareRepositoriesUsecase;
import com.pum.tomasz.mobileengineerassignment1.view.RepositoriesView;

import rx.Subscription;

/**
 * Created by tomasz on 13.10.2017.
 */

public class RepositoriesPresenter implements Presenter<RepositoriesView> {

    private Subscription getRepositoriesSubscription;
    private RepositoriesView repositoriesView;
    private FetchSquareRepositoriesUsecase fetchSquareRepositoriesUsecase;

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
    public void attachView(RepositoriesView view) {

    }
}

package com.pum.tomasz.mobileengineerassignment1.injector.module;

import com.pum.tomasz.mobileengineerassignment1.frontend.DataProvider;
import com.pum.tomasz.mobileengineerassignment1.frontend.FetchSquareRepositoriesUsecase;
import com.pum.tomasz.mobileengineerassignment1.presenter.RepositoriesPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by tomasz on 20.10.2017.
 */

@Module
public class RepositoriesModule {

    @Provides
    public FetchSquareRepositoriesUsecase provideFetchSquareRepositoriesUsecase(DataProvider dataProvider) {
        return new FetchSquareRepositoriesUsecase(dataProvider);
    }

    @Provides
    public RepositoriesPresenter provideRepositoriesPresenter(FetchSquareRepositoriesUsecase usecase) {
        return new RepositoriesPresenter(usecase);
    }
}

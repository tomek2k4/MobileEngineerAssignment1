package com.pum.tomasz.mobileengineerassignment1.injector.module;

import com.pum.tomasz.mobileengineerassignment1.presenter.RepositoryPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by tomasz on 06.11.2017.
 */



@Module
public class RepositoryModule {

    @Provides
    public RepositoryPresenter provideRepositoryPresenter(){
        return new RepositoryPresenter();
    }

}

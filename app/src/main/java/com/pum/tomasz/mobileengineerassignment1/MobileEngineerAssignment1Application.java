package com.pum.tomasz.mobileengineerassignment1;

import android.app.Application;

import com.pum.tomasz.mobileengineerassignment1.injector.component.ApplicationComponent;

import com.pum.tomasz.mobileengineerassignment1.injector.component.DaggerApplicationComponent;
import com.pum.tomasz.mobileengineerassignment1.injector.module.ApplicationModule;

/**
 * Created by tomasz on 20.10.2017.
 */

public class MobileEngineerAssignment1Application extends Application {

    private ApplicationComponent applicationComponent;


    @Override
    public void onCreate() {
        super.onCreate();
        setupInjector();

    }

    private void setupInjector() {
        applicationComponent = DaggerApplicationComponent.create();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

}

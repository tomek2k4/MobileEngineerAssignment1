package com.pum.tomasz.mobileengineerassignment1.injector.module;

import android.app.Activity;
import android.content.Context;

import com.pum.tomasz.mobileengineerassignment1.injector.scope.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by tomasz on 23.10.2017.
 */

@Module
public class ActivityModule {
    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @PerActivity
    public Context context() {
        return activity;
    }
}

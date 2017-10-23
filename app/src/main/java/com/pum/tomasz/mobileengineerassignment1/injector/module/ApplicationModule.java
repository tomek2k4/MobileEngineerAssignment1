package com.pum.tomasz.mobileengineerassignment1.injector.module;

import android.app.Application;

import com.pum.tomasz.mobileengineerassignment1.MobileEngineerAssignment1Application;
import com.pum.tomasz.mobileengineerassignment1.injector.scope.PerApplication;

import dagger.Module;
import dagger.Provides;

/**
 * Created by tomasz on 21.10.2017.
 */

@Module
public class ApplicationModule {
    private final MobileEngineerAssignment1Application application;


    public ApplicationModule(MobileEngineerAssignment1Application application) {
        this.application = application;
    }

    @Provides
    @PerApplication
    public MobileEngineerAssignment1Application provideMvpApplication() {
        return application;
    }

    @Provides
    @PerApplication
    public Application provideApplication() {return application; }


}

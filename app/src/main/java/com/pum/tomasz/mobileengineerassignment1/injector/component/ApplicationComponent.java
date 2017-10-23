package com.pum.tomasz.mobileengineerassignment1.injector.component;

import android.app.Application;

import com.pum.tomasz.mobileengineerassignment1.MobileEngineerAssignment1Application;
import com.pum.tomasz.mobileengineerassignment1.frontend.DataProvider;
import com.pum.tomasz.mobileengineerassignment1.injector.module.ApplicationModule;
import com.pum.tomasz.mobileengineerassignment1.injector.module.NetworkModule;
import com.pum.tomasz.mobileengineerassignment1.injector.scope.PerApplication;

import dagger.Component;

/**
 * Created by tomasz on 20.10.2017.
 */

@PerApplication
@Component(modules = {ApplicationModule.class,NetworkModule.class})

public interface ApplicationComponent {

    Application application();
    MobileEngineerAssignment1Application mobileEngineerAssignment1Application();
    DataProvider dataProvider();
}

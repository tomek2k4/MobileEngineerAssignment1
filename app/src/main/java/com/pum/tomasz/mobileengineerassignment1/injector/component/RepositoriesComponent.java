package com.pum.tomasz.mobileengineerassignment1.injector.component;

import com.pum.tomasz.mobileengineerassignment1.activity.RepositoriesActivity;
import com.pum.tomasz.mobileengineerassignment1.activity.RepositoryActivity;
import com.pum.tomasz.mobileengineerassignment1.injector.module.RepositoriesModule;
import com.pum.tomasz.mobileengineerassignment1.injector.module.RepositoryModule;
import com.pum.tomasz.mobileengineerassignment1.injector.scope.PerActivity;

import dagger.Component;

/**
 * Created by tomasz on 21.10.2017.
 */

@PerActivity
@Component (dependencies = ApplicationComponent.class,
        modules = {
                RepositoriesModule.class,
                RepositoryModule.class
        })

public interface RepositoriesComponent {
    void inject(RepositoriesActivity repositoriesActivity);
    void inject(RepositoryActivity repositoryActivity);
}

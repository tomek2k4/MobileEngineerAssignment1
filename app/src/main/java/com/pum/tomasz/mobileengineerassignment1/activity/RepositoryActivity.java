package com.pum.tomasz.mobileengineerassignment1.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.pum.tomasz.mobileengineerassignment1.MobileEngineerAssignment1Application;
import com.pum.tomasz.mobileengineerassignment1.R;
import com.pum.tomasz.mobileengineerassignment1.injector.component.ApplicationComponent;
import com.pum.tomasz.mobileengineerassignment1.injector.component.DaggerRepositoriesComponent;
import com.pum.tomasz.mobileengineerassignment1.injector.component.RepositoriesComponent;
import com.pum.tomasz.mobileengineerassignment1.model.RepositoryItem;
import com.pum.tomasz.mobileengineerassignment1.presenter.RepositoryPresenter;
import com.pum.tomasz.mobileengineerassignment1.view.RepositoryView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RepositoryActivity extends AppCompatActivity implements RepositoryView {

    @Inject
    RepositoryPresenter repositoryPresenter;

    @Bind(R.id.ar_repository_details)
    TextView repoDetailsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository);
        ButterKnife.bind(this);
        initPresenter();
        init(savedInstanceState);
    }


    private void init(Bundle savedInstanceState) {
        Bundle extras = savedInstanceState != null ? savedInstanceState : getIntent().getExtras();
        RepositoryItem repoItem = (RepositoryItem) extras.getSerializable("repoItem");
        repositoryPresenter.attachView(this);
        repositoryPresenter.setRepositoryItem(repoItem);
    }

    private void initPresenter() {
        ApplicationComponent applicationComponent = ((MobileEngineerAssignment1Application) getApplication()).getApplicationComponent();

        RepositoriesComponent repositoriesComponent = DaggerRepositoriesComponent.builder()
                .applicationComponent(applicationComponent)
                .build();
        repositoriesComponent.inject(this);

        repositoryPresenter.onCreate();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("repoItem", repositoryPresenter.getRepositoryItem());
    }


    @Override
    public void showRepository(RepositoryItem repositoryItem) {
        repoDetailsTextView.append(String.format("%s : %s \n","Name",repositoryItem.getName()));
        repoDetailsTextView.append(String.format("%s : %s \n","Owner",repositoryItem.getOwner().getLogin()));
        repoDetailsTextView.append(String.format("%s : %s \n","Description",repositoryItem.getDescription()));
        repoDetailsTextView.append(String.format("%s : %s \n","Url",repositoryItem.getUrl()));
        repoDetailsTextView.append(String.format("%s : %s \n","Created at",repositoryItem.getCreatedAt()));
        repoDetailsTextView.append(String.format("%s : %s \n","Homepage",repositoryItem.getHomepage()));
    }
}

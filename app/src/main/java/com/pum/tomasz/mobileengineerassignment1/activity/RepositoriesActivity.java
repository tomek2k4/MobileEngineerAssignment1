package com.pum.tomasz.mobileengineerassignment1.activity;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;

import com.pum.tomasz.mobileengineerassignment1.R;
import com.pum.tomasz.mobileengineerassignment1.model.RepositoryItem;
import com.pum.tomasz.mobileengineerassignment1.presenter.RepositoriesPresenter;
import com.pum.tomasz.mobileengineerassignment1.view.RepositoriesView;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

public class RepositoriesActivity extends AppCompatActivity implements RepositoriesView {

    @Inject
    RepositoriesPresenter repositoriesPresenter;

    @Bind(R.id.fc_list)
    RecyclerView eventsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repositories);


    }

    @Override
    public void showRepositories(List<RepositoryItem> repos) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void showEmpty() {

    }
}

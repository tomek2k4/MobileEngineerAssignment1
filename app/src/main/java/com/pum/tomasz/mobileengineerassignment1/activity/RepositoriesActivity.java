package com.pum.tomasz.mobileengineerassignment1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.pum.tomasz.mobileengineerassignment1.MobileEngineerAssignment1Application;
import com.pum.tomasz.mobileengineerassignment1.R;
import com.pum.tomasz.mobileengineerassignment1.adapter.RepositoriesAdapter;
import com.pum.tomasz.mobileengineerassignment1.injector.component.ApplicationComponent;
import com.pum.tomasz.mobileengineerassignment1.injector.component.DaggerRepositoriesComponent;
import com.pum.tomasz.mobileengineerassignment1.injector.component.RepositoriesComponent;
import com.pum.tomasz.mobileengineerassignment1.injector.module.ActivityModule;
import com.pum.tomasz.mobileengineerassignment1.model.RepositoryItem;
import com.pum.tomasz.mobileengineerassignment1.presenter.RepositoriesPresenter;
import com.pum.tomasz.mobileengineerassignment1.view.RepositoriesView;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class RepositoriesActivity extends AppCompatActivity implements RepositoriesView {

    @Inject
    RepositoriesPresenter repositoriesPresenter;

    @Bind(R.id.ar_list)
    RecyclerView reposList;

    @Bind(R.id.ar_refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @Bind(R.id.ar_loading)
    View loadingView;

    @Bind(R.id.ar_empty_list)
    View emptyListView;

    @Bind(R.id.ar_search_edit_text)
    EditText searchEditText;

    private RepositoriesAdapter repositoriesAdapter;
    private RepositoriesComponent repositoriesComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repositories);
        ButterKnife.bind(this);
        initAdapter();
        initList();
        injectDependencies();
        repositoriesPresenter.onCreate();

        initRepositories();
        setupRefreshLayout();

        initOnClicListenerSubscription();
        initSearchTextChangedListener();
    }

    private void initSearchTextChangedListener() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if(charSequence.length() != 0){
                    repositoriesPresenter.filterRepositories(charSequence.toString());
                }else{
                    repositoriesPresenter.onRefresh();
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        searchEditText.setText("");
        repositoriesPresenter.onStart();
        initOnClicListenerSubscription();

    }

    @Override
    protected void onStop() {
        super.onStop();
        repositoriesPresenter.onStop();
    }

    @Override
    public void showRepositories(final List<RepositoryItem> repos) {
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
                loadingView.setVisibility(View.GONE);
                reposList.setVisibility(View.VISIBLE);
                addRepositories(repos);
            }
        };
        runOnUiThread(myRunnable);

    }

    @Override
    public void showRepositoryDetail(RepositoryItem repositoryItem) {
        // repository item will be shown in new activity
        Intent repositoryDetailsIntent = new Intent(RepositoriesActivity.this,RepositoryActivity.class);
        repositoryDetailsIntent.putExtra("repoItem",repositoryItem);
        startActivity(repositoryDetailsIntent);
    }


    public void addRepositories(Collection<RepositoryItem> reposCollection) {
        repositoriesAdapter.replaceRepositories(reposCollection);
        reposList.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showLoading() {
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                emptyListView.setVisibility(View.GONE);
                loadingView.setVisibility(View.VISIBLE);
                refreshLayout.setRefreshing(true);
                reposList.setVisibility(View.INVISIBLE);
            }
        };
        runOnUiThread(myRunnable);
    }



    @Override
    public void showError() {
        showEmpty();
    }

    @Override
    public void showEmpty() {
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
                loadingView.setVisibility(View.GONE);
                reposList.setVisibility(View.GONE);
                emptyListView.setVisibility(View.VISIBLE);
            }
        };
        runOnUiThread(myRunnable);
    }

    private void initAdapter() {
        if (repositoriesAdapter == null) {
            repositoriesAdapter = new RepositoriesAdapter(this);
        }
    }

    private void initList() {
        reposList.setLayoutManager(new LinearLayoutManager(this));
        reposList.setAdapter(repositoriesAdapter);
    }

    private void injectDependencies() {
        ApplicationComponent appComponent = ((MobileEngineerAssignment1Application) getApplication()).getApplicationComponent();

        RepositoriesComponent repositoriesComponent = DaggerRepositoriesComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(appComponent)
                .build();
        repositoriesComponent.inject(this);

    }

    private void setupRefreshLayout() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                searchEditText.setText("");
                repositoriesPresenter.onRefresh();
            }
        });
    }

    private void initRepositories() {
        repositoriesPresenter.attachView(this);
    }

    private void initOnClicListenerSubscription() {
        if(repositoriesPresenter.getClickItemSubscription() == null || repositoriesPresenter.getClickItemSubscription().isDisposed()) {
            repositoriesPresenter.setClickItemSubscription(
                repositoriesAdapter.getPositionClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.computation())
                        .subscribe(new Consumer<RepositoryItem>() {
                            @Override
                            public void accept(RepositoryItem repositoryItem) throws Exception {
                                Log.d("Tomek","Clicked on repository with name: " + repositoryItem.getName());

                                repositoriesPresenter.setSelectedItem(repositoryItem);
                            }
                        })
            );
        }

    }


}

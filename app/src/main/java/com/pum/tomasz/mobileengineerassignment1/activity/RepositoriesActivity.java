package com.pum.tomasz.mobileengineerassignment1.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pum.tomasz.mobileengineerassignment1.BuildConfig;
import com.pum.tomasz.mobileengineerassignment1.R;
import com.pum.tomasz.mobileengineerassignment1.adapter.RepositoriesAdapter;
import com.pum.tomasz.mobileengineerassignment1.frontend.DataProvider;
import com.pum.tomasz.mobileengineerassignment1.frontend.FetchSquareRepositoriesUsecase;
import com.pum.tomasz.mobileengineerassignment1.frontend.RetrofitRestDataProvider;
import com.pum.tomasz.mobileengineerassignment1.gson.JsonExclusionStrategy;
import com.pum.tomasz.mobileengineerassignment1.gson.ResponseDeserializer;
import com.pum.tomasz.mobileengineerassignment1.model.GithubSearchResultWrapper;
import com.pum.tomasz.mobileengineerassignment1.model.RepositoryItem;
import com.pum.tomasz.mobileengineerassignment1.presenter.RepositoriesPresenter;
import com.pum.tomasz.mobileengineerassignment1.view.RepositoriesView;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class RepositoriesActivity extends AppCompatActivity implements RepositoriesView {

    public static final FieldNamingPolicy API_JSON_NAMING_POLICY = FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;

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
    private Retrofit retrofit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repositories);
        ButterKnife.bind(this);
        initAdapter();
        initList();
        initializeRetrofit();
        initPresenter();

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
                    Log.d("Tomek","Text changed");
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

    private void initializeRetrofit() {
        String endpointUrl = BuildConfig.githubApiEndpointUrl;

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(API_JSON_NAMING_POLICY)
                .registerTypeAdapter(GithubSearchResultWrapper.class, new ResponseDeserializer())
                .addSerializationExclusionStrategy(new JsonExclusionStrategy())
                .addDeserializationExclusionStrategy(new JsonExclusionStrategy())
                .create();

        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);
        OkHttpClient client = new OkHttpClient();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(logging);
        retrofit = new Retrofit.Builder()
                .baseUrl(endpointUrl)
                .client(client)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    private void initPresenter() {
        DataProvider dataProvider = new RetrofitRestDataProvider(retrofit);
        FetchSquareRepositoriesUsecase fetchSquareRepositoriesUsecase = new FetchSquareRepositoriesUsecase(dataProvider);
        repositoriesPresenter = new RepositoriesPresenter(fetchSquareRepositoriesUsecase);
        repositoriesPresenter.onCreate();
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
        if(repositoriesPresenter.getClickItemSubscription() == null || repositoriesPresenter.getClickItemSubscription().isUnsubscribed()) {
            repositoriesPresenter.setClickItemSubscription(
                repositoriesAdapter.getPositionClicks()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<RepositoryItem>() {
                            @Override
                            public void call(RepositoryItem repositoryItem) {
                                Log.d("Tomek","Clicked on repository with name: " + repositoryItem.getName());

                                repositoriesPresenter.setSelectedItem(repositoryItem);
                            }
                        })
            );
        }

    }


}

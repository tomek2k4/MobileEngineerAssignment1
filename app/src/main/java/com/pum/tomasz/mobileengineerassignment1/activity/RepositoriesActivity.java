package com.pum.tomasz.mobileengineerassignment1.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;

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


    }

    @Override
    protected void onStart() {
        super.onStart();
        repositoriesPresenter.onStart();
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


    public void addRepositories(Collection<RepositoryItem> reposCollection) {
        repositoriesAdapter.replaceEvents(reposCollection);
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
                repositoriesPresenter.onRefresh();
            }
        });
    }

    private void initRepositories() {
        repositoriesPresenter.attachView(this);
    }


}

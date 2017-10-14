package com.pum.tomasz.mobileengineerassignment1.frontend;

import com.pum.tomasz.mobileengineerassignment1.model.GithubSearchResultWrapper;
import com.pum.tomasz.mobileengineerassignment1.model.RepositoryItem;
import com.pum.tomasz.mobileengineerassignment1.service.GithubService;

import java.util.List;

import retrofit.Retrofit;
import rx.Observable;

/**
 * Created by tomasz on 13.10.2017.
 */

public class RetrofitRestDataProvider implements DataProvider {

    public static final String SQUARE_USER = "user:square";
    public static final String MAX_NUMBER_OF_REPOSITORIES = "20";

    private GithubService apiService;

    public RetrofitRestDataProvider(Retrofit retrofit) {
        apiService = retrofit.create(GithubService.class);
    }

    @Override
    public Observable<GithubSearchResultWrapper<List<RepositoryItem>>> getRepositoriesForUserSquare() {
        return apiService.getRepositoriesForUser(SQUARE_USER,MAX_NUMBER_OF_REPOSITORIES);
    }
}

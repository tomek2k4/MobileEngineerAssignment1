package com.pum.tomasz.mobileengineerassignment1.service;

import com.pum.tomasz.mobileengineerassignment1.model.GitResult;
import com.pum.tomasz.mobileengineerassignment1.model.GithubSearchResultWrapper;
import com.pum.tomasz.mobileengineerassignment1.model.RepositoryItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by tomasz on 12.10.2017.
 */

public interface GithubService {

    @GET("search/repositories")
    Observable<GithubSearchResultWrapper<List<RepositoryItem>>> getRepositoriesForUser(@Query("q") String name,
                                                                                       @Query("per_page") String resNumber);
}

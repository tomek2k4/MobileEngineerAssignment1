package com.pum.tomasz.mobileengineerassignment1.frontend;

import com.pum.tomasz.mobileengineerassignment1.model.GithubSearchResultWrapper;
import com.pum.tomasz.mobileengineerassignment1.model.RepositoryItem;

import java.util.List;

import rx.Observable;

public interface DataProvider {
    Observable<GithubSearchResultWrapper<List<RepositoryItem>>> getRepositoriesForUserSquare();
}
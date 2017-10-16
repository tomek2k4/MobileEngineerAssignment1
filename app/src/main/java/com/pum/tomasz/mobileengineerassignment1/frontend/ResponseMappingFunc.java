package com.pum.tomasz.mobileengineerassignment1.frontend;

import com.pum.tomasz.mobileengineerassignment1.model.GithubSearchResultWrapper;

import io.reactivex.functions.Function;


public class ResponseMappingFunc<R> implements Function<GithubSearchResultWrapper<R>, R> {

    @Override
    public R apply(GithubSearchResultWrapper<R> rGithubSearchResultWrapper) throws Exception {
        if (rGithubSearchResultWrapper == null) {
            return null;
        }
        return rGithubSearchResultWrapper.body;
    }
}
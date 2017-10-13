package com.pum.tomasz.mobileengineerassignment1.frontend;

import com.pum.tomasz.mobileengineerassignment1.model.GithubSearchResultWrapper;

import rx.functions.Func1;

public class ResponseMappingFunc<R> implements Func1<GithubSearchResultWrapper<R>, R> {

    @Override
    public R call(GithubSearchResultWrapper<R> rGithubSearchResultWrapper) {
            if (rGithubSearchResultWrapper == null) {
                return null;
            }
            return rGithubSearchResultWrapper.body;
    }
}
package com.pum.tomasz.mobileengineerassignment1.frontend;


import io.reactivex.Observable;

public interface Usecase<T> {
    Observable<T> execute();
}
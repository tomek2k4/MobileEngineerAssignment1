package com.pum.tomasz.mobileengineerassignment1.frontend;

import rx.Observable;

public interface Usecase<T> {
    Observable<T> execute();
}
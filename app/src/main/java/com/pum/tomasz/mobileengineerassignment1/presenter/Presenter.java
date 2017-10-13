package com.pum.tomasz.mobileengineerassignment1.presenter;


import com.pum.tomasz.mobileengineerassignment1.view.View;

public interface Presenter<T extends View> {
    void onCreate();

    void onStart();

    void onStop();

    void onPause();

    void attachView(T view);

}

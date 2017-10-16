package com.pum.tomasz.mobileengineerassignment1.frontend;

import com.pum.tomasz.mobileengineerassignment1.model.RepositoryItem;

import java.util.List;

import io.reactivex.Observable;


/**
 * Created by tomasz on 13.10.2017.
 */

public class FetchSquareRepositoriesUsecase implements Usecase<List<RepositoryItem>> {

    private DataProvider dataProvider;

    public FetchSquareRepositoriesUsecase(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public Observable<List<RepositoryItem>> execute() {
        return dataProvider.getRepositoriesForUserSquare().map(new ResponseMappingFunc<List<RepositoryItem>>());
    }
}

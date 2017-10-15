package com.pum.tomasz.mobileengineerassignment1.view;

import com.pum.tomasz.mobileengineerassignment1.model.RepositoryItem;

import java.util.List;

/**
 * Created by tomasz on 13.10.2017.
 */

public interface RepositoriesView extends View{

    void showLoading();
    void showRepositories(List<RepositoryItem> repos);
    void showRepositoryDetail(RepositoryItem repositoryItem);
    void showError();
    void showEmpty();
}

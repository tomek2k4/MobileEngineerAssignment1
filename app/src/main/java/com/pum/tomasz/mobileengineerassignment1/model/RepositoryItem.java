package com.pum.tomasz.mobileengineerassignment1.model;


import lombok.Data;
/**
 * Created by tomasz on 12.10.2017.
 */


@Data
public class RepositoryItem {
    private final String id;
    private final String name;

    public RepositoryItem(String id, String name) {
        this.id = id;
        this.name = name;
    }
}

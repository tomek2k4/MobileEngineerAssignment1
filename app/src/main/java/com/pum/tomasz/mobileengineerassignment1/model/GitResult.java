package com.pum.tomasz.mobileengineerassignment1.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Created by tomasz on 12.10.2017.
 */

@Data
public class GitResult {
    private Integer totalCount;
    private Boolean incompleteResults;
    private List<RepositoryItem> items = new ArrayList<RepositoryItem>();
}

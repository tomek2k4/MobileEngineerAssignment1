package com.pum.tomasz.mobileengineerassignment1.model;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by tomasz on 12.10.2017.
 */

@Data
public class Owner implements Serializable {

    private String login;
    private Integer id;
    private String avatarUrl;
    private String gravatarId;
    private String url;
    private String htmlUrl;
    private String followersUrl;
    private String followingUrl;
    private String gistsUrl;
    private String starredUrl;
    private String subscriptionsUrl;
    private String organizationsUrl;
    private String reposUrl;
    private String eventsUrl;
    private String receivedEventsUrl;
    private String type;
    private Boolean siteAdmin;
}

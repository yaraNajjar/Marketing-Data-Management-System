package com.tsfn.loaders.helper;

import lombok.Getter;

@Getter
public enum FieldName {
    ID("id"),
    TIMESTAMP("timestamp"),
    POST_ID("postID"),
    CONTENT_TYPE("contentType"),
    IMPRESSIONS("impressions"),
    VIEWS("views"),
    OFFSITE_VIEWS("offsiteViews"),
    CLICKS("clicks"),
    CTR("ctr"),
    LIKES("likes"),
    COMMENTS("comments"),
    SHARES("shares"),
    ENGAGEMENT_RATE("engagementRate"),
    RCS("Reactions, Comments and Shares"),
    DESCRIPTION("description"),
    LINKEDIN("linkedin"),
    FACEBOOK("facebook"),
    INSTAGRAM("instagram");


    private final String fieldName;

    FieldName(String fieldName) {
        this.fieldName = fieldName;
    }



}

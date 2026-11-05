package com.salesforce.metrics.helper;

import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class Helper {

    public List<String> getColumns() {
        return Arrays.asList(
                "impressions",
                "views",
                "clicks",
                "ctr",
                "likes",
                "comments",
                "shares",
                "engagementRate"
        );
    }
}

package com.tsfn.loaders.helper;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsfn.loaders.helper.FieldName.*;

public class Helper {

    public static MapInfo mappingExcelToListOfDats(List<String> headerList, String fileName) {
        return switch (fileName) {
            case "linkedin" -> new MapInfo(LINKEDIN.getFieldName(), mapperColumnLinkedin(headerList));
            case "facebook" -> new MapInfo(FACEBOOK.getFieldName(), mapperColumnFacebook(headerList));
            case "instagram" -> new MapInfo(INSTAGRAM.getFieldName(), mapperColumnInstagram(headerList));
            default -> null;
        };
    }

    private static Map<String, Integer> mapperColumnLinkedin(List<String> headers) {
        Map<String, Integer> linkedin = new HashMap<>();
        linkedin.put(POST_ID.getFieldName(), headers.indexOf("Post link"));
        linkedin.put(CONTENT_TYPE.getFieldName(), headers.indexOf("Content Type"));
        linkedin.put(IMPRESSIONS.getFieldName(), headers.indexOf("Impressions"));
        linkedin.put(VIEWS.getFieldName(), headers.indexOf("Views (Excluding offsite video views)"));
        linkedin.put(OFFSITE_VIEWS.getFieldName(), headers.indexOf("Offsite Views"));
        linkedin.put(CLICKS.getFieldName(), headers.indexOf("Clicks"));
        linkedin.put(CTR.getFieldName(), headers.indexOf("Click through rate (CTR)"));
        linkedin.put(LIKES.getFieldName(), headers.indexOf("Likes"));
        linkedin.put(COMMENTS.getFieldName(), headers.indexOf("Comments"));
        linkedin.put(SHARES.getFieldName(), headers.indexOf("Reposts"));
        linkedin.put(DESCRIPTION.getFieldName(), headers.indexOf("Post title"));
        linkedin.put(ENGAGEMENT_RATE.getFieldName(), headers.indexOf("Engagement rate"));
        return linkedin;
    }

    private static Map<String, Integer> mapperColumnFacebook(List<String> headers) {
        Map<String, Integer> facebook = new HashMap<>();
        facebook.put(POST_ID.getFieldName(), headers.indexOf("Post ID"));
        facebook.put(CONTENT_TYPE.getFieldName(), headers.indexOf("Post type"));
        facebook.put(IMPRESSIONS.getFieldName(), headers.indexOf("Impressions"));
        facebook.put(VIEWS.getFieldName(), headers.indexOf("Reach"));
        facebook.put(CLICKS.getFieldName(), headers.indexOf("Total clicks"));
        facebook.put(LIKES.getFieldName(), headers.indexOf("Reactions"));
        facebook.put(COMMENTS.getFieldName(), headers.indexOf("Comments"));
        facebook.put(SHARES.getFieldName(), headers.indexOf("Shares"));
        facebook.put(DESCRIPTION.getFieldName(), headers.indexOf("Description"));
        facebook.put(RCS.getFieldName(), headers.indexOf("Reactions, Comments and Shares"));
        return facebook;
    }

    private static Map<String, Integer> mapperColumnInstagram(List<String> headers) {
        Map<String, Integer> instagram = new HashMap<>();
        instagram.put(POST_ID.getFieldName(), headers.indexOf("Post ID"));
        instagram.put(CONTENT_TYPE.getFieldName(), headers.indexOf("Post type"));
        instagram.put(IMPRESSIONS.getFieldName(), headers.indexOf("Impressions"));
        instagram.put(VIEWS.getFieldName(), headers.indexOf("Reach"));
        instagram.put(CLICKS.getFieldName(), headers.indexOf("Saves"));
        instagram.put(LIKES.getFieldName(), headers.indexOf("Likes"));
        instagram.put(COMMENTS.getFieldName(), headers.indexOf("Comments"));
        instagram.put(DESCRIPTION.getFieldName(), headers.indexOf("Description"));
        instagram.put(SHARES.getFieldName(), headers.indexOf("Shares"));
        return instagram;
    }

    public static String currentTimestamp() {
        Instant now = Instant.now();
        ZoneId israelTimeZone = ZoneId.of("Asia/Jerusalem");
        Timestamp currentTimestamp = Timestamp.from(now.atZone(israelTimeZone).toInstant());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSSS");
        return dateFormat.format(currentTimestamp);
    }
}






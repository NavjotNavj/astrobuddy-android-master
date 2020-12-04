package in.appnow.astrobuddy.rest;


import in.appnow.astrobuddy.BuildConfig;

/**
 * Created by sonu on 12:16, 13/07/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class RestUtils {

    public static String getEndPoint() {
        return BuildConfig.DEBUG ? BuildConfig.STAGING_END_POINT : BuildConfig.PRODUCTION_END_POINT;
    }

    public static String getPlacesAPIKey() {
        return BuildConfig.PLACES_API_KEY1.replace(":", "") + BuildConfig.PLACES_API_KEY2.replace(":", "");
    }

    public static String getYoutubeAPIKey() {
        return BuildConfig.YOUTUBE_API_KEY1.replace(":", "") + BuildConfig.YOUTUBE_API_KEY2.replace(":", "");
    }

    public static String getAstroBlurtAPIKey() {
        return BuildConfig.DEBUG ? BuildConfig.ASTRO_BLURT_STAGING_KEY : BuildConfig.ASTRO_BLURT_PRODUCTION_KEY;
    }

}

package in.appnow.astrobuddy.helper;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import in.appnow.astrobuddy.horoscope_chart.APITask;
import in.appnow.astrobuddy.models.LogoutModel;
import in.appnow.astrobuddy.models.PendingFeedbackModel;
import in.appnow.astrobuddy.rest.response.PromoBannerResponse;
import in.appnow.astrobuddy.rest.response.LoginResponseModel;

/**
 * Created by Abhishek Thanvi on 28/03/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class PreferenceManger {

    public static final String FCM_TOKEN = "fcm_token";

    public static final String LOAD_MESSAGES_ONE_TIME = "load_messages_one_time";
    public static final String MESSAGE_UNREAD_COUNTER = "message_unread_counter";
    public static final String INTRO_SCREEN = "intro_screen";
    public static final String WELCOME_INTRO_DIALOG = "welcome_intro_dialog";
    public static final String ALL_HINT = "all_hint";

    public static final String CONVERSATION_NOTIFY_TYPE = "conversation_notify_type";
    public static final String HANDLER_ID = "handler_id";
    public static final String CHAT_SESSION_ID = "chat_session_id";
    public static final String CHAT_QUERY = "chat_query";

    public static final String AUTH_TOKEN = "auth_token";
    public static final String NODE_AUTH_TOKEN = "node_auth_token";
    public static final String APP_DOWNLOAD_API = "app_download";
    public static final String UUID = "uuid";

    public static final String LOGIN_DETAILS = "login_details";

    public static final String CHAT_TAP_HINT = "chat_tap_hint_new";
    public static final String CHAT_BACK_PRESS_TAP_HINT = "chat_back_press_tap_hint";

    public static final String CHART_TAP_HINT = "chart_tap_hint";
    public static final String CHART_BACK_PRESS_TAP_HINT = "chart_back_press_tap_hint";

    public static final String HOROSCOPE_TAP_HINT = "horoscope_tap_hint_new";
    public static final String HOROSCOPE_BACK_PRESS_TAP_HINT = "horoscope_back_press_tap_hint";

    public static final String SIDE_MENU_HINT = "side_menu_hint";
    public static final String FORECAST_TAP_HINT = "forecast_hint";
    public static final String FORECAST_SCREEN_SHOW = "forecast_screen_shown";

    public static final String MY_ACCOUNT_HINT = "my_account_hint";
    public static final String MY_ACCOUNT_BACK_PRESS_HINT = "my_account_back_press_hint";

    public static final String MYTH_BUSTER_HINT = "myth_buster_hint_new";
    public static final String MYTH_BUSTER_BACK_PRESS_HINT = "myth_buster_back_press_hint";
    public static final String MATCH_MAKING_HINT = "match_making_hint";

    public static final String BUY_CREDITS_HINT = "buy_credits_hint_new";
    public static final String BUY_CREDITS_BACK_PRESS_HINT = "buy_credits_back_press_hint";

    public static final String LIVE_CALL_HINT = "live_call_hint_new";
    public static final String LIVE_CALL_BACK_PRESS_HINT = "live_call_back_press_hint";

    public static final String PENDING_FEEDBACK = "pending_feedback";
    public static final String LOGOUT_MODEL = "logout_model";

    public static final String TIP_OF_THE_DAY = "tip_of_the_day";
    public static final String TOD_DAILY_JOB = "tod_daily_job";
    public static final String USER_STATS_DAILY_JOB = "user_stats_daily_job";

    public static final String PROMO_BANNER = "promo_banner";

    public static final String ASTROLOGY_API_LANGUAGE = "astrology_api_language";

    public static final String PANCHANG_LOCATION = "panchang_location";
    public static final String PANCHANG_LATLNG = "panchang_latlng";

    private SharedPreferences mSharedPreferences;

    public PreferenceManger(SharedPreferences mSharedPreferences) {
        this.mSharedPreferences = mSharedPreferences;
    }

    private SharedPreferences.Editor getEditor() {
        return mSharedPreferences.edit();
    }

    public void remove(String key) {
        SharedPreferences.Editor editor = getEditor();
        editor.remove(key);
        editor.apply();
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(key, value);
        editor.apply();
    }


    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putInt(key, value);
        editor.apply();
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public String getStringValue(String key) {
        return mSharedPreferences.getString(key, "");
    }

    public boolean getBooleanValue(String key) {
        return mSharedPreferences.getBoolean(key, false);
    }

    public boolean getBooleanValue(String key, boolean defaultValue) {
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    public int getIntegerValue(String key) {
        return mSharedPreferences.getInt(key, 0);
    }


    public void logoutUser() {
        putUserDetails(null);
        putString(AUTH_TOKEN, "");
        putString(NODE_AUTH_TOKEN,"");
        putString(PANCHANG_LATLNG, "");
        putString(PANCHANG_LOCATION, "");
        putString(ASTROLOGY_API_LANGUAGE, APITask.ENGLISH_LANG);
        putPromoBanner(null);
    }


    public void putUserDetails(LoginResponseModel loginResponseModel) {
        Gson gson = new Gson();
        String json = gson.toJson(loginResponseModel);
        putString(LOGIN_DETAILS, json);
    }

    public LoginResponseModel getUserDetails() {
        Gson gson = new Gson();
        String json = getStringValue(LOGIN_DETAILS);
        return gson.fromJson(json, LoginResponseModel.class);
    }


    public void putPendingFeedback(PendingFeedbackModel pendingFeedbackModel) {
        Gson gson = new Gson();
        String json = gson.toJson(pendingFeedbackModel);
        putString(PENDING_FEEDBACK, json);
    }

    public PendingFeedbackModel getPendingFeedback() {
        Gson gson = new Gson();
        String json = getStringValue(PENDING_FEEDBACK);
        return gson.fromJson(json, PendingFeedbackModel.class);
    }

    public void putPromoBanner(PromoBannerResponse.PromoBanner promoBanner) {
        Gson gson = new Gson();
        String json = gson.toJson(promoBanner);
        putString(PROMO_BANNER, json);
    }

    public PromoBannerResponse.PromoBanner getPromoBanner() {
        Gson gson = new Gson();
        String json = getStringValue(PROMO_BANNER);
        return gson.fromJson(json, PromoBannerResponse.PromoBanner.class);
    }

    public void putSessionExpiredModel(LogoutModel logoutModel) {
        Gson gson = new Gson();
        String json = gson.toJson(logoutModel);
        putString(LOGOUT_MODEL, json);
    }

    public LogoutModel getSessionExpiredModel() {
        Gson gson = new Gson();
        String json = getStringValue(LOGOUT_MODEL);
        return gson.fromJson(json, LogoutModel.class);
    }
}

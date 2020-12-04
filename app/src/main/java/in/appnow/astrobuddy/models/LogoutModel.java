package in.appnow.astrobuddy.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 20:31, 03/08/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class LogoutModel {
    @SerializedName("title")
    private String title;
    @SerializedName("body")
    private String body;
    @SerializedName("isLogout")
    private boolean forceLogout;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isForceLogout() {
        return forceLogout;
    }

    public void setForceLogout(boolean forceLogout) {
        this.forceLogout = forceLogout;
    }
}

package in.appnow.astrobuddy.rest.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 11:17, 30/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class StartChatRequest extends BaseRequestModel {
    @SerializedName("notify_User")
    private boolean notifyUser;

    public void setNotifyUser(boolean notifyUser) {
        this.notifyUser = notifyUser;
    }
}

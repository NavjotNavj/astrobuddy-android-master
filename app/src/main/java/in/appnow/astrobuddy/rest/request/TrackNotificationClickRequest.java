package in.appnow.astrobuddy.rest.request;

import com.google.gson.annotations.SerializedName;

import in.appnow.astrobuddy.rest.response.BaseResponseModel;

/**
 * Created by sonu on 14:20, 02/08/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class TrackNotificationClickRequest extends BaseRequestModel {
    @SerializedName("noti_id")
    private int notificationId;

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }
}

package in.appnow.astrobuddy.rest.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 14:41, 16/07/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class AppVersionRequest extends BaseRequestModel{
    @SerializedName("version_code")
    private int versionCode;

    @SerializedName("device_type")
    private String deviceType;

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}

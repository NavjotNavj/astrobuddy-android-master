package in.appnow.astrobuddy.rest.request;

import com.google.gson.annotations.SerializedName;

import in.appnow.astrobuddy.utils.StringUtils;

/**
 * Created by sonu on 17:15, 04/06/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class UpdateMaritalStatusRequest extends BaseRequestModel {
    @SerializedName("marital_status")
    private String maritalStatus;

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }
}

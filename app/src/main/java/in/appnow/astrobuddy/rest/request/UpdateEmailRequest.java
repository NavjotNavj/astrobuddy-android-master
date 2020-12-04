package in.appnow.astrobuddy.rest.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 21:55, 26/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class UpdateEmailRequest extends BaseRequestModel {
    @SerializedName("email")
    private String emailId;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}

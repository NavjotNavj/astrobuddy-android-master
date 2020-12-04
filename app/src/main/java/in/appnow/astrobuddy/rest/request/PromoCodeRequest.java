package in.appnow.astrobuddy.rest.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 12:01, 04/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class PromoCodeRequest extends BaseRequestModel {
    @SerializedName("purpose")
    private String purpose;
    @SerializedName("locale")
    private String locale;

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public String toString() {
        return "PromoCodeRequest{" +
                ", purpose='" + purpose + '\'' +
                ", locale='" + locale + '\'' +
                '}';
    }
}

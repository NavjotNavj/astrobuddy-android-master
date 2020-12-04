package in.appnow.astrobuddy.rest.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 20:05, 17/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ReferralCodeResponse extends BaseResponseModel {
    @SerializedName("user_exists")
    private boolean userExist;
    @SerializedName("referral_code")
    private String referralCode;

    public boolean isUserExist() {
        return userExist;
    }

    public String getReferralCode() {
        return referralCode;
    }
}

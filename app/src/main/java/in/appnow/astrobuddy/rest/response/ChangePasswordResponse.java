package in.appnow.astrobuddy.rest.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 11:41, 27/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ChangePasswordResponse extends BaseResponseModel {
    @SerializedName("user_exsist")
    private boolean isUserExist;
    @SerializedName("password_match")
    private boolean passwordMatched;
    @SerializedName("password_updated")
    private boolean passwordUpdated;

    public boolean isUserExist() {
        return isUserExist;
    }

    public void setUserExist(boolean userExist) {
        isUserExist = userExist;
    }

    public boolean isPasswordMatched() {
        return passwordMatched;
    }

    public void setPasswordMatched(boolean passwordMatched) {
        this.passwordMatched = passwordMatched;
    }

    public boolean isPasswordUpdated() {
        return passwordUpdated;
    }

    public void setPasswordUpdated(boolean passwordUpdated) {
        this.passwordUpdated = passwordUpdated;
    }
}

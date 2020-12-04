package in.appnow.astrobuddy.rest.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 21:55, 26/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class UpdateEmailResponse extends BaseResponseModel {
    @SerializedName("user_exsist")
    private boolean userExist;
    @SerializedName("email_updated")
    private boolean emailUpdated;

    public boolean getUserExist() {
        return userExist;
    }

    public void setUserExist(boolean userExist) {
        this.userExist = userExist;
    }

    public boolean getEmailUpdated() {
        return emailUpdated;
    }

    public void setEmailUpdated(boolean emailUpdated) {
        this.emailUpdated = emailUpdated;
    }
}

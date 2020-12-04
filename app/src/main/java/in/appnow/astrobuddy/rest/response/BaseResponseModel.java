package in.appnow.astrobuddy.rest.response;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Abhishek Thanvi on 29/03/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class BaseResponseModel {

    @SerializedName("message")
    private String errorMessage;

    @SerializedName("error_status")
    private boolean errorStatus;

    @SerializedName("logout_user")
    private boolean logoutUser;

    public BaseResponseModel() {
    }


    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(boolean errorStatus) {
        this.errorStatus = errorStatus;
    }

    public boolean isLogoutUser() {
        return logoutUser;
    }

    public void setLogoutUser(boolean logoutUser) {
        this.logoutUser = logoutUser;
    }

    @NotNull
    @Override
    public String toString() {
        return "BaseResponseModel{" +
                "errorMessage='" + errorMessage + '\'' +
                ", errorStatus=" + errorStatus +
                ", logoutUser=" + logoutUser +
                '}';
    }
}

package in.appnow.astrobuddy.rest.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 14:56, 27/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class UpdateImageRequest extends BaseRequestModel {

    @SerializedName("profile_img")
    private String profileImage;

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}

package in.appnow.astrobuddy.rest.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 14:56, 27/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class UpdateImageResponse extends BaseResponseModel {
    @SerializedName("user_exsist")
    private boolean isUserExist;
    @SerializedName("profile_img_upload_status")
    private boolean isImageUploadStatus;
    @SerializedName("image_updated")
    private boolean isImageUpdated;
    @SerializedName("profile_img")
    private String profileImage;

    public boolean isUserExist() {
        return isUserExist;
    }

    public void setUserExist(boolean userExist) {
        isUserExist = userExist;
    }

    public boolean isImageUploadStatus() {
        return isImageUploadStatus;
    }

    public void setImageUploadStatus(boolean imageUploadStatus) {
        isImageUploadStatus = imageUploadStatus;
    }

    public boolean isImageUpdated() {
        return isImageUpdated;
    }

    public void setImageUpdated(boolean imageUpdated) {
        isImageUpdated = imageUpdated;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}

package in.appnow.astrobuddy.rest.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 14:48, 07/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class BaseRequestModel implements Parcelable{
    @SerializedName("user_id")
    private String userId;

    public BaseRequestModel() {
    }

    protected BaseRequestModel(Parcel in) {
        userId = in.readString();
    }

    public static final Creator<BaseRequestModel> CREATOR = new Creator<BaseRequestModel>() {
        @Override
        public BaseRequestModel createFromParcel(Parcel in) {
            return new BaseRequestModel(in);
        }

        @Override
        public BaseRequestModel[] newArray(int size) {
            return new BaseRequestModel[size];
        }
    };

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userId);
    }
}

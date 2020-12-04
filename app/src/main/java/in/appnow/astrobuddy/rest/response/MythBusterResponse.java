package in.appnow.astrobuddy.rest.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sonu on 16:23, 23/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class MythBusterResponse extends BaseResponseModel {
    @SerializedName("myths")
    private List<MythBuster> mythBuster;

    public MythBusterResponse() {
    }

    public List<MythBuster> getMythBuster() {
        return mythBuster;
    }

}

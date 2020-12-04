package in.appnow.astrobuddy.rest.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhishek Thanvi on 2019-08-30.
 * Copyright © 2019 Abhishek Thanvi. All rights reserved.
 */

public class UpdateSocketIdRequest {

    @SerializedName("socket_id")
    private String socketId;

    public String getSocketId() {
        return socketId;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }

}

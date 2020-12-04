package in.appnow.astrobuddy.rest.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 12:10, 21/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class StartChatResponse extends BaseResponseModel {

    @SerializedName("response")
    private StartChatResponseData responseData;

    public StartChatResponse() {
    }

    public StartChatResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(StartChatResponseData responseData) {
        this.responseData = responseData;
    }
}

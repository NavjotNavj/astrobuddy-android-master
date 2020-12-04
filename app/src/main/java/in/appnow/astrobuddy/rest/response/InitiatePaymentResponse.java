package in.appnow.astrobuddy.rest.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhishek Thanvi on 2019-08-29.
 * Copyright © 2019 Abhishek Thanvi. All rights reserved.
 */

public class InitiatePaymentResponse extends BaseResponseModel {
    
    @SerializedName("response")
    InitiatePaymentResponseData responseData;

    public InitiatePaymentResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(InitiatePaymentResponseData responseData) {
        this.responseData = responseData;
    }

    @Override
    public String toString() {
        return "InitiatePaymentResponse{" +
                "responseData=" + responseData +
                '}';
    }
}

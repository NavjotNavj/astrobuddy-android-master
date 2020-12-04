package in.appnow.astrobuddy.rest.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhishek Thanvi on 2019-08-29.
 * Copyright © 2019 Abhishek Thanvi. All rights reserved.
 */

public class InitiatePaymentResponseData {

    @SerializedName("ref_id_custom")
    String customReferenceId;

    @SerializedName("status")
    String status;

    public String getCustomReferenceId() {
        return customReferenceId;
    }

    public void setCustomReferenceId(String customReferenceId) {
        this.customReferenceId = customReferenceId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "InitiatePaymentResponseData{" +
                "customReferenceId='" + customReferenceId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

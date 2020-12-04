package in.appnow.astrobuddy.rest.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhishek Thanvi on 2019-08-29.
 * Copyright © 2019 Abhishek Thanvi. All rights reserved.
 */

public class AbortPaymentRequest {

    @SerializedName("ref_id_custom")
    private String customReferenceId;

    public String getCustomReferenceId() {
        return customReferenceId;
    }

    public void setCustomReferenceId(String customReferenceId) {
        this.customReferenceId = customReferenceId;
    }
}

package in.appnow.astrobuddy.rest.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhishek Thanvi on 2019-08-30.
 * Copyright © 2019 Abhishek Thanvi. All rights reserved.
 */

public class CapturePaymentRequest {

    @SerializedName("ref_id_custom")
    private String customReferenceId;
    @SerializedName("pg_id")
    private String paymentId;
    @SerializedName("topics")
    private String topics;
    @SerializedName("amountpre")
    private String amount;


    public String getCustomReferenceId() {
        return customReferenceId;
    }

    public void setCustomReferenceId(String customReferenceId) {
        this.customReferenceId = customReferenceId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getTopics() {
        return topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}

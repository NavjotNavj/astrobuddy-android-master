package in.appnow.astrobuddy.rest.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhishek Thanvi on 2019-09-09.
 * Copyright © 2019 Abhishek Thanvi. All rights reserved.
 */

public class CaptureCallPaymentRequest {


    @SerializedName("ref_id_custom")
    private String customReferenceId;
    @SerializedName("pg_id")
    private String paymentId;
    @SerializedName("topics")
    private String topics;
    @SerializedName("amountpre")
    private String amount;
    @SerializedName("call_minutes")
    private String callMinutes;
    @SerializedName("plan_id")
    private String planId;

    @SerializedName("call_number")
    private String callNumber;

    @SerializedName("plan_name")
    private String planName;


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

    public String getCallMinutes() {
        return callMinutes;
    }

    public void setCallMinutes(String callMinutes) {
        this.callMinutes = callMinutes;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }
}

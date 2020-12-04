package in.appnow.astrobuddy.rest.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 12:13, 04/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ApplyPromoCodeRequest extends BaseRequestModel {

    @SerializedName("promo_id")
    private int promoId;
    @SerializedName("promo_code")
    private String promoCode;
    @SerializedName("topics")
    private int topicCount;
    @SerializedName("purpose")
    private String purpose;
    @SerializedName("amount")
    private double amount;

    public int getPromoId() {
        return promoId;
    }

    public void setPromoId(int promoId) {
        this.promoId = promoId;
    }

    public int getTopicCount() {
        return topicCount;
    }

    public void setTopicCount(int topicCount) {
        this.topicCount = topicCount;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "ApplyPromoCodeRequest{" +
                "promoId='" + promoId + '\'' +
                ", promoCode='" + promoCode + '\'' +
                ", topicCount=" + topicCount +
                ", purpose='" + purpose + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}

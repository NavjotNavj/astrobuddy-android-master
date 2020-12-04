package in.appnow.astrobuddy.rest.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 18:07, 07/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ProcessTransactionRequest extends BaseRequestModel {

    @SerializedName("promo_id")
    private int promoId;
    @SerializedName("promo_code")
    private String promoCode;
    @SerializedName("promo_purpose")
    private String promoPurpose;
    @SerializedName("curr")
    private String currency;
    @SerializedName("action")
    private String action;
    @SerializedName("topic")
    private String topicCount;
    @SerializedName("final_topic")
    private String finalTopicCount;
    @SerializedName("amount")
    private String amount;
    @SerializedName("final_amount")
    private String finalAmount;
    @SerializedName("sub_plan_id")
    private int subPlanId;
    @SerializedName("pg_name")
    private String pgName;
    @SerializedName("amount_inr")
    private String amountINR;
    @SerializedName("final_amount_inr")
    private String finalAmountINR;

    public void setPgName(String pgName) {
        this.pgName = pgName;
    }

    public int getPromoId() {
        return promoId;
    }

    public void setPromoId(int promoId) {
        this.promoId = promoId;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public void setPromoPurpose(String promoPurpose) {
        this.promoPurpose = promoPurpose;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setTopicCount(String topicCount) {
        this.topicCount = topicCount;
    }

    public void setFinalTopicCount(String finalTopicCount) {
        this.finalTopicCount = finalTopicCount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setFinalAmount(String finalAmount) {
        this.finalAmount = finalAmount;
    }


    public String getPromoPurpose() {
        return promoPurpose;
    }

    public String getCurrency() {
        return currency;
    }

    public String getAction() {
        return action;
    }

    public String getTopicCount() {
        return topicCount;
    }

    public String getFinalTopicCount() {
        return finalTopicCount;
    }

    public String getAmount() {
        return amount;
    }

    public String getFinalAmount() {
        return finalAmount;
    }

    public int getSubPlanId() {
        return subPlanId;
    }

    public void setSubPlanId(int subPlanId) {
        this.subPlanId = subPlanId;
    }

    public void setAmountINR(String amountINR) {
        this.amountINR = amountINR;
    }

    public void setFinalAmountINR(String finalAmountINR) {
        this.finalAmountINR = finalAmountINR;
    }
}

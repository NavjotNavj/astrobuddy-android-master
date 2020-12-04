package in.appnow.astrobuddy.rest.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhishek Thanvi on 2019-08-29.
 * Copyright © 2019 Abhishek Thanvi. All rights reserved.
 */

public class InitiatePaymentRequest {

    @SerializedName("curr")
    private String currency;

    @SerializedName("amount")
    private String amount;

    @SerializedName("unique_ref_id")
    private String uniqueReferenceId;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUniqueReferenceId() {
        return uniqueReferenceId;
    }

    public void setUniqueReferenceId(String uniqueReferenceId) {
        this.uniqueReferenceId = uniqueReferenceId;
    }
}

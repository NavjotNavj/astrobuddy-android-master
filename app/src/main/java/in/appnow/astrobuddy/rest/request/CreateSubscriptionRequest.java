package in.appnow.astrobuddy.rest.request;

import com.google.gson.annotations.SerializedName;

import in.appnow.astrobuddy.rest.response.BaseResponseModel;

/**
 * Created by sonu on 12:22, 10/09/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class CreateSubscriptionRequest extends BaseRequestModel {
    @SerializedName("plan_id")
    private String planId;
    @SerializedName("promo_id")
    private String promoId;

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getPromoId() {
        return promoId;
    }

    public void setPromoId(String promoId) {
        this.promoId = promoId;
    }
}

package in.appnow.astrobuddy.rest.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 12:23, 10/09/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class CreateSubscriptionResponse  extends BaseResponseModel{
    @SerializedName("pg_sub_trans_created")
    private boolean subscriptionCreate;
    @SerializedName("pg_sub_id")
    private String subscriptionId;
    @SerializedName("ab_sub_trans_id")
    private String subscriptionTransId;

    public boolean isSubscriptionCreate() {
        return subscriptionCreate;
    }

    public void setSubscriptionCreate(boolean subscriptionCreate) {
        this.subscriptionCreate = subscriptionCreate;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getSubscriptionTransId() {
        return subscriptionTransId;
    }

    public void setSubscriptionTransId(String subscriptionTransId) {
        this.subscriptionTransId = subscriptionTransId;
    }
}

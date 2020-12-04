package in.appnow.astrobuddy.rest.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 18:08, 07/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ProcessTransactionResponse extends BaseResponseModel {
    @SerializedName("user_exsist")
    private boolean isUserExist;
    @SerializedName("transaction_id")
    private String transactionId;
    @SerializedName("trasaction_created")
    private boolean transactionCreated;

    public boolean isTransactionCreated() {
        return transactionCreated;
    }

    public boolean isUserExist() {
        return isUserExist;
    }

    public String getTransactionId() {
        return transactionId;
    }
}

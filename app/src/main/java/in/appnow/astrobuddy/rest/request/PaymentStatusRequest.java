package in.appnow.astrobuddy.rest.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 17:33, 05/07/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class PaymentStatusRequest extends BaseRequestModel{
    @SerializedName("trans_id")
    private String orderId;
    @SerializedName("status")
    private String status;
    @SerializedName("pg_trans_id")
    private String pgTransId;
    @SerializedName("amount")
    private String amount;
    @SerializedName("pg_error_code")
    private int code;
    @SerializedName("signature")
    private String signature;

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPgTransId(String pgTransId) {
        this.pgTransId = pgTransId;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}

package in.appnow.astrobuddy.rest.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 12:22, 10/07/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class TransactionReportRequest extends BaseRequestModel {
    @SerializedName("trans_id")
    private String transId;
    @SerializedName("comments")
    private String comments;

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}

package in.appnow.astrobuddy.rest.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sonu on 14:51, 07/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class TransactionHistoryResponse extends BaseResponseModel {

    @SerializedName("response")
    private List<TransactionHistory> transactionHistoryList;


    public List<TransactionHistory> getTransactionHistoryList() {
        return transactionHistoryList;
    }

    public class TransactionHistory {
        @SerializedName("id")
        private String orderId;
        @SerializedName("init_time")
        private String transDate;
        @SerializedName("status")
        private String transStatus;
        @SerializedName("topics")
        private int topicsCount;
        @SerializedName("amount")
        private double amount;
        @SerializedName("curr")
        private String currency;
        @SerializedName("comments")
        private String comments = "Testing comments";
        @SerializedName("trans_type")
        private String transType;
        @SerializedName("trans_action")
        private String transAction;

        public String getOrderId() {
            return orderId;
        }

        public String getTransDate() {
            return transDate;
        }

        public String getTransStatus() {
            return transStatus;
        }

        public int getTopicsCount() {
            return topicsCount;
        }

        public double getAmount() {
            return amount;
        }

        public String getCurrency() {
            return currency;
        }

        public String getComments() {
            return comments;
        }

        public String getTransType() {
            return transType;
        }

        public String getTransAction() {
            return transAction;
        }
    }

}

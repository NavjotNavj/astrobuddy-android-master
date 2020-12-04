package in.appnow.astrobuddy.rest.response;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sonu on 12:07, 06/09/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class PlanResponse extends BaseResponseModel {
    @SerializedName("curr_on_basic_plan")
    private boolean basicPlanAvailable;
    @SerializedName("curr_plan_id")
    private String currentPlanId;
    @SerializedName("curr_plan_start")
    private String currentPlanStartDate;//format : 2018-09-05 00:00:00
    @SerializedName("curr_plan_name")
    private String currentPlanName;
    @SerializedName("curr_plan_description")
    private String currentPlanDescription;
    @SerializedName("curr_plan_validity")
    private String currentPlanValidity;
    @SerializedName("curr_plan_topics")
    private String currentPlanTopics;
    @SerializedName("sub_plans_count")
    private int numberOfPlans;
    @SerializedName("sub_plans")
    private List<Plans> plansList;

    public String getCurrentPlanStartDate() {
        return currentPlanStartDate;
    }

    public void setCurrentPlanStartDate(String currentPlanStartDate) {
        this.currentPlanStartDate = currentPlanStartDate;
    }

    public int getNumberOfPlans() {
        return numberOfPlans;
    }

    public void setNumberOfPlans(int numberOfPlans) {
        this.numberOfPlans = numberOfPlans;
    }

    public List<Plans> getPlansList() {
        return plansList;
    }

    public void setPlansList(List<Plans> plansList) {
        this.plansList = plansList;
    }

    public boolean isBasicPlanAvailable() {
        return basicPlanAvailable;
    }

    public void setBasicPlanAvailable(boolean basicPlanAvailable) {
        this.basicPlanAvailable = basicPlanAvailable;
    }

    public String getCurrentPlanId() {
        return currentPlanId;
    }

    public void setCurrentPlanId(String currentPlanId) {
        this.currentPlanId = currentPlanId;
    }

    public String getCurrentPlanName() {
        return currentPlanName;
    }

    public void setCurrentPlanName(String currentPlanName) {
        this.currentPlanName = currentPlanName;
    }

    public String getCurrentPlanDescription() {
        return currentPlanDescription;
    }

    public void setCurrentPlanDescription(String currentPlanDescription) {
        this.currentPlanDescription = currentPlanDescription;
    }

    public String getCurrentPlanValidity() {
        return currentPlanValidity;
    }

    public void setCurrentPlanValidity(String currentPlanValidity) {
        this.currentPlanValidity = currentPlanValidity;
    }

    public String getCurrentPlanTopics() {
        return currentPlanTopics;
    }

    public void setCurrentPlanTopics(String currentPlanTopics) {
        this.currentPlanTopics = currentPlanTopics;
    }

    public class Plans {
        @SerializedName("id")
        private String planId;
        @SerializedName("name")
        private String planName;
        @SerializedName("description")
        private String planDescription;
        @SerializedName("validity")
        private String validity;
        @SerializedName("topics")
        private int topics;
        @SerializedName("curr")
        private String currency;
        @SerializedName("amount")
        private Double amount;
        @SerializedName("amount_inr")
        private Double amountINR;

        @Nullable
        private String currentPlanStartDate;

        public String getPlanId() {
            return planId;
        }

        public void setPlanId(String planId) {
            this.planId = planId;
        }

        public String getPlanName() {
            return planName;
        }

        public void setPlanName(String planName) {
            this.planName = planName;
        }

        public String getPlanDescription() {
            return planDescription;
        }

        public void setPlanDescription(String planDescription) {
            this.planDescription = planDescription;
        }

        public String getValidity() {
            return validity;
        }

        public void setValidity(String validity) {
            this.validity = validity;
        }

        public int getTopics() {
            return topics;
        }

        public void setTopics(int topics) {
            this.topics = topics;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        public Double getAmountINR() {
            return amountINR;
        }

        public void setAmountINR(Double amountINR) {
            this.amountINR = amountINR;
        }

        @Nullable
        public String getCurrentPlanStartDate() {
            return currentPlanStartDate;
        }

        public void setCurrentPlanStartDate(@Nullable String currentPlanStartDate) {
            this.currentPlanStartDate = currentPlanStartDate;
        }

        @Override
        public String toString() {
            return "Plans{" +
                    "planId='" + planId + '\'' +
                    ", planName='" + planName + '\'' +
                    ", planDescription='" + planDescription + '\'' +
                    ", validity='" + validity + '\'' +
                    ", topics=" + topics +
                    ", currency='" + currency + '\'' +
                    ", amount=" + amount +
                    ", amountINR=" + amountINR +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "PlanResponse{" +
                "basicPlanAvailable=" + basicPlanAvailable +
                ", currentPlanId='" + currentPlanId + '\'' +
                ", currentPlanName='" + currentPlanName + '\'' +
                ", currentPlanDescription='" + currentPlanDescription + '\'' +
                ", currentPlanValidity='" + currentPlanValidity + '\'' +
                ", currentPlanTopics='" + currentPlanTopics + '\'' +
                ", numberOfPlans=" + numberOfPlans +
                ", plansList=" + plansList +
                '}';
    }
}

package in.appnow.astrobuddy.rest.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sonu on 12:21, 04/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class SubscriptionPlanResponse extends BaseResponseModel {
    @SerializedName("user_exist")
    private boolean isUserExist;
    @SerializedName("currentPlans")
    private CurrentPlan currentPlan;
    @SerializedName("curr")
    private String currency;
    @SerializedName("remainingPlans")
    private List<SubscriptionPlans> subscriptionPlansList;

    public CurrentPlan getCurrentPlan() {
        return currentPlan;
    }

    public String getCurrency() {
        return currency;
    }

    public boolean isUserExist() {
        return isUserExist;
    }

    public List<SubscriptionPlans> getSubscriptionPlansList() {
        return subscriptionPlansList;
    }

    public class SubscriptionPlans {
        @SerializedName("id")
        private int subscriptionPlanId;
        @SerializedName("name")
        private String planName;
        @SerializedName("description")
        private String description;
        @SerializedName("days")
        private int days;
        @SerializedName("topic")
        private int topicCount;
        @SerializedName("amount")
        private String amount;


        public int getSubscriptionPlanId() {
            return subscriptionPlanId;
        }

        public String getPlanName() {
            return planName;
        }

        public String getDescription() {
            return description;
        }

        public int getDays() {
            return days;
        }

        public int getTopicCount() {
            return topicCount;
        }

        public String getAmount() {
            return amount;
        }

        @Override
        public String toString() {
            return "SubscriptionPlans{" +
                    "subscriptionPlanId=" + subscriptionPlanId +
                    ", planName='" + planName + '\'' +
                    ", description='" + description + '\'' +
                    ", days=" + days +
                    ", topicCount=" + topicCount +
                    ", amount=" + amount +
                    '}';
        }
    }

    public class CurrentPlan {
        @SerializedName("name")
        private String planName;
        @SerializedName("start_date")
        private String startDate;
        @SerializedName("end_date")
        private String expiryDate;
        @SerializedName("plan_id")
        private String planId;
        @SerializedName("topic")
        private int topicCount;
        @SerializedName("description")
        private String description;

        public String getPlanName() {
            return planName;
        }

        public String getStartDate() {
            return startDate;
        }

        public String getExpiryDate() {
            return expiryDate;
        }

        public String getPlanId() {
            return planId;
        }

        public int getTopicCount() {
            return topicCount;
        }

        public String getDescription() {
            return description;
        }
    }

}

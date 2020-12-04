package in.appnow.astrobuddy.rest.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sonu on 12:03, 04/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class PromoCodeResponse extends BaseResponseModel {

    @SerializedName("user_exsist")
    private boolean isUserExist;
    @SerializedName("promo_details")
    private PromoCodeDetails promoCodeDetails;

    public boolean isUserExist() {
        return isUserExist;
    }

    public PromoCodeDetails getPromoCodeDetails() {
        return promoCodeDetails;
    }

    public class PromoCodeDetails {
        @SerializedName("promo_count")
        private int promoCount;

        @SerializedName("promo_codes")
        private List<PromoCodes> promoCodes;

        public int getPromoCount() {
            return promoCount;
        }

        public List<PromoCodes> getPromoCodes() {
            return promoCodes;
        }
    }

    public class PromoCodes {
        @SerializedName("id")
        private int promoId;
        @SerializedName("code")
        private String promoCode;
        @SerializedName("purpose")
        private String purpose;
        @SerializedName("create_date")
        private String createdDate;
        @SerializedName("expiry_date")
        private String expiryDate;
        @SerializedName("promo_type_id")
        private String promoTypeId;
        @SerializedName("promo_value")
        private String promoValue;
        @SerializedName("description")
        private String description;

        public int getPromoId() {
            return promoId;
        }

        public String getPromoCode() {
            return promoCode;
        }

        public String getPurpose() {
            return purpose;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public String getExpiryDate() {
            return expiryDate;
        }

        public String getPromoTypeId() {
            return promoTypeId;
        }

        public String getPromoValue() {
            return promoValue;
        }

        public String getDescription() {
            return description;
        }
    }
}

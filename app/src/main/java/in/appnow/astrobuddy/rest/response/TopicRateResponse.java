package in.appnow.astrobuddy.rest.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 11:28, 16/07/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class TopicRateResponse extends BaseResponseModel {
    @SerializedName("price_index")
    private String priceIndex;
    @SerializedName("price")
    private String price;
    @SerializedName("currency")
    private String currency;
    @SerializedName("USD_TO_INR")
    private String usdToInr;
    @SerializedName("GBP_TO_INR")
    private String gbpToInr;

    public String getPriceIndex() {
        return priceIndex;
    }

    public String getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public String getUsdToInr() {
        return usdToInr;
    }

    public String getGbpToInr() {
        return gbpToInr;
    }
}

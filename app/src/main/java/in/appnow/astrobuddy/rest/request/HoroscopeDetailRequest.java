package in.appnow.astrobuddy.rest.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 17:14, 25/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class HoroscopeDetailRequest {
    @SerializedName("star_sign_id")
    private int starSignId;
    @SerializedName("forecast_date")
    private String forecastDate;

    public HoroscopeDetailRequest(int starSignId, String forecastDate) {
        this.starSignId = starSignId;
        this.forecastDate = forecastDate;
    }

    public int getStarSignId() {
        return starSignId;
    }

    public String getForecastDate() {
        return forecastDate;
    }
}

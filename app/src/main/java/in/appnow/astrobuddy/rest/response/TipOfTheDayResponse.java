package in.appnow.astrobuddy.rest.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 18:57, 05/06/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class TipOfTheDayResponse extends BaseResponseModel {
    @SerializedName("tip_of_day")
    private String tipOfTheDay;

    public String getTipOfTheDay() {
        return tipOfTheDay;
    }
}

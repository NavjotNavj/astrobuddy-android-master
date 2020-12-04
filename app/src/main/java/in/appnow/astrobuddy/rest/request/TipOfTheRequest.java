package in.appnow.astrobuddy.rest.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 18:27, 05/06/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class TipOfTheRequest {
    @SerializedName("star_sign")
    private String starSign;
    @SerializedName("user_date")
    private String currentDate;

    public TipOfTheRequest() {
    }

    public void setStarSign(String starSign) {
        this.starSign = starSign;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }
}

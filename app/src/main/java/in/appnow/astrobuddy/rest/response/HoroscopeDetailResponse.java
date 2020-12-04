package in.appnow.astrobuddy.rest.response;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Created by sonu on 17:13, 25/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class HoroscopeDetailResponse extends BaseResponseModel {

    @SerializedName("horoscope_detail")
    public HashMap<String, String> horoscopeDetailHashMap;

    public HashMap<String, String> getHoroscopeDetailHashMap() {
        return horoscopeDetailHashMap;
    }

    public void setHoroscopeDetailHashMap(HashMap<String, String> horoscopeDetailHashMap) {
        this.horoscopeDetailHashMap = horoscopeDetailHashMap;
    }
}

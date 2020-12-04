package in.appnow.astrobuddy.rest.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 14:40, 16/07/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class AppVersionResponse extends BaseResponseModel {
    @SerializedName("force_upgrade")
    private boolean forceUpgrade;
    @SerializedName("recommend_upgrade")
    private boolean recommendUpgrade;

    public boolean isForceUpgrade() {
        return forceUpgrade;
    }

    public boolean isRecommendUpgrade() {
        return recommendUpgrade;
    }
}

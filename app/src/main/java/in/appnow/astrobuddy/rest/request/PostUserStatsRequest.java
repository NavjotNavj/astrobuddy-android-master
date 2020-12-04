package in.appnow.astrobuddy.rest.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import in.appnow.astrobuddy.models.BannerStatsModel;
import in.appnow.astrobuddy.models.ScreenStatsModel;

/**
 * Created by sonu on 18:16, 15/04/19
 * Copyright (c) 2019 . All rights reserved.
 */
public class PostUserStatsRequest {
    @SerializedName("user_id")
    private String userId;
    @SerializedName("stat_date")
    private String statDate;
    @SerializedName("app_version")
    private String appVersion;
    @SerializedName("source_id")
    private int sourceId;
    @SerializedName("screen_stat")
    private List<ScreenStatsModel> screenStatsModelList;
    @SerializedName("post_stat")
    private List<BannerStatsModel> bannerStatsModelList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatDate() {
        return statDate;
    }

    public void setStatDate(String statDate) {
        this.statDate = statDate;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public List<ScreenStatsModel> getScreenStatsModelList() {
        return screenStatsModelList;
    }

    public void setScreenStatsModelList(List<ScreenStatsModel> screenStatsModelList) {
        this.screenStatsModelList = screenStatsModelList;
    }

    public List<BannerStatsModel> getBannerStatsModelList() {
        return bannerStatsModelList;
    }

    public void setBannerStatsModelList(List<BannerStatsModel> bannerStatsModelList) {
        this.bannerStatsModelList = bannerStatsModelList;
    }



    @Override
    public String toString() {
        return "PostUserStatsRequest{" +
                "userId='" + userId + '\'' +
                ", statDate='" + statDate + '\'' +
                ", sourceId=" + sourceId +
                ", screenStatsModelList=" + screenStatsModelList +
                ", bannerStatsModelList=" + bannerStatsModelList +
                '}';
    }
}

package in.appnow.astrobuddy.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 17:04, 12/04/19
 * Copyright (c) 2019 . All rights reserved.
 */
@Entity(tableName = "ScreenStats")
public class ScreenStatsModel {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private transient long id;

    @ColumnInfo(name = "screenName")
    @SerializedName("name")
    private String screenName;

    @ColumnInfo(name = "clickCount")
    @SerializedName("count")
    private int clickCount;

    @ColumnInfo(name = "stayedTime")
    @SerializedName("stay_time")
    private int stayedTime;

    @ColumnInfo(name = "timeStamp")
    @SerializedName("timeStamp")
    private long timeStamp;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public int getStayedTime() {
        return stayedTime;
    }

    public void setStayedTime(int stayedTime) {
        this.stayedTime = stayedTime;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "ScreenStatsModel{" +
                "id=" + id +
                ", screenName='" + screenName + '\'' +
                ", clickCount=" + clickCount +
                ", stayedTime=" + stayedTime +
                ", timeStamp=" + timeStamp +
                '}';
    }
}

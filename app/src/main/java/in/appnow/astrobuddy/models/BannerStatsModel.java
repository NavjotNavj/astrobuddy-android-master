package in.appnow.astrobuddy.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 17:03, 12/04/19
 * Copyright (c) 2019 . All rights reserved.
 */
@Entity(tableName = "BannerStats")
public class BannerStatsModel {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private transient long id;

    @ColumnInfo(name = "postId")
    @SerializedName("post_id")
    private int postId;

    @ColumnInfo(name = "postType")
    @SerializedName("type")
    private String postType;

    @ColumnInfo(name = "impressionsCount")
    @SerializedName("impressions")
    private int impressionsCount;

    @ColumnInfo(name = "clickCount")
    @SerializedName("count")
    private int clickCount;

    @ColumnInfo(name = "timeStamp")
    @SerializedName("timeStamp")
    private long timeStamp;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getImpressionsCount() {
        return impressionsCount;
    }

    public void setImpressionsCount(int impressionsCount) {
        this.impressionsCount = impressionsCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    @Override
    public String toString() {
        return "BannerStatsModel{" +
                "id=" + id +
                ", postId=" + postId +
                ", postType='" + postType + '\'' +
                ", impressionsCount=" + impressionsCount +
                ", clickCount=" + clickCount +
                ", timeStamp=" + timeStamp +
                '}';
    }
}

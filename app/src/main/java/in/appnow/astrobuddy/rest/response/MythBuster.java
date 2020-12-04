package in.appnow.astrobuddy.rest.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 15:48, 31/10/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class MythBuster implements Parcelable {

    @SerializedName("id")
    private long mythId;
    @SerializedName("source")
    private String source;
    @SerializedName("title")
    private String title;
    @SerializedName("desc")
    private String description;
    @SerializedName("pub_date")
    private String date;
    @SerializedName("type")
    private String mythType;

    protected MythBuster(Parcel in) {
        mythId = in.readLong();
        source = in.readString();
        title = in.readString();
        description = in.readString();
        date = in.readString();
        mythType = in.readString();
    }

    public static final Creator<MythBuster> CREATOR = new Creator<MythBuster>() {
        @Override
        public MythBuster createFromParcel(Parcel in) {
            return new MythBuster(in);
        }

        @Override
        public MythBuster[] newArray(int size) {
            return new MythBuster[size];
        }
    };

    public long getMythId() {
        return mythId;
    }

    public void setMythId(long mythId) {
        this.mythId = mythId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMythType() {
        return mythType;
    }

    public void setMythType(String mythType) {
        this.mythType = mythType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(mythId);
        parcel.writeString(source);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(date);
        parcel.writeString(mythType);
    }

    @Override
    public String toString() {
        return "MythBuster{" +
                "mythId=" + mythId +
                ", source='" + source + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", mythType='" + mythType + '\'' +
                '}';
    }
}

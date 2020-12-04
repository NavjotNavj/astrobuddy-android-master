package in.appnow.astrobuddy.rest.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhishek Thanvi on 2019-09-05.
 * Copyright © 2019 Abhishek Thanvi. All rights reserved.
 */

public class ConversationList implements Parcelable {

    @SerializedName("chat_id")
    private String conversationId;
    @SerializedName("init_time")
    private String startTimestamp;
    @SerializedName("end_time")
    private String endTimestamp;
    @SerializedName("fdbk_rating")
    private int feedbackRating;
    @SerializedName("end_status")
    private int chatStatus;


    protected ConversationList(Parcel in) {
        conversationId = in.readString();
        startTimestamp = in.readString();
        endTimestamp = in.readString();
        feedbackRating = in.readInt();
        chatStatus = in.readInt();
    }

    public static final Creator<ConversationList> CREATOR = new Creator<ConversationList>() {
        @Override
        public ConversationList createFromParcel(Parcel in) {
            return new ConversationList(in);
        }

        @Override
        public ConversationList[] newArray(int size) {
            return new ConversationList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(conversationId);
        parcel.writeString(startTimestamp);
        parcel.writeString(endTimestamp);
        parcel.writeInt(feedbackRating);
        parcel.writeInt(chatStatus);
    }

    public String getConversationId() {
        return conversationId;
    }

    public String getStartTimestamp() {
        return startTimestamp;
    }

    public String getEndTimestamp() {
        return endTimestamp;
    }

    public int getFeedbackRating() {
        return feedbackRating;
    }

    public int getChatStatus() {
        return chatStatus;
    }
}

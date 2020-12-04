package in.appnow.astrobuddy.rest.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sonu on 13:01, 06/08/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class Conversations implements Parcelable {
    @SerializedName("id")
    private String conversationId;
    @SerializedName("session_id")
    private String sessionId;
    @SerializedName("start_timestamp")
    private String startTimestamp;
    @SerializedName("end_timestamp")
    private String endTimestamp;
    @SerializedName("topics")
    private int topicsCount;
    @SerializedName("fdbk_rating")
    private float feedbackRating;
    @SerializedName("fdbk_comment")
    private String feedbackComment;
    @SerializedName("message_count")
    private int messageCount;
    @SerializedName("messages")
    private List<Messages> messagesList;


    protected Conversations(Parcel in) {
        conversationId = in.readString();
        sessionId = in.readString();
        startTimestamp = in.readString();
        endTimestamp = in.readString();
        topicsCount = in.readInt();
        feedbackRating = in.readFloat();
        feedbackComment = in.readString();
        messageCount = in.readInt();
        messagesList = in.createTypedArrayList(Messages.CREATOR);
    }

    public static final Creator<Conversations> CREATOR = new Creator<Conversations>() {
        @Override
        public Conversations createFromParcel(Parcel in) {
            return new Conversations(in);
        }

        @Override
        public Conversations[] newArray(int size) {
            return new Conversations[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public String getConversationId() {
        return conversationId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getStartTimestamp() {
        return startTimestamp;
    }

    public String getEndTimestamp() {
        return endTimestamp;
    }

    public int getTopicsCount() {
        return topicsCount;
    }

    public float getFeedbackRating() {
        return feedbackRating;
    }

    public String getFeedbackComment() {
        return feedbackComment;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public List<Messages> getMessagesList() {
        return messagesList;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(conversationId);
        parcel.writeString(sessionId);
        parcel.writeString(startTimestamp);
        parcel.writeString(endTimestamp);
        parcel.writeInt(topicsCount);
        parcel.writeFloat(feedbackRating);
        parcel.writeString(feedbackComment);
        parcel.writeInt(messageCount);
        parcel.writeTypedList(messagesList);
    }
}



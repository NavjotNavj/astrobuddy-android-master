package in.appnow.astrobuddy.rest.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 14:52, 31/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class Messages implements Parcelable {
    @SerializedName("id")
    private long messageId;
    @SerializedName("message")
    private String message;
    @SerializedName("timestamp")
    private long timestamp;
    @SerializedName("from_user_id")
    private String fromUserId;
    @SerializedName("to_user_id")
    private String toUserId;
    @SerializedName("msg_type")
    private String msgType;

    public Messages() {
    }

    protected Messages(Parcel in) {
        messageId = in.readLong();
        message = in.readString();
        timestamp = in.readLong();
        fromUserId = in.readString();
        toUserId = in.readString();
        msgType = in.readString();
    }

    public static final Creator<Messages> CREATOR = new Creator<Messages>() {
        @Override
        public Messages createFromParcel(Parcel in) {
            return new Messages(in);
        }

        @Override
        public Messages[] newArray(int size) {
            return new Messages[size];
        }
    };

    public long getMessageId() {
        return messageId;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public String getMsgType() {
        return msgType;
    }

    @Override
    public String toString() {
        return "Messages{" +
                "messageId=" + messageId +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                ", fromUserId=" + fromUserId +
                ", toUserId=" + toUserId +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(messageId);
        parcel.writeString(message);
        parcel.writeLong(timestamp);
        parcel.writeString(fromUserId);
        parcel.writeString(toUserId);
        parcel.writeString(msgType);
    }
}

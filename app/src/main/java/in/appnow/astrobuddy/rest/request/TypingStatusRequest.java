package in.appnow.astrobuddy.rest.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 15:13, 05/06/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class TypingStatusRequest extends BaseRequestModel implements Parcelable {
    @SerializedName("handler_id")
    private String handlerId;
    @SerializedName("chat_session_id")
    private String chatSessionId;
    @SerializedName("typing_status")
    private int typingStatus;

    public TypingStatusRequest() {
    }

    protected TypingStatusRequest(Parcel in) {
        handlerId = in.readString();
        chatSessionId = in.readString();
        typingStatus = in.readInt();
    }

    public static final Creator<TypingStatusRequest> CREATOR = new Creator<TypingStatusRequest>() {
        @Override
        public TypingStatusRequest createFromParcel(Parcel in) {
            return new TypingStatusRequest(in);
        }

        @Override
        public TypingStatusRequest[] newArray(int size) {
            return new TypingStatusRequest[size];
        }
    };

    public void setHandlerId(String handlerId) {
        this.handlerId = handlerId;
    }

    public void setChatSessionId(String chatSessionId) {
        this.chatSessionId = chatSessionId;
    }

    public void setTypingStatus(int typingStatus) {
        this.typingStatus = typingStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(handlerId);
        parcel.writeString(chatSessionId);
        parcel.writeInt(typingStatus);
    }
}

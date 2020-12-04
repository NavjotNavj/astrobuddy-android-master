package in.appnow.astrobuddy.conversation_module.rest_service.models.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import in.appnow.astrobuddy.rest.request.BaseRequestModel;


/**
 * Created by sonu on 16:07, 04/06/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class UpdateMessageRequest extends BaseRequestModel implements Parcelable {
    @SerializedName("message_ids")
    private List<Long> messageIds = new ArrayList<>();
    @SerializedName("status")
    private String messageStatus;
    @SerializedName("handler_id")
    private String handlerId;
    @SerializedName("chat_session_id")
    private String sessionId;

    public UpdateMessageRequest() {
    }


    protected UpdateMessageRequest(Parcel in) {
        messageStatus = in.readString();
        handlerId = in.readString();
        sessionId = in.readString();
        in.readList(messageIds, null);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(messageStatus);
        dest.writeString(handlerId);
        dest.writeString(sessionId);
        dest.writeList(messageIds);
    }

    public static final Creator<UpdateMessageRequest> CREATOR = new Creator<UpdateMessageRequest>() {
        @Override
        public UpdateMessageRequest createFromParcel(Parcel in) {
            return new UpdateMessageRequest(in);
        }

        @Override
        public UpdateMessageRequest[] newArray(int size) {
            return new UpdateMessageRequest[size];
        }
    };

    public void setMessageIds(List<Long> messageIds) {
        this.messageIds = messageIds;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public void setHandlerId(String handlerId) {
        this.handlerId = handlerId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<Long> getMessageIds() {
        return messageIds;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public String getHandlerId() {
        return handlerId;
    }

    public String getSessionId() {
        return sessionId;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public String toString() {
        return "UpdateMessageRequest{" +
                "messageIds=" + messageIds +
                ", messageStatus='" + messageStatus + '\'' +
                ", handlerId='" + handlerId + '\'' +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }
}

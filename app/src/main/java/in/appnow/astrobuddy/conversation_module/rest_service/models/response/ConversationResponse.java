package in.appnow.astrobuddy.conversation_module.rest_service.models.response;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import in.appnow.astrobuddy.rest.response.BaseResponseModel;


/**
 * Created by sonu on 18/12/17.
 */

@Entity(tableName = "Chat",indices = {@Index(value =
        {"sendDateTime"}, unique = true)})
public class ConversationResponse extends BaseResponseModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "msgTempId")
    @SerializedName("msg_tmp_id")
    private long messageTemporaryId;

    @ColumnInfo(name = "msgId")
    @SerializedName("id")
    private long messageId;

    @SerializedName("msg")
    private String message;

    @SerializedName("sentBy")
    private String senderId;

    @SerializedName("status")
    private String messageStatus;

    @SerializedName("timestamp")
    private long sendDateTime;

    @SerializedName("msg_type")
    private String messageType;

    @SerializedName("chat_id")
    private String sessionId;

    public ConversationResponse() {
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public long getMessageTemporaryId() {
        return messageTemporaryId;
    }

    public void setMessageTemporaryId(long messageTemporaryId) {
        this.messageTemporaryId = messageTemporaryId;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public long getSendDateTime() {
        return sendDateTime;
    }

    public void setSendDateTime(long sendDateTime) {
        this.sendDateTime = sendDateTime;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "ConversationResponse{" +
                "messageTemporaryId=" + messageTemporaryId +
                ", messageId=" + messageId +
                ", message='" + message + '\'' +
                ", senderId='" + senderId + '\'' +
                ", messageStatus=" + messageStatus +
                ", sendDateTime=" + sendDateTime +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }

    protected ConversationResponse(Parcel in) {
        messageTemporaryId = in.readLong();
        messageId = in.readLong();
        message = in.readString();
        senderId = in.readString();
        messageStatus = in.readString();
        sendDateTime = in.readLong();
        sessionId = in.readString();
        messageType = in.readString();
    }

    public static final Creator<ConversationResponse> CREATOR = new Creator<ConversationResponse>() {
        @Override
        public ConversationResponse createFromParcel(Parcel in) {
            return new ConversationResponse(in);
        }

        @Override
        public ConversationResponse[] newArray(int size) {
            return new ConversationResponse[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(messageTemporaryId);
        parcel.writeLong(messageId);
        parcel.writeString(message);
        parcel.writeString(senderId);
        parcel.writeString(messageStatus);
        parcel.writeLong(sendDateTime);
        parcel.writeString(sessionId);
        parcel.writeString(messageType);
    }
}

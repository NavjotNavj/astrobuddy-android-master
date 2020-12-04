package in.appnow.astrobuddy.rest.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sonu on 13:18, 31/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ChatHistoryResponse extends BaseResponseModel {
    @SerializedName("conversation_count")
    private int conversationCount;
    @SerializedName("conversations")
    private List<Conversations> conversationsList;

    public int getConversationCount() {
        return conversationCount;
    }

    public List<Conversations> getConversationsList() {
        return conversationsList;
    }


}

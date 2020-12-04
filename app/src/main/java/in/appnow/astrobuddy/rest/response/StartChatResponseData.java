package in.appnow.astrobuddy.rest.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhishek Thanvi on 2019-09-03.
 * Copyright © 2019 Abhishek Thanvi. All rights reserved.
 */

public class StartChatResponseData {


    @SerializedName("existing_chat")
    private boolean existingChat;
    @SerializedName("chat_session_id")
    private String chatSessionId;
    @SerializedName("chat_status")
    private String chatStatus;
    @SerializedName("topic_available")
    private boolean topicAvailable;

    public StartChatResponseData() {
    }

    public boolean isExistingChat() {
        return existingChat;
    }

    public void setExistingChat(boolean existingChat) {
        this.existingChat = existingChat;
    }

    public String getChatSessionId() {
        return chatSessionId;
    }

    public void setChatSessionId(String chatSessionId) {
        this.chatSessionId = chatSessionId;
    }

    public String getChatStatus() {
        return chatStatus;
    }

    public void setChatStatus(String chatStatus) {
        this.chatStatus = chatStatus;
    }

    public boolean isTopicAvailable() {
        return topicAvailable;
    }

    public void setTopicAvailable(boolean topicAvailable) {
        this.topicAvailable = topicAvailable;
    }
}

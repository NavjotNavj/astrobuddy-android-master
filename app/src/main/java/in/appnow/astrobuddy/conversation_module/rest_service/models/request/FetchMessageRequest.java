package in.appnow.astrobuddy.conversation_module.rest_service.models.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 12:11, 15/06/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class FetchMessageRequest {
    @SerializedName("chat_id")
    private String sessionId;

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }
}

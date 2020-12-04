package in.appnow.astrobuddy.rest.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 16:38, 24/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ChatFeedbackRequest extends BaseRequestModel {
    @SerializedName("chat_id")
    private String chatSessionId;
    @SerializedName("rating")
    private int rating;
    @SerializedName("fdbk")
    private String comment;

    public ChatFeedbackRequest(String chatSessionId, int rating, String comment) {
        this.chatSessionId = chatSessionId;
        this.rating = rating;
        this.comment = comment;
    }
}

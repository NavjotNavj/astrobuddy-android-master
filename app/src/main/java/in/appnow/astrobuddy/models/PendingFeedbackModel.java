package in.appnow.astrobuddy.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 20:15, 27/06/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class PendingFeedbackModel {
    @SerializedName("fdbk_pending")
    private boolean isFeedbackPending;
    @SerializedName("chat_session_id")
    private String sessionId;
    @SerializedName("timestamp")
    private String startTimeStamp;
    @SerializedName("end_timestamp")
    private String endTimeStamp;

    public PendingFeedbackModel(boolean isFeedbackPending, String sessionId, String startTimeStamp, String endTimeStamp) {
        this.isFeedbackPending = isFeedbackPending;
        this.sessionId = sessionId;
        this.startTimeStamp = startTimeStamp;
        this.endTimeStamp = endTimeStamp;
    }

    public boolean isFeedbackPending() {
        return isFeedbackPending;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getStartTimeStamp() {
        return startTimeStamp;
    }

    public String getEndTimeStamp() {
        return endTimeStamp;
    }
}

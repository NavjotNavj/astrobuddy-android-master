package in.appnow.astrobuddy.rest.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by NILESH BHARODIYA on 09-09-2019.
 */
public class CallFeedbackRequest extends BaseRequestModel {
    @SerializedName("purchased_plan_id")
    private String planId;

    @SerializedName("rating")
    private String rating;

    @SerializedName("comments")
    private String comment;

    public CallFeedbackRequest(String chatSessionId, String rating, String comment) {
        this.planId = chatSessionId;
        this.rating = rating;
        this.comment = comment;
    }
}

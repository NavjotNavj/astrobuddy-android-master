package in.appnow.astrobuddy.rest.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sonu on 18:31, 10/04/19
 * Copyright (c) 2019 . All rights reserved.
 */
public class ClickMetricsRequest {
    @SerializedName("post_id")
    private int postId;

    public void setPostId(int postId) {
        this.postId = postId;
    }
}

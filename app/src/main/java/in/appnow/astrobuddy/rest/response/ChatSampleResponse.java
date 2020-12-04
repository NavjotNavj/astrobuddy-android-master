package in.appnow.astrobuddy.rest.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sonu on 16:04, 17/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ChatSampleResponse extends BaseResponseModel {
    @SerializedName("samples")
    private List<ChatSample> chatSampleList;

    public List<ChatSample> getChatSampleList() {
        return chatSampleList;
    }

    public class ChatSample {
        @SerializedName("sample_question")
        private String sampleQuestion;
        @SerializedName("topic")
        private String topicName;
        @SerializedName("topic_img")
        private String topicImage;

        public String getSampleQuestion() {
            return sampleQuestion;
        }

        public String getTopicName() {
            return topicName;
        }

        public String getTopicImage() {
            return topicImage;
        }
    }
}

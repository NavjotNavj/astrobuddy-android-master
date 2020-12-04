package in.appnow.astrobuddy.conversation_module.rest_service.models.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import in.appnow.astrobuddy.rest.response.BaseResponseModel;


/**
 * Created by sonu on 19/12/17.
 */

public class FetchAllMessageResponse extends BaseResponseModel {

    @SerializedName("response")
    private List<ConversationResponse> conversationModelList;

    public List<ConversationResponse> getConversationModelList() {
        return conversationModelList;
    }

    public void setConversationModelList(List<ConversationResponse> conversationModelList) {
        this.conversationModelList = conversationModelList;
    }

    @Override
    public String toString() {
        return "FetchAllMessageResponse{" +
                ", conversationModelList=" + conversationModelList +
                '}';
    }
}

package in.appnow.astrobuddy.dao.repository.conversation;


import in.appnow.astrobuddy.conversation_module.rest_service.models.request.FetchMessageRequest;
import in.appnow.astrobuddy.conversation_module.rest_service.models.response.FetchAllMessageResponse;
import io.reactivex.Observable;

public interface ConversationRemoteRepository {
   Observable<FetchAllMessageResponse> fetchAllConversation(FetchMessageRequest requestModel);
}

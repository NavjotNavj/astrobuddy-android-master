package in.appnow.astrobuddy.dao.repository.conversation;

import javax.inject.Inject;


import in.appnow.astrobuddy.conversation_module.rest_service.models.request.FetchMessageRequest;
import in.appnow.astrobuddy.conversation_module.rest_service.models.response.FetchAllMessageResponse;
import in.appnow.astrobuddy.rest.APIInterface;
import io.reactivex.Observable;


public class ConversationRemoteDataSource implements ConversationRemoteRepository {

    private final APIInterface apiInterface;

    @Inject
    public ConversationRemoteDataSource(APIInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    @Override
    public Observable<FetchAllMessageResponse> fetchAllConversation(FetchMessageRequest requestModel) {
        return apiInterface.getAllMessagesBySessionId(requestModel);
    }
}

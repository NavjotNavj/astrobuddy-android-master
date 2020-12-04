package in.appnow.astrobuddy.dao.repository.conversation;



import androidx.lifecycle.LiveData;

import java.util.List;

import in.appnow.astrobuddy.conversation_module.rest_service.models.response.ConversationResponse;
import in.appnow.astrobuddy.dao.repository.BaseRepository;

public interface ConversationLocalRepository extends BaseRepository<ConversationResponse> {
    LiveData<List<ConversationResponse>> fetchAllConversation(String sessionId);

    ConversationResponse fetchSingleMessage(long messageId);

    ConversationResponse fetchSingleMessageForTempId(long msgTempId);

    int updateMessageStatus(String status, long msgId);

    void deleteChatTable();
}

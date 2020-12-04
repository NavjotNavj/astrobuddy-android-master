package in.appnow.astrobuddy.dao.repository.conversation;


import androidx.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

import in.appnow.astrobuddy.conversation_module.rest_service.models.response.ConversationResponse;
import in.appnow.astrobuddy.dao.ConversationDao;


public class ConversationLocalDataSource implements ConversationLocalRepository {

    private final ConversationDao conversationDao;

    @Inject
    public ConversationLocalDataSource(ConversationDao conversationDao) {
        this.conversationDao = conversationDao;
    }

    @Override
    public LiveData<List<ConversationResponse>> fetchAllConversation(String sessionId) {
        return conversationDao.fetchAllConversation(sessionId);
    }

    @Override
    public ConversationResponse fetchSingleMessage(long messageId) {
        return conversationDao.fetchSingleMessage(messageId);
    }

    @Override
    public ConversationResponse fetchSingleMessageForTempId(long msgTempId) {
        return conversationDao.fetchSingleMessageForTempId(msgTempId);
    }

    @Override
    public int updateMessageStatus(String status, long msgId) {
        return conversationDao.updateMessageStatus(status, msgId);
    }

    @Override
    public void deleteChatTable() {

    }

    @Override
    public long insert(ConversationResponse obj) {
        return conversationDao.insert(obj);
    }

    @Override
    public long[] insert(List<ConversationResponse> list) {
        return conversationDao.insert(list);
    }

    @Override
    public int update(ConversationResponse obj) {
        return conversationDao.update(obj);
    }

    @Override
    public int delete(ConversationResponse obj) {
        return conversationDao.delete(obj);
    }

    @Override
    public int delete(List<ConversationResponse> list) {
        return conversationDao.delete(list);
    }
}

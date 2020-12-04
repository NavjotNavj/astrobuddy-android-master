package in.appnow.astrobuddy.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import in.appnow.astrobuddy.conversation_module.rest_service.models.response.ConversationResponse;


@Dao
public interface ConversationDao extends BaseDao<ConversationResponse> {

    @Query("SELECT * FROM Chat WHERE sessionId =:sessionId ORDER BY datetime (sendDateTime) ASC")
    LiveData<List<ConversationResponse>> fetchAllConversation(String sessionId);

    @Query("SELECT * FROM Chat ORDER BY datetime (sendDateTime) ASC")
    List<ConversationResponse> fetchAllConversations();

    @Query("SELECT * FROM Chat WHERE msgId = :messageId LIMIT 1")
    ConversationResponse fetchSingleMessage(long messageId);

    @Query("SELECT * FROM Chat WHERE msgTempId = :msgTempId LIMIT 1")
    ConversationResponse fetchSingleMessageForTempId(long msgTempId);

    @Query("UPDATE Chat SET messageStatus= :status WHERE msgTempId = :msgTempId")
    int updateMessageStatus(String status, long msgTempId);

    @Query("UPDATE Chat SET messageStatus= :status WHERE msgId = :msgId")
    int updateChatMessageStatus(String status, long msgId);

    @Query("UPDATE Chat SET messageStatus= :status, msgId= :messageId WHERE sendDateTime = :timeStamp")
    int updateMessageStatusForTimeStamp(String status, long timeStamp, long messageId);

    @Query("UPDATE Chat SET msgId= :msgId WHERE msgTempId = :tempMsgId")
    int updateMessageId(long tempMsgId, long msgId);

    @Query("DELETE FROM Chat")
    void deleteChatTable();

}

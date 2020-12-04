package in.appnow.astrobuddy.rest.response;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class CallHistoryResponse extends BaseResponseModel{


	@SerializedName("call_count")
	private int callCount;


	@SerializedName("call_history")
	private List<CallConversationHistory> callConversationHistory;

	public int getCallCount() {
		return callCount;
	}

	public void setCallCount(int callCount) {
		this.callCount = callCount;
	}

	public List<CallConversationHistory> getCallConversationHistory() {
		return callConversationHistory;
	}

	public void setCallConversationHistory(List<CallConversationHistory> callConversationHistory) {
		this.callConversationHistory = callConversationHistory;
	}
}
package in.appnow.astrobuddy.ui.fragments.call_plans.call_plan_history.mvp;

import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.response.CallConversationHistory;
import in.appnow.astrobuddy.rest.response.CallHistoryResponse;
import in.appnow.astrobuddy.ui.activities.chat_feedback.ChatFeedbackActivity;
import in.appnow.astrobuddy.ui.fragments.call_plans.CallPlansFragment;
import in.appnow.astrobuddy.ui.fragments.call_plans.call_plan_history.CallPlansHistoryFragment;
import in.appnow.astrobuddy.utils.DateUtils;
import in.appnow.astrobuddy.utils.FragmentUtils;
import io.reactivex.Observable;

/**
 * Created by NILESH BHARODIYA on 03-09-2019.
 */
public class CallPlansHistoryModel {

    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;

    public CallPlansHistoryModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
        this.appCompatActivity = appCompatActivity;
        this.apiInterface = apiInterface;
    }

    public Observable<CallHistoryResponse> getCallPlanHistoryResponse() {
        return apiInterface.getCallHistory();
    }

    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    private String getMessage(String usedDate, String name) {

        if (TextUtils.isEmpty(usedDate)) {
            return "Please give feedback for your previous call with " + name;
        }

        return "Please give feedback for your previous call on " +
                DateUtils.parseStringDate(usedDate,
                        DateUtils.SERVER_DATE_FORMAT,
                        DateUtils.CHAT_HISTORY_DATE_FORMAT) +
                " with " + name;
    }

    public void showRateNowDialog(CallConversationHistory conversationHistory) {
        ChatFeedbackActivity.openChatFeedbackActivity(
                appCompatActivity,
                conversationHistory.getPurchasedPlanId(),
                false,
                getMessage(conversationHistory.getUsedDate(),
                        conversationHistory.getName()),
                FragmentUtils.FEEDBACK_TYPE_CALL, CallPlansHistoryFragment.REQUEST_CODE);
    }

    public void showProgressBar() {
        ProgressDialogFragment.showProgress(appCompatActivity.getSupportFragmentManager());
    }

    public void hideProgressBar() {
        ProgressDialogFragment.dismissProgress(appCompatActivity.getSupportFragmentManager());
    }
}

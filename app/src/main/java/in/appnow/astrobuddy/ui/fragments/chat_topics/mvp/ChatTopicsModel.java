package in.appnow.astrobuddy.ui.fragments.chat_topics.mvp;

import androidx.appcompat.app.AppCompatActivity;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.conversation_module.activity.ConversationActivity;
import in.appnow.astrobuddy.conversation_module.chat_history.ChatHistoryFragment;
import in.appnow.astrobuddy.dialog.DialogHelperClass;
import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.response.ChatSampleResponse;
import in.appnow.astrobuddy.rest.response.StartChatResponse;
import in.appnow.astrobuddy.ui.activities.chat_feedback.ChatFeedbackActivity;
import in.appnow.astrobuddy.ui.fragments.myaccount.MyAccountFragment;
import in.appnow.astrobuddy.utils.DateUtils;
import in.appnow.astrobuddy.utils.FragmentUtils;
import io.reactivex.Observable;

/**
 * Created by sonu on 15:44, 17/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ChatTopicsModel {
    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;

    public ChatTopicsModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
        this.appCompatActivity = appCompatActivity;
        this.apiInterface = apiInterface;
    }

    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    public Observable<ChatSampleResponse> fetchChatSample() {
        return apiInterface.getChatSamples();
    }

    public Observable<StartChatResponse> startChat() {
        return apiInterface.startChat();
    }

    public void startConversationActivity(int currentQueue, String sessionId, boolean isExistingChat) {
        ConversationActivity.openActivity(appCompatActivity, currentQueue, sessionId, isExistingChat);
    }

    public void showProgressBar() {
        ProgressDialogFragment.showProgress(appCompatActivity.getSupportFragmentManager());
    }

    public void hideProgressBar() {
        ProgressDialogFragment.dismissProgress(appCompatActivity.getSupportFragmentManager());
    }

    public void showAddTopicDialog(String message) {
        DialogHelperClass.showMessageOKCancel(appCompatActivity, message, "Add Topic", "Cancel", (dialogInterface, i) -> {
            FragmentUtils.onChangeFragment(appCompatActivity.getSupportFragmentManager(), R.id.container_view, MyAccountFragment.newInstance(), FragmentUtils.MY_ACCOUNT_FRAGMENT);
        }, null);
    }

    public void replaceChatHistory() {
        FragmentUtils.onChangeFragment(appCompatActivity.getSupportFragmentManager(), R.id.container_view, ChatHistoryFragment.newInstance(), FragmentUtils.CHAT_HISTORY_FRAGMENT);
    }

    public void openFeedbackActivity(String chatSessionId, String date) {
        ChatFeedbackActivity.openChatFeedbackActivity(appCompatActivity, chatSessionId, false, "Please give feedback for your previous chat on " + DateUtils.parseFeedBackDateFormat(date), FragmentUtils.FEEDBACK_TYPE_CHAT, -1);
    }
}

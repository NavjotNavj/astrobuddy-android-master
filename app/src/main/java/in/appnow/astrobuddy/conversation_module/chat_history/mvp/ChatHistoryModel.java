package in.appnow.astrobuddy.conversation_module.chat_history.mvp;

import androidx.appcompat.app.AppCompatActivity;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.conversation_module.chat_history.single_chat_history.SingleChatHistoryFragment;
import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.request.BaseRequestModel;
import in.appnow.astrobuddy.rest.response.ChatHistoryResponse;
import in.appnow.astrobuddy.rest.response.Conversations;
import in.appnow.astrobuddy.utils.FragmentUtils;
import io.reactivex.Observable;

/**
 * Created by sonu on 13:58, 31/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ChatHistoryModel {

    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;

    public ChatHistoryModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
        this.appCompatActivity = appCompatActivity;
        this.apiInterface = apiInterface;
    }

    public Observable<ChatHistoryResponse> getChatHistory(BaseRequestModel requestModel) {
        return apiInterface.getChatHistory(requestModel);
    }

    public void showProgressBar() {
        ProgressDialogFragment.showProgress(appCompatActivity.getSupportFragmentManager());
    }

    public void hideProgressBar() {
        ProgressDialogFragment.dismissProgress(appCompatActivity.getSupportFragmentManager());

    }

    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    public void openSingleChatFragment(Conversations conversations) {
        FragmentUtils.onChangeFragment(appCompatActivity.getSupportFragmentManager(), R.id.container_view, SingleChatHistoryFragment.newInstance(conversations), FragmentUtils.SINGLE_CHAT_HISTORY_FRAGMENT);
    }
}

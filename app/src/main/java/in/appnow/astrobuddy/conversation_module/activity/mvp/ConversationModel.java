package in.appnow.astrobuddy.conversation_module.activity.mvp;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import java.util.List;

import in.appnow.astrobuddy.conversation_module.activity.ConversationActivity;
import in.appnow.astrobuddy.conversation_module.background_service.ConversationIntentService;
import in.appnow.astrobuddy.conversation_module.rest_service.models.request.FetchMessageRequest;
import in.appnow.astrobuddy.conversation_module.rest_service.models.request.UpdateMessageRequest;
import in.appnow.astrobuddy.conversation_module.rest_service.models.response.ConversationResponse;
import in.appnow.astrobuddy.conversation_module.utils.ConversationUtils;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dao.viewmodel.ConversationViewModel;
import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.request.UpdateSocketIdRequest;
import in.appnow.astrobuddy.rest.response.BaseResponseModel;
import in.appnow.astrobuddy.ui.activities.chat_feedback.ChatFeedbackActivity;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.KeyboardUtils;
import io.reactivex.Observable;

public class ConversationModel {
    private final AppCompatActivity appCompatActivity;
    private final APIInterface blurtApiInterface;
    private ABDatabase abDatabase;
    private ConversationViewModel conversationViewModel;

    public ConversationModel(AppCompatActivity appCompatActivity, APIInterface blurtApiInterface, ViewModelProvider.Factory viewModeFactory, ABDatabase abDatabase) {
        this.appCompatActivity = appCompatActivity;
        this.blurtApiInterface = blurtApiInterface;
        this.abDatabase = abDatabase;
        //instantiate view model
        conversationViewModel = ViewModelProviders.of(appCompatActivity, viewModeFactory).get(ConversationViewModel.class);

    }

    public void getAllMessagesFromServer(FetchMessageRequest requestModel) {
        conversationViewModel.fetchConversationFromServer(requestModel);
    }

    public LiveData<List<ConversationResponse>> getAllMessages() {
        return conversationViewModel.fetchAllConversation(getSessionId());
    }

    public long insertData(ConversationResponse response) {
        return conversationViewModel.insert(response);
    }


    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    public void showProgressBar() {
        ProgressDialogFragment.showProgress(appCompatActivity.getSupportFragmentManager());
    }

    public void hideProgressBar() {
        ProgressDialogFragment.dismissProgress(appCompatActivity.getSupportFragmentManager());

    }

    public void onBackArrowPress() {
        KeyboardUtils.hideSoftKeyboard(appCompatActivity);
        appCompatActivity.finish();
    }

    public boolean isExistingChat() {
        return appCompatActivity.getIntent().getBooleanExtra(ConversationActivity.EXTRA_EXISTING_CHAT, false);
    }

    public String getSessionId() {
        return appCompatActivity.getIntent().getStringExtra(ConversationActivity.EXTRA_SESSION_ID);
    }


    public Observable<BaseResponseModel> updateSocketId(UpdateSocketIdRequest request) {
        return blurtApiInterface.updateSocketId(request);
    }


    public void openChatFeedbackActivity(String sessionId) {
        ChatFeedbackActivity.openChatFeedbackActivity(appCompatActivity, sessionId, true, "Chat successfully done.", FragmentUtils.FEEDBACK_TYPE_CHAT, -1);
    }

    public void updateMessageStatus(UpdateMessageRequest updateMessageRequest) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ConversationUtils.MESSAGE_STATUS_MODEL, updateMessageRequest);
        Intent intent = new Intent(appCompatActivity, ConversationIntentService.class);
        intent.setAction(ConversationIntentService.MESSAGE_STATUS);
        intent.putExtras(bundle);
        appCompatActivity.startService(intent);
    }

    public void close() {
        appCompatActivity.finish();
    }
}

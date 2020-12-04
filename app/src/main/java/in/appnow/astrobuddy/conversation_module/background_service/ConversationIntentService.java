package in.appnow.astrobuddy.conversation_module.background_service;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;

import java.util.List;

import javax.inject.Inject;

import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.conversation_module.rest_service.models.request.UpdateMessageRequest;
import in.appnow.astrobuddy.conversation_module.rest_service.service.ConversationService;
import in.appnow.astrobuddy.conversation_module.utils.ConversationUtils;
import in.appnow.astrobuddy.dagger.component.DaggerMyServiceComponent;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.interfaces.APICallback;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.response.BaseResponseModel;
import in.appnow.astrobuddy.utils.Logger;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sonu on 19/12/17.
 */

public class ConversationIntentService extends IntentService implements APICallback {

    private static final String TAG = ConversationIntentService.class.getSimpleName();
    public static final String KEY = "key";

    public static final String MESSAGE_STATUS = "message_status";

    private static final int UPDATE_MESSAGE_REQUEST_CODE = 2;


    @Inject
    ABDatabase abDatabase;
    @Inject
    APIInterface blurtApiInterface;
    @Inject
    PreferenceManger blurtPreferenceManger;

    public ConversationIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerMyServiceComponent.builder()
                .appComponent(AstroApplication.getInstance().component())
                .build()
                .inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null)
            return;
        String key = intent.getAction();
        if (TextUtils.isEmpty(key))
            return;
        switch (key) {
            case MESSAGE_STATUS:
                UpdateMessageRequest updateMessageRequest = intent.getParcelableExtra(ConversationUtils.MESSAGE_STATUS_MODEL);
                updateMessageRequest.setUserId(blurtPreferenceManger.getUserDetails().getUserId());
                doUpdateMessageStatus(updateMessageRequest);
                break;

        }

    }

    private void doUpdateMessageStatus(UpdateMessageRequest updateMessageRequest) {
        if (updateMessageRequest == null)
            return;
        ConversationService.submitMessageStatus(this, blurtApiInterface, updateMessageRequest, this, UPDATE_MESSAGE_REQUEST_CODE);

    }

    @Override
    public void onResponse(Call<?> call, Response<?> response, int requestCode, Object request) {
        switch (requestCode) {
            case UPDATE_MESSAGE_REQUEST_CODE:
                if (response.isSuccessful()) {
                    BaseResponseModel baseResponseModel = (BaseResponseModel) response.body();
                    if (baseResponseModel != null) {
                        if (baseResponseModel.isErrorStatus()) {
                            Logger.ErrorLog(TAG, "Failed to update message status : " + baseResponseModel.getErrorMessage());
                        } else {
                            UpdateMessageRequest submitMessageStatusRequest = (UpdateMessageRequest) request;
                            if (submitMessageStatusRequest != null) {
                                List<Long> messageIds = submitMessageStatusRequest.getMessageIds();
                                if (messageIds.size() > 0) {
                                    AsyncTask.execute(() -> {
                                        for (Long messageId : messageIds) {
                                            abDatabase.conversationDao().updateChatMessageStatus(submitMessageStatusRequest.getMessageStatus(), messageId);
                                        }
                                    });
                                }
                            }
                            Logger.DebugLog(TAG, "Message status updated successfully.");
                        }
                    } else {
                        Logger.ErrorLog(TAG, "Failed to update message status as response is null");
                    }
                } else {
                    Logger.ErrorLog(TAG, "Failed to update message status due to unsuccessful response");
                }
                break;

        }
    }

    @Override
    public void onFailure(Call<?> call, Throwable t, int requestCode, Object request) {
        switch (requestCode) {
            case UPDATE_MESSAGE_REQUEST_CODE:
                Logger.ErrorLog(TAG, "Failed to update message status : " + t.getLocalizedMessage());
                break;

        }
    }

    @Override
    public void onNoNetwork(int requestCode) {
        switch (requestCode) {

            case UPDATE_MESSAGE_REQUEST_CODE:
                break;

        }
    }

}
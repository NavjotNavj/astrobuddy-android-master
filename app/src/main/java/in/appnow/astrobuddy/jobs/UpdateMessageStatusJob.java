package in.appnow.astrobuddy.jobs;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;

import java.util.List;

import javax.inject.Inject;

import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.conversation_module.rest_service.models.request.UpdateMessageRequest;
import in.appnow.astrobuddy.conversation_module.rest_service.service.ConversationService;
import in.appnow.astrobuddy.conversation_module.utils.ConversationUtils;
import in.appnow.astrobuddy.dagger.component.DaggerMyJobsComponent;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.interfaces.APICallback;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.response.BaseResponseModel;
import in.appnow.astrobuddy.utils.Logger;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sonu on 13:01, 07/08/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class UpdateMessageStatusJob extends Job implements APICallback {
    public static final String TAG = "UpdateMessageStatusJob";

    private static final int UPDATE_MESSAGE_REQUEST_CODE = 2;


    @Inject
    APIInterface apiInterface;
    @Inject
    ABDatabase abDatabase;
    @Inject
    PreferenceManger preferenceManger;

    // start this job when user is logged in
    public static void schedule(UpdateMessageRequest updateMessageRequest) {

        //cancel job if already running
        if (!JobManager.instance().getAllJobRequestsForTag(TAG).isEmpty()) {
            JobUtils.cancelJob(TAG);
        }
        Bundle extras = new Bundle();
        extras.putParcelable(ConversationUtils.MESSAGE_STATUS_MODEL, updateMessageRequest);

        new JobRequest.Builder(UpdateMessageStatusJob.TAG)
                .startNow()
                .setTransientExtras(extras)
                .build()
                .schedule();
    }


    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
        DaggerMyJobsComponent.builder()
                .appComponent(AstroApplication.getInstance().component())
                .build()
                .inject(this);
        try {
            UpdateMessageRequest updateMessageRequest = params.getTransientExtras().getParcelable(ConversationUtils.MESSAGE_STATUS_MODEL);
            if (updateMessageRequest != null) {
                updateMessageRequest.setUserId(preferenceManger.getUserDetails().getUserProfile().getUserId());
                doUpdateMessageStatus(updateMessageRequest);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.SUCCESS;
    }

    private void doUpdateMessageStatus(UpdateMessageRequest updateMessageRequest) {
        ConversationService.submitMessageStatus(getContext(), apiInterface, updateMessageRequest, this, UPDATE_MESSAGE_REQUEST_CODE);

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

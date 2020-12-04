package in.appnow.astrobuddy.jobs;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;

import javax.inject.Inject;

import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.conversation_module.rest_service.models.response.ConversationResponse;
import in.appnow.astrobuddy.conversation_module.utils.ConversationUtils;
import in.appnow.astrobuddy.dagger.component.DaggerMyJobsComponent;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.interfaces.APICallback;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.utils.Logger;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sonu on 13:01, 07/08/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ChatQuerySendJob extends Job implements APICallback{
    public static final String TAG = "ChatQueryJob";

    private static final int SEND_MESSAGE_REQUEST_CODE = 1;


    @Inject
    APIInterface apiInterface;
    @Inject
    ABDatabase abDatabase;

    // start this job when user is logged in
    public static void schedule(ConversationResponse conversationResponse) {

        //cancel job if already running
        if (!JobManager.instance().getAllJobRequestsForTag(TAG).isEmpty()) {
            JobUtils.cancelJob(TAG);
        }
        Bundle extras = new Bundle();
        extras.putParcelable(ConversationUtils.MESSAGE_MODEL, conversationResponse);

        new JobRequest.Builder(ChatQuerySendJob.TAG)
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
            ConversationResponse response = params.getTransientExtras().getParcelable(ConversationUtils.MESSAGE_MODEL);
            if (response != null) {
                doSendMessage(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.SUCCESS;
    }


    private void doSendMessage(ConversationResponse conversationModel) {
       // ConversationService.sendMessage(getContext(), apiInterface, conversationModel, this, SEND_MESSAGE_REQUEST_CODE);
    }

    @Override
    public void onResponse(Call<?> call, Response<?> response, int requestCode, Object request) {
        switch (requestCode) {
            case SEND_MESSAGE_REQUEST_CODE:
                ConversationResponse requestModel = (ConversationResponse) request;
                if (response.isSuccessful()) {
                    ConversationResponse conversationModel = (ConversationResponse) response.body();
                    if (conversationModel != null) {
                        if (conversationModel.isErrorStatus()) {
                            Logger.ErrorLog(TAG, "Failed to send message : " + conversationModel.getMessage());
                            messageSent(false, requestModel);
                        } else {
                            Logger.DebugLog(TAG, "Message sent successfully : " + conversationModel.getMessage());
                            messageSent(true, conversationModel);
                        }
                    } else {
                        Logger.ErrorLog(TAG, "Failed to send message as response is null.");
                        messageSent(false, requestModel);
                    }
                } else {
                    Logger.ErrorLog(TAG, "Failed to send message due to unsuccessful response.");
                    messageSent(false, requestModel);
                }
                break;
        }
    }

    @Override
    public void onFailure(Call<?> call, Throwable t, int requestCode, Object request) {
        switch (requestCode) {
            case SEND_MESSAGE_REQUEST_CODE:
                ConversationResponse requestModel = (ConversationResponse) request;
                Logger.ErrorLog(TAG, "Failed to send message : " + t.getLocalizedMessage());
                messageSent(false, requestModel);
                break;
        }
    }

    @Override
    public void onNoNetwork(int requestCode) {
        switch (requestCode) {
            case SEND_MESSAGE_REQUEST_CODE:
                messageSent(false, null);
                break;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void messageSent(final boolean isSuccessful,
                             final @Nullable ConversationResponse conversationModel) {
        if (conversationModel != null) {

            new AsyncTask<Void, Void, ConversationResponse>() {

                @Override
                protected ConversationResponse doInBackground(Void... voids) {
                    ConversationResponse conversationResponse= null;
                    try {
                        if (isSuccessful) {
                            int updateMessage = abDatabase.conversationDao().updateMessageId(conversationModel.getMessageTemporaryId(), conversationModel.getMessageId());
                            abDatabase.conversationDao().updateMessageStatus(ConversationUtils.MESSAGE_SENT, conversationModel.getMessageTemporaryId());
                            conversationResponse = abDatabase.conversationDao().fetchSingleMessageForTempId(conversationModel.getMessageTemporaryId());
                            //   conversationResponse = abDatabase.conversationDao().fetchSingleMessage(conversationModel.getMessageId());

                        } else {
                            abDatabase.conversationDao().updateMessageStatus(ConversationUtils.MESSAGE_FAILED, conversationModel.getMessageTemporaryId());
                            conversationResponse = abDatabase.conversationDao().fetchSingleMessageForTempId(conversationModel.getMessageTemporaryId());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Logger.ErrorLog(TAG,"Exception : "+e.getLocalizedMessage());
                    }
                    return conversationResponse;
                }

                @Override
                protected void onPostExecute(ConversationResponse conversationModel) {
                    super.onPostExecute(conversationModel);

                   /* Intent messageSent = new Intent(Config.SEND_MESSAGE_PUSH);
                    messageSent.putExtra(ConversationUtils.MESSAGE_SUCCESS, isSuccessful);
                    if (conversationModel != null) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(ConversationUtils.MESSAGE_MODEL, conversationModel);
                        messageSent.putExtras(bundle);
                    }
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(messageSent);*/
                }
            }.execute();
        }
    }

}

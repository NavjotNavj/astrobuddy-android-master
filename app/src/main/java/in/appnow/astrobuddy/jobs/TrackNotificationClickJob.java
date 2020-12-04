package in.appnow.astrobuddy.jobs;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;

import javax.inject.Inject;

import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.dagger.component.DaggerMyJobsComponent;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.interfaces.APICallback;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.request.TrackNotificationClickRequest;
import in.appnow.astrobuddy.rest.response.BaseResponseModel;
import in.appnow.astrobuddy.rest.service.AstroService;
import in.appnow.astrobuddy.services.MyIntentService;
import in.appnow.astrobuddy.utils.Logger;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sonu on 13:01, 07/08/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class TrackNotificationClickJob extends Job {
    public static final String TAG = "TrackNotificationClickJob";
    @Inject
    APIInterface apiInterface;
    @Inject
    PreferenceManger preferenceManger;

    // start this job when user is logged in
    public static void schedule(int notificationId) {

        //cancel job if already running
        if (!JobManager.instance().getAllJobRequestsForTag(TAG).isEmpty()) {
            JobUtils.cancelJob(TAG);
        }
        Bundle extras = new Bundle();
        extras.putInt(MyIntentService.NOTIFICATION_ID, notificationId);

        new JobRequest.Builder(TrackNotificationClickJob.TAG)
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
            int notificationId = params.getTransientExtras().getInt(MyIntentService.NOTIFICATION_ID);
            if (notificationId > 0) {


                TrackNotificationClickRequest trackNotificationClickRequest = new TrackNotificationClickRequest();
                trackNotificationClickRequest.setNotificationId(notificationId);
                trackNotificationClickRequest.setUserId(preferenceManger.getUserDetails().getUserProfile().getUserId());

                AstroService.trackNotificationClick(apiInterface, trackNotificationClickRequest, new APICallback() {
                    @Override
                    public void onResponse(Call<?> call, Response<?> response, int requestCode, @Nullable Object request) {
                        if (response.isSuccessful()) {
                            BaseResponseModel responseModel = (BaseResponseModel) response.body();
                            if (responseModel != null && !responseModel.isErrorStatus()) {
                                Logger.DebugLog(TAG, "Notification click sent done");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<?> call, Throwable t, int requestCode, @Nullable Object request) {
                        Logger.ErrorLog(TAG, "Failed to track notification click : " + t.getLocalizedMessage());
                    }

                    @Override
                    public void onNoNetwork(int requestCode) {

                    }
                }, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.SUCCESS;
    }
}

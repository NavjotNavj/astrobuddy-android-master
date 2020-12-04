package in.appnow.astrobuddy.jobs;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;

import in.appnow.astrobuddy.services.MyIntentService;

/**
 * Created by sonu on 13:01, 07/08/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class AppDownloadJob extends Job {
    public static final String TAG = "AppDownloadJob";

    // start this job when user is logged in
    public static void schedule() {

        //cancel job if already running
        if (!JobManager.instance().getAllJobRequestsForTag(TAG).isEmpty()) {
            JobUtils.cancelJob(TAG);
        }

        new JobRequest.Builder(AppDownloadJob.TAG)
                .startNow()
                .build()
                .schedule();
    }

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
        try {
            Intent intent = new Intent(getContext(), MyIntentService.class);
            intent.setAction(MyIntentService.APP_DOWNLOAD);
            getContext().startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.SUCCESS;
    }
}

package in.appnow.astrobuddy.jobs;

import android.content.Context;

import androidx.annotation.NonNull;

import com.evernote.android.job.DailyJob;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;

import java.util.concurrent.TimeUnit;

/**
 * Created by sonu on 13:01, 07/08/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class PostUserStatsDailyJob extends DailyJob {
    public static final String TAG = "PostUserStatsDailyJob";

    // start this job when user is logged in
    public static void schedule(Context context) {

       // TipOfTheDailyAlarm.setJobAlarm(context);

        //cancel job if already running
        try {
            if (!JobManager.instance().getAllJobRequestsForTag(TAG).isEmpty()) {
                JobUtils.cancelJob(TAG);
            }
            DailyJob.schedule(new JobRequest.Builder(TAG)
                            .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                            .setUpdateCurrent(true),
                    TimeUnit.HOURS.toMillis(10),//10 AM
                    TimeUnit.HOURS.toMillis(17));//5 PM
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    protected DailyJobResult onRunDailyJob(@NonNull Params params) {
        PostUserStatsJob.schedule();
        return DailyJobResult.SUCCESS;
    }
}

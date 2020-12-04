package in.appnow.astrobuddy.jobs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

/**
 * Created by sonu on 16:25, 06/08/18
 * Copyright (c) 2018. All rights reserved.
 */
public class AstroJobCreator implements JobCreator {
    @Nullable
    @Override
    public Job create(@NonNull String tag) {
        try {
            switch (tag) {
                case TipOfTheDailyJob.TAG:
                    return new TipOfTheDailyJob();
                case ChatQuerySendJob.TAG:
                    return new ChatQuerySendJob();
                case UpdateMessageStatusJob.TAG:
                    return new UpdateMessageStatusJob();
                case TrackNotificationClickJob.TAG:
                    return new TrackNotificationClickJob();
                case AppDownloadJob.TAG:
                    return new AppDownloadJob();
                case PostUserStatsDailyJob.TAG:
                    return new PostUserStatsDailyJob();
                case PostUserStatsJob.TAG:
                    return new PostUserStatsJob();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}

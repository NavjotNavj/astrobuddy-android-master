package in.appnow.astrobuddy.jobs;

import com.evernote.android.job.JobManager;

/**
 * Created by sonu on 16/03/18.
 */

public class JobUtils {
    /**
     * method to cancel job is already running
     * @param TAG of the running job
     */
    public static void cancelJob(String TAG) {
        JobManager.instance().cancelAllForTag(TAG);
    }
}

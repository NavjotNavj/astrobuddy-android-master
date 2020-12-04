package in.appnow.astrobuddy.dagger.module;

import com.evernote.android.job.Job;

import dagger.Module;
import dagger.Provides;
import in.appnow.astrobuddy.dagger.scope.MyJobScope;

@Module
public class MyJobsModule {
    private final Job myJob;

    public MyJobsModule(Job myJob) {
        this.myJob = myJob;
    }

    @MyJobScope
    @Provides
    Job providesMyIntentService() {
        return myJob;
    }
}

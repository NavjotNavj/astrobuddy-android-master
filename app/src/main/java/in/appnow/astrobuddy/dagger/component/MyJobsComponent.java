package in.appnow.astrobuddy.dagger.component;

import dagger.Component;
import in.appnow.astrobuddy.dagger.module.MyJobsModule;
import in.appnow.astrobuddy.dagger.scope.MyJobScope;
import in.appnow.astrobuddy.jobs.ChatQuerySendJob;
import in.appnow.astrobuddy.jobs.PostUserStatsJob;
import in.appnow.astrobuddy.jobs.TipOfTheDailyJob;
import in.appnow.astrobuddy.jobs.TrackNotificationClickJob;
import in.appnow.astrobuddy.jobs.UpdateMessageStatusJob;

@MyJobScope
@Component(modules = MyJobsModule.class, dependencies = AppComponent.class)
public interface MyJobsComponent {
    void inject(ChatQuerySendJob chatQuerySendJob);
    void inject(UpdateMessageStatusJob updateMessageStatusJob);
    void inject(TrackNotificationClickJob trackNotificationClickJob);
    void inject(PostUserStatsJob postUserStatsJob);
    void inject(TipOfTheDailyJob tipOfTheDailyJob);
}


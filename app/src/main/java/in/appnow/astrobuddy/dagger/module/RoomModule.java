package in.appnow.astrobuddy.dagger.module;

import android.app.Application;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import dagger.Module;
import dagger.Provides;
import in.appnow.astrobuddy.dagger.scope.AppScope;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dao.BannerStatsDao;
import in.appnow.astrobuddy.dao.ConversationDao;
import in.appnow.astrobuddy.dao.ScreenStatsDao;
import in.appnow.astrobuddy.dao.repository.conversation.ConversationLocalDataSource;
import in.appnow.astrobuddy.dao.repository.conversation.ConversationLocalRepository;
import in.appnow.astrobuddy.dao.repository.conversation.ConversationRemoteDataSource;
import in.appnow.astrobuddy.dao.repository.conversation.ConversationRemoteRepository;
import in.appnow.astrobuddy.dao.viewmodel.CustomViewModelFactory;
import in.appnow.astrobuddy.rest.APIInterface;

@Module
public class RoomModule {

    private final ABDatabase abDatabase;

    public RoomModule(Application application) {
        this.abDatabase = Room.databaseBuilder(application,
                ABDatabase.class,
                "astro_buddy.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    @AppScope
    @Provides
    ABDatabase providesRoomDatabase() {
        return abDatabase;
    }

    @AppScope
    @Provides
    ConversationDao providesConversationDao(ABDatabase demoDatabase) {
        return demoDatabase.conversationDao();
    }


    @AppScope
    @Provides
    BannerStatsDao providesBannerStatsDao(ABDatabase demoDatabase) {
        return demoDatabase.bannerStatsDao();
    }

    @AppScope
    @Provides
    ScreenStatsDao providesScreenStatsDao(ABDatabase demoDatabase) {
        return demoDatabase.screenStatsDao();
    }


    @AppScope
    @Provides
    ConversationLocalRepository conversationLocalRepository(ConversationDao conversationDao) {
        return new ConversationLocalDataSource(conversationDao);
    }

    @AppScope
    @Provides
    ConversationRemoteRepository conversationRemoteRepository(APIInterface apiInterface) {
        return new ConversationRemoteDataSource(apiInterface);
    }

    @Provides
    @AppScope
    ViewModelProvider.Factory provideViewModelFactory(ConversationLocalRepository conversationLocalRepository, ConversationRemoteRepository conversationRemoteRepository) {
        return new CustomViewModelFactory(conversationLocalRepository, conversationRemoteRepository);
    }

}

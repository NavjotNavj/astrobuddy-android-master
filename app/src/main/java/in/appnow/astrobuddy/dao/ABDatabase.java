package in.appnow.astrobuddy.dao;



import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import in.appnow.astrobuddy.conversation_module.rest_service.models.response.ConversationResponse;
import in.appnow.astrobuddy.models.BannerStatsModel;
import in.appnow.astrobuddy.models.ScreenStatsModel;


@TypeConverters(DateConverters.class)
@Database(entities = {ConversationResponse.class, BannerStatsModel.class, ScreenStatsModel.class}, version = ABDatabase.VERSION)
public abstract class ABDatabase extends RoomDatabase {

    static final int VERSION = 1;


    public abstract ConversationDao conversationDao();

    public abstract BannerStatsDao bannerStatsDao();

    public abstract ScreenStatsDao screenStatsDao();


    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }
}

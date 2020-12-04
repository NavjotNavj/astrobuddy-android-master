package in.appnow.astrobuddy.dao;


import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import in.appnow.astrobuddy.models.ScreenStatsModel;

/**
 * Created by sonu on 17:19, 12/04/19
 * Copyright (c) 2019 . All rights reserved.
 */
@Dao
public interface ScreenStatsDao extends BaseDao<ScreenStatsModel> {

    @Query("SELECT * FROM ScreenStats")
    List<ScreenStatsModel> fetchScreenStats();

    @Query("SELECT * FROM ScreenStats WHERE screenName = :screenName ORDER BY id DESC LIMIT 1")
    ScreenStatsModel fetchSingleScreenStats(String screenName);

    @Query("UPDATE ScreenStats SET clickCount= :clickCount, timeStamp= :timeStamp WHERE id = :rowId")
    int updateScreenStatsCount(long rowId, int clickCount, long timeStamp);

    @Query("UPDATE ScreenStats SET stayedTime= :stayedTime, timeStamp= :timeStamp WHERE id = :rowId")
    int updateScreenStatsStayTime(long rowId, int stayedTime, long timeStamp);

    @Query("DELETE FROM ScreenStats")
    void deleteTable();
}

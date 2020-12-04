package in.appnow.astrobuddy.dao;


import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import in.appnow.astrobuddy.models.BannerStatsModel;

/**
 * Created by sonu on 17:19, 12/04/19
 * Copyright (c) 2019 . All rights reserved.
 */
@Dao
public interface BannerStatsDao extends BaseDao<BannerStatsModel> {

    @Query("SELECT * FROM BannerStats")
    List<BannerStatsModel> fetchAllBannerStats();

    @Query("SELECT * FROM BannerStats WHERE postId = :postId ORDER BY id DESC LIMIT 1")
    BannerStatsModel fetchingSingleBannerStats(long postId);

    @Query("UPDATE BannerStats SET impressionsCount= :impressionsCount, timeStamp= :timeStamp WHERE id = :rowId")
    int updateBannerImpressions(long rowId, int impressionsCount, long timeStamp);

    @Query("UPDATE BannerStats SET clickCount= :clickCount, timeStamp= :timeStamp WHERE id = :rowId")
    int updateClickCount(long rowId, int clickCount, long timeStamp);

    @Query("DELETE FROM BannerStats")
    void deleteTable();
}

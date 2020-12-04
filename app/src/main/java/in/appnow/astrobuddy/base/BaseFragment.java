package in.appnow.astrobuddy.base;

import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;

import java.util.concurrent.TimeUnit;

import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.interfaces.OnToolbarListener;
import in.appnow.astrobuddy.models.ScreenStatsModel;
import in.appnow.astrobuddy.utils.DateUtils;
import in.appnow.astrobuddy.utils.Logger;

/**
 * Created by sonu on 12:05, 18/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class BaseFragment extends Fragment {

    private static final String TAG = BaseFragment.class.getSimpleName();
    private Context context;

    private OnToolbarListener onToolbarBackButtonListener;

    private long startTimeMills, endTimeMills;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        try {
            onToolbarBackButtonListener = (OnToolbarListener) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showHideToolbar(boolean show) {
        if (onToolbarBackButtonListener != null) {
            onToolbarBackButtonListener.showToolBar(show);
        }
    }

    public void hideBottomBar(boolean isHide) {
        if (onToolbarBackButtonListener != null) {
            onToolbarBackButtonListener.hideBottomBar(isHide);
        }
    }

    public void updateToolbarTitle(String title) {
        if (onToolbarBackButtonListener != null)
            onToolbarBackButtonListener.onToolbarTitleChange(title);
    }

    public void showHideBackButton(boolean show) {
        if (onToolbarBackButtonListener != null) {
            onToolbarBackButtonListener.onBackButtonChange(show);
            if (show) {

            }
        }
    }

    public void showHideBuyCreditsButton(int visibility) {
        if (onToolbarBackButtonListener != null) {
            onToolbarBackButtonListener.showHideBuyCreditsButton(visibility);
        }
    }

    public void showToolbarBackPrompt(int iconName) {
        if (onToolbarBackButtonListener != null) {
            onToolbarBackButtonListener.showToolbarBackTutorialPrompt(iconName);
        }
    }

    public void hideBuyTopicTooltip() {
        if (onToolbarBackButtonListener != null) {
            onToolbarBackButtonListener.hideBuyTopicTooltip();
        }
    }

    @Nullable
    @Override
    public Context getContext() {
        return context;
    }

    public void forceBackPress() {
        if (onToolbarBackButtonListener != null) {
            onToolbarBackButtonListener.onBackButtonPress();
        }
    }

    public void setStartTimeMills(ABDatabase abDatabase, String screenName) {
        this.startTimeMills = System.currentTimeMillis();

        //update screen click count
        updateScreenStats(abDatabase, screenName, false, 0);

    }

    public void setEndTimeMills(ABDatabase abDatabase, String screenName) {
        this.endTimeMills = System.currentTimeMillis();
        long getStayedTime = getStayedTime();

        //update screen stayed time
        updateScreenStats(abDatabase, screenName, true, getStayedTime);
    }

    public long getStayedTime() {
        return (long) ((endTimeMills - startTimeMills) / 1000.0);
    }

    private void updateScreenStats(ABDatabase abDatabase, String screenName, boolean isStayedStats, long stayedTime) {
        if (abDatabase == null)
            return;
        AsyncTask.execute(() -> {
            if (!TextUtils.isEmpty(screenName)) {
                ScreenStatsModel model = abDatabase.screenStatsDao().fetchSingleScreenStats(screenName);
                long timeStamp = System.currentTimeMillis();
                Logger.DebugLog(TAG, "SCREEN STAYED " + screenName + " - " + stayedTime + " sec");
                if (model != null) {
                    //post already exist in db
                    if (isStayedStats) {
                        updateStayedTimeStats(abDatabase,model.getId(), screenName, model, stayedTime, timeStamp);
                    } else {
                        updateClickCount(abDatabase,model.getId(), screenName, model, stayedTime, timeStamp);
                    }

                } else {
                    //new post impression
                    insertNewEntry(abDatabase, screenName, isStayedStats, stayedTime, timeStamp);
                }
            }
        });

    }

    private void updateStayedTimeStats(ABDatabase abDatabase,long rowId, String screenName, ScreenStatsModel model, long stayedTime, long timeStamp) {
        if (abDatabase == null)
            return;

        long previousTimeStamp = model.getTimeStamp();
        if (DateUtils.getDate(previousTimeStamp, DateUtils.SERVER_ONLY_DATE_FORMAT).equalsIgnoreCase(DateUtils.getDate(timeStamp, DateUtils.SERVER_ONLY_DATE_FORMAT))) {
            //both date are same means entry is of same day. Update the entry
            int previousStayTime = model.getStayedTime();
            previousStayTime += stayedTime;
            long id = abDatabase.screenStatsDao().updateScreenStatsStayTime(rowId, previousStayTime, timeStamp);
            Logger.DebugLog(TAG, "POST UPDATE STAYED TIME : " + id);
        } else {
            //both date are not same do a new entry
            insertNewEntry(abDatabase, screenName, true, stayedTime, timeStamp);
        }

    }

    private void updateClickCount(ABDatabase abDatabase,long rowId, String screenName, ScreenStatsModel model, long stayedTime, long timeStamp) {
        if (abDatabase == null)
            return;

        long previousTimeStamp = model.getTimeStamp();
        if (DateUtils.getDate(previousTimeStamp, DateUtils.SERVER_ONLY_DATE_FORMAT).equalsIgnoreCase(DateUtils.getDate(timeStamp, DateUtils.SERVER_ONLY_DATE_FORMAT))) {
            //both date are same means entry is of same day. Update the entry
            int previousCount = model.getClickCount();
            previousCount += 1;
            long id = abDatabase.screenStatsDao().updateScreenStatsCount(rowId, previousCount, timeStamp);
            Logger.DebugLog(TAG, "POST UPDATE CLICK COUNT : " + id);
        } else {
            insertNewEntry(abDatabase, screenName, false, stayedTime, timeStamp);
        }

    }

    private void insertNewEntry(ABDatabase abDatabase, String screenName, boolean isStayedStats, long stayedTime, long timeStamp) {
        if (abDatabase == null)
            return;
        ScreenStatsModel screenStatsModel = new ScreenStatsModel();
        screenStatsModel.setScreenName(screenName);
        screenStatsModel.setTimeStamp(timeStamp);
        if (isStayedStats) {
            screenStatsModel.setStayedTime((int) stayedTime);
        } else {
            screenStatsModel.setClickCount(1);
        }
        long id = abDatabase.screenStatsDao().insert(screenStatsModel);
        Logger.DebugLog(TAG, "POST INSERT : " + id);
    }

}

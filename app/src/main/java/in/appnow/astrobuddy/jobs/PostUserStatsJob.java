package in.appnow.astrobuddy.jobs;

import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import in.appnow.astrobuddy.BuildConfig;
import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.dagger.component.DaggerMyJobsComponent;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dao.BannerStatsDao;
import in.appnow.astrobuddy.dao.ScreenStatsDao;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.interfaces.APICallback;
import in.appnow.astrobuddy.models.BannerStatsModel;
import in.appnow.astrobuddy.models.ScreenStatsModel;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.request.PostUserStatsRequest;
import in.appnow.astrobuddy.rest.response.BaseResponseModel;
import in.appnow.astrobuddy.rest.service.AstroService;
import in.appnow.astrobuddy.utils.DateUtils;
import in.appnow.astrobuddy.utils.Logger;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sonu on 13:01, 07/08/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class PostUserStatsJob extends Job {
    public static final String TAG = "PostUserStatsJob";

    @Inject
    APIInterface apiInterface;
    @Inject
    ABDatabase abDatabase;
    @Inject
    PreferenceManger preferenceManger;

    private static final int EXECUTION_WINDOW_START = 15;//minutes = 1hour (after 1 hour job will start)
    private static final int EXECUTION_WINDOW_END = 7 * 60;//minutes = 3hour (till 3 hour job will run)

    private static int RETRY_COUNT = 0;//retry count of an api
    private static final int MAX_RETRY_COUNT = 3;//max retry count job can do

    private int failedAttempt;//to check how many times an api failed (in a loop)
    private int totalStatCount;//total stat count on date basis

    private static final int BACKOFF_CRITERIA_INTERVAL = 10;//minutes

    private List<Long> statsDateList = new ArrayList<>();

    // start this job when user is logged in
    public static void schedule() {

        //cancel job if already running
        try {
            if (!JobManager.instance().getAllJobRequestsForTag(TAG).isEmpty()) {
                JobUtils.cancelJob(TAG);
            }

            new JobRequest.Builder(PostUserStatsJob.TAG)
                    .setExecutionWindow(TimeUnit.MINUTES.toMillis(EXECUTION_WINDOW_START), TimeUnit.MINUTES.toMillis(EXECUTION_WINDOW_END))
                    .setBackoffCriteria(TimeUnit.MINUTES.toMillis(BACKOFF_CRITERIA_INTERVAL), JobRequest.BackoffPolicy.LINEAR)
                    .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                    .setUpdateCurrent(true)
                    .build()
                    .schedule();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
        DaggerMyJobsComponent.builder()
                .appComponent(AstroApplication.getInstance().component())
                .build()
                .inject(this);

        // long currentTimeMills = System.currentTimeMillis();

        //String getCurrentHourIn24Hours = DateUtils.getDate(currentTimeMills, DateUtils.POST_STATS_HOUR_FORMAT);

        /*    if (!TextUtils.isEmpty(getCurrentHourIn24Hours) && Integer.parseInt(getCurrentHourIn24Hours) >= 1 && Integer.parseInt(getCurrentHourIn24Hours) <= 20) {*/
        fetchUserStatsData();
        //Logger.DebugLog(TAG,"Post User Stats Time : "+ getCurrentHourIn24Hours);
        //Crashlytics.log("Post User Stats Time : " + getCurrentHourIn24Hours);
        /*}else{
            Logger.DebugLog(TAG,"Post User Failed Stats Time : "+ getCurrentHourIn24Hours);
            Crashlytics.log("Post User Failed Stats Time : " + getCurrentHourIn24Hours);
        }
*/
        return Result.SUCCESS;
    }

    private void fetchUserStatsData() {
        statsDateList.clear();

        ScreenStatsDao screenStatsDao = abDatabase.screenStatsDao();

        List<ScreenStatsModel> screenStatsModelList = screenStatsDao.fetchScreenStats();
        if (screenStatsModelList != null && screenStatsModelList.size() > 0) {
            for (ScreenStatsModel model : screenStatsModelList) {
                if (isDateNoExist(model.getTimeStamp())) {
                    statsDateList.add(model.getTimeStamp());
                }
            }
        }

        BannerStatsDao bannerStatsDao = abDatabase.bannerStatsDao();
        List<BannerStatsModel> bannerStatsModelList = bannerStatsDao.fetchAllBannerStats();

        if (bannerStatsModelList != null && bannerStatsModelList.size() > 0) {
            for (BannerStatsModel model : bannerStatsModelList) {
                if (isDateNoExist(model.getTimeStamp())) {
                    statsDateList.add(model.getTimeStamp());
                }
            }
        }

        if (statsDateList != null && statsDateList.size() > 0) {

            List<PostUserStatsRequest> postUserStatsRequestList = new ArrayList<>();

            for (long timeStamp : statsDateList) {

                PostUserStatsRequest postUserStatsRequest = new PostUserStatsRequest();
                postUserStatsRequest.setUserId(preferenceManger.getUserDetails().getUserId());
                postUserStatsRequest.setStatDate(DateUtils.getDate(timeStamp, DateUtils.SERVER_DATE_FORMAT));
                postUserStatsRequest.setAppVersion(BuildConfig.VERSION_NAME);
                postUserStatsRequest.setSourceId(1);

                String date = DateUtils.getDate(timeStamp, DateUtils.SERVER_ONLY_DATE_FORMAT);

                if (screenStatsModelList != null && screenStatsModelList.size() > 0) {
                    List<ScreenStatsModel> screenStatsModels = new ArrayList<>();
                    for (ScreenStatsModel model : screenStatsModelList) {
                        if (DateUtils.getDate(model.getTimeStamp(), DateUtils.SERVER_ONLY_DATE_FORMAT).equalsIgnoreCase(date)) {
                            screenStatsModels.add(model);
                        }
                    }
                    if (screenStatsModels.size() > 0) {
                        postUserStatsRequest.setScreenStatsModelList(screenStatsModels);
                    } else {
                        postUserStatsRequest.setScreenStatsModelList(new ArrayList<>());
                    }
                } else {
                    postUserStatsRequest.setScreenStatsModelList(new ArrayList<>());
                }

                if (bannerStatsModelList != null && bannerStatsModelList.size() > 0) {
                    List<BannerStatsModel> bannerStatsModels = new ArrayList<>();
                    for (BannerStatsModel model : bannerStatsModelList) {
                        if (DateUtils.getDate(model.getTimeStamp(), DateUtils.SERVER_ONLY_DATE_FORMAT).equalsIgnoreCase(date)) {
                            bannerStatsModels.add(model);
                        }
                    }
                    if (bannerStatsModels.size() > 0) {
                        postUserStatsRequest.setBannerStatsModelList(bannerStatsModels);
                    } else {
                        postUserStatsRequest.setBannerStatsModelList(new ArrayList<>());
                    }
                } else {
                    postUserStatsRequest.setBannerStatsModelList(new ArrayList<>());
                }
                postUserStatsRequestList.add(postUserStatsRequest);
            }
            //Logger.DebugLog("STATS DATA", "JSON : " + postUserStatsRequestList.toString());
            if (postUserStatsRequestList.size() > 0) {
                for (PostUserStatsRequest request : postUserStatsRequestList) {
                    totalStatCount++;
                    postUserStats(request);
                }
            }

        }

    }

    /**
     * check if stat date already exist in state_date_list
     * @param timeStamp
     * @return
     */
    private boolean isDateNoExist(long timeStamp) {
        if (statsDateList == null || statsDateList.size() == 0)
            return true;
        String date = DateUtils.getDate(timeStamp, DateUtils.SERVER_ONLY_DATE_FORMAT);
        for (long time : statsDateList) {
            String saveDate = DateUtils.getDate(time, DateUtils.SERVER_ONLY_DATE_FORMAT);
            if (date.equalsIgnoreCase(saveDate)) {
                return false;
            }
        }
        return true;
    }

    private void postUserStats(PostUserStatsRequest postUserStatsRequest) {
        try {
            AstroService.postUserStats(apiInterface, postUserStatsRequest, new APICallback() {
                @Override
                public void onResponse(Call<?> call, Response<?> response, int requestCode, @Nullable Object request) {
                    if (response.isSuccessful()) {
                        BaseResponseModel responseModel = (BaseResponseModel) response.body();
                        if (responseModel != null) {
                            if (!responseModel.isErrorStatus()) {
                                Logger.DebugLog(TAG, "USER STATS POSTED SUCCESSFULLY");

                                AsyncTask.execute(() -> {
                                    if (postUserStatsRequest.getScreenStatsModelList() != null && postUserStatsRequest.getScreenStatsModelList().size() > 0) {
                                        int deleteItems = abDatabase.screenStatsDao().delete(postUserStatsRequest.getScreenStatsModelList());
                                        Logger.DebugLog(TAG, "SCREEN DELETE ROWS : " + deleteItems);

                                    }
                                    if (postUserStatsRequest.getBannerStatsModelList() != null && postUserStatsRequest.getBannerStatsModelList().size() > 0) {
                                        int deleteItems = abDatabase.bannerStatsDao().delete(postUserStatsRequest.getBannerStatsModelList());
                                        Logger.DebugLog(TAG, "BANNER DELETE ROWS : " + deleteItems);
                                    }
                                });
                                rescheduleJob();
                            } else {
                                //don't reschedule job some server error
//                                Crashlytics.log("USER STATS RESPONSE (TRUE) : " + responseModel.toString());
                            }
                        } else {
                            failedAttempt++;
                            rescheduleJob();
//                            Crashlytics.log("USER STATS RESPONSE NULL");
                        }
                    } else {
                        failedAttempt++;
                        rescheduleJob();
//                        Crashlytics.log("USER STATS API NOT SUCCESSFUL");
                    }
                }

                @Override
                public void onFailure(Call<?> call, Throwable t, int requestCode, @Nullable Object request) {
                    Logger.ErrorLog(TAG, "Failed to post user stats : " + t.getLocalizedMessage());
//                    Crashlytics.logException(t);
                    t.printStackTrace();
                    failedAttempt++;
                    rescheduleJob();
                }

                @Override
                public void onNoNetwork(int requestCode) {

                }
            }, 2);
        } catch (Exception e) {
            e.printStackTrace();
            failedAttempt++;
            rescheduleJob();
        }
    }

    private void rescheduleJob() {
        if (failedAttempt > 0 && totalStatCount == statsDateList.size()) {
            failedAttempt = 0;
            totalStatCount = 0;
            RETRY_COUNT++;
            if (RETRY_COUNT <= MAX_RETRY_COUNT) {
                schedule();
            }
        } else {
            RETRY_COUNT = 0;
        }
    }

}

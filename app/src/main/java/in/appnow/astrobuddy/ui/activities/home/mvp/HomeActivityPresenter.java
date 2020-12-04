package in.appnow.astrobuddy.ui.activities.home.mvp;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.evernote.android.job.JobManager;
import com.google.gson.Gson;
import com.razorpay.Checkout;

import java.util.ArrayList;
import java.util.List;

import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.base.BasePresenter;
import in.appnow.astrobuddy.conversation_module.utils.ConversationUtils;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dao.BannerStatsDao;
import in.appnow.astrobuddy.dao.ScreenStatsDao;
import in.appnow.astrobuddy.dialog.DialogHelperClass;
import in.appnow.astrobuddy.fcm.NotificationUtils;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.models.BannerStatsModel;
import in.appnow.astrobuddy.models.LogoutModel;
import in.appnow.astrobuddy.models.ScreenStatsModel;
import in.appnow.astrobuddy.rest.CallbackWrapper;
import in.appnow.astrobuddy.rest.request.BaseRequestModel;
import in.appnow.astrobuddy.rest.request.PostUserStatsRequest;
import in.appnow.astrobuddy.rest.response.BaseResponseModel;
import in.appnow.astrobuddy.rest.response.MyAccountResponse;
import in.appnow.astrobuddy.rest.response.StartChatResponse;
import in.appnow.astrobuddy.rest.response.StartChatResponseData;
import in.appnow.astrobuddy.ui.activities.main.mvp.MainActivityView;
import in.appnow.astrobuddy.utils.DateUtils;
import in.appnow.astrobuddy.utils.Logger;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Abhishek Thanvi on 19/03/19.
 * Copyright © 2019 Abhishek Thanvi. All rights reserved.
 */

public class HomeActivityPresenter implements BasePresenter, MainActivityView.LogoutListener {

    private static final String TAG = HomeActivityPresenter.class.getSimpleName();
    private final HomeActivityView view;
    private final HomeActivityModel model;
    private final PreferenceManger preferenceManger;
    private final ABDatabase abDatabase;
    private final CompositeDisposable disposable = new CompositeDisposable();


    public HomeActivityPresenter(HomeActivityView view, HomeActivityModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        this.view = view;
        this.model = model;
        this.preferenceManger = preferenceManger;
        this.abDatabase = abDatabase;
    }


    @Override
    public void onCreate() {
        view.setLogoutListener(this);

      /*  AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                fetchUserStatsData();

            }
        });*/

    }

    private List<Long> statsDateList = new ArrayList<>();

    private boolean isDateExist(long timeStamp) {
        if (statsDateList != null && statsDateList.size() > 0) {
            String date = DateUtils.getDate(timeStamp, DateUtils.SERVER_ONLY_DATE_FORMAT);
            for (long time : statsDateList) {
                if (DateUtils.getDate(time, DateUtils.SERVER_ONLY_DATE_FORMAT).equalsIgnoreCase(date)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void fetchUserStatsData() {
        statsDateList.clear();

        ScreenStatsDao screenStatsDao = abDatabase.screenStatsDao();

        List<ScreenStatsModel> screenStatsModelList = screenStatsDao.fetchScreenStats();
        if (screenStatsModelList != null && screenStatsModelList.size() > 0) {
            for (ScreenStatsModel model : screenStatsModelList) {
                if (!isDateExist(model.getTimeStamp())) {
                    statsDateList.add(model.getTimeStamp());
                }
            }
        }

        BannerStatsDao bannerStatsDao = abDatabase.bannerStatsDao();
        List<BannerStatsModel> bannerStatsModelList = bannerStatsDao.fetchAllBannerStats();

        if (bannerStatsModelList != null && bannerStatsModelList.size() > 0) {
            for (BannerStatsModel model : bannerStatsModelList) {
                if (!isDateExist(model.getTimeStamp())) {
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
            if (postUserStatsRequestList.size() > 0) {
                Gson gson = new Gson();

                Logger.DebugLog(TAG, "STATS JSON : " + gson.toJson(postUserStatsRequestList));
            }

        }

    }


    private Disposable openChatActivity(boolean isNotify) {
        model.showProgressBar();
        return model.startChat()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> model.hideProgressBar())
                .subscribeWith(new CallbackWrapper<StartChatResponse>(view, this) {
                    @Override
                    protected void onSuccess(StartChatResponse data) {
                        if (data != null) {
                            if (isNotify) {
                                ToastUtils.shortToast(data.getErrorMessage());
                            } else {
                                if (!data.isErrorStatus()) {
                                   /* if (data.isFeedbackPending()) {
                                        model.openFeedbackActivity(data.getChatSessionId());
                                    } else */
                                    StartChatResponseData responseData = data.getResponseData();
//                                    if (!data.isHandlerAvailable()) {
//                                        showNotifyMeDialog(data.getMessage());
//                                    } else
                                        if (responseData.isExistingChat() || responseData.isTopicAvailable()|| responseData.getChatStatus().equalsIgnoreCase(ConversationUtils.CHAT_WAITING)) {
                                        model.startConversationActivity(0, responseData.getChatSessionId(), responseData.isExistingChat());
                                    } else if (!responseData.isTopicAvailable()) {
                                        model.showAddTopicDialog(data.getErrorMessage());
                                    } else {
                                        ToastUtils.shortToast(data.getErrorMessage());
                                    }
                                } else {
                                    ToastUtils.shortToast(data.getErrorMessage());
                                }
                            }
                        }
                    }
                });
    }

    private void showNotifyMeDialog(String message) {
        DialogHelperClass.showMessageOKCancel(model.getAppCompatActivity(), message, "Yes", "No", (dialogInterface, i) -> {
            if (AstroApplication.getInstance().isInternetConnected(true)) {
                startChat(true);
            }
        }, null);
    }


    public void startChat(boolean isNotify) {
        disposable.add(openChatActivity(isNotify));
    }

    private Disposable observeBuyCreditsButtonClick() {
        return view.observeBuyCreditsButton()
                .doOnNext(__ -> model.showProgressBar())
                .map(isConnected -> AstroApplication.getInstance().isInternetConnected(true))
                .observeOn(Schedulers.io())
                .switchMap(isConnected -> {
                    if (isConnected) {
                        BaseRequestModel requestModel = new BaseRequestModel();
                        requestModel.setUserId(preferenceManger.getUserDetails().getUserProfile().getUserId());
                        return model.getMyAccountDetails(requestModel);
                    } else {
                        return Observable.just(new MyAccountResponse());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> model.hideProgressBar())
                .subscribeWith(new CallbackWrapper<MyAccountResponse>(view, this) {
                    @Override
                    protected void onSuccess(MyAccountResponse data) {
                        int topicsCount = 0;
                        if (data != null) {
                            if (!data.isErrorStatus() && data.getAccountDetails() != null) {
                                topicsCount = data.getAccountDetails().getUserTopics();
                            } else {
                                topicsCount = 0;
                                ToastUtils.shortToast(data.getErrorMessage());
                            }
                        }
                        model.replaceAddTopicPointsFragment(topicsCount);
                    }
                });
    }

    public void openBuyCreditScreen() {
        if (AstroApplication.getInstance().isInternetConnected(true))
            disposable.add(getAccountDetails());
    }

    private Disposable getAccountDetails() {
        model.showProgressBar();
        BaseRequestModel requestModel = new BaseRequestModel();
        requestModel.setUserId(preferenceManger.getUserDetails().getUserProfile().getUserId());
        return model.getMyAccountDetails(requestModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> model.hideProgressBar())
                .subscribeWith(new CallbackWrapper<MyAccountResponse>(view, this) {
                    @Override
                    protected void onSuccess(MyAccountResponse data) {
                        int topicsCount = 0;
                        if (data != null) {
                            if (!data.isErrorStatus() && data.getAccountDetails() != null) {
                                topicsCount = data.getAccountDetails().getUserTopics();
                            } else {
                                topicsCount = 0;
                                ToastUtils.shortToast(data.getErrorMessage());
                            }
                        }
                        model.replaceAddTopicPointsFragment(topicsCount);
                    }
                });
    }

    public void checkForSessionExpired() {
        LogoutModel logoutModel = preferenceManger.getSessionExpiredModel();
        if (logoutModel != null && logoutModel.isForceLogout()) {
            //show dialog for force logout
            DialogHelperClass.showMessageOKCancel(model.getAppCompatActivity(), logoutModel.getTitle(), logoutModel.getBody(), "OK", "", (dialogInterface, i) -> {
                disposable.add(doLogout());
            }, null);
        }
    }

    private Disposable doLogout() {
        model.showProgressBar();
        BaseRequestModel requestModel = new BaseRequestModel();
        requestModel.setUserId(preferenceManger.getUserDetails().getUserProfile().getUserId());
        return model.doLogout(requestModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> model.hideProgressBar())
                .subscribeWith(new CallbackWrapper<BaseResponseModel>(view, this) {
                    @Override
                    protected void onSuccess(BaseResponseModel responseModel) {
                        if (responseModel != null) {
                            if (responseModel.isErrorStatus()) {
                                ToastUtils.longToast(responseModel.getErrorMessage());
                            } else {
                                clearOtherStuffsOnLogout();
                            }
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void unAuthorizeUserAccess(String message) {

    }


    @SuppressLint("StaticFieldLeak")
    private void clearOtherStuffsOnLogout() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                //Clear Razor pay data when user logout
                model.getAppCompatActivity().runOnUiThread(() -> Checkout.clearUserData(view.getContext()));

                //delete chat table
                abDatabase.conversationDao().deleteChatTable();
                abDatabase.screenStatsDao().deleteTable();
                abDatabase.bannerStatsDao().deleteTable();

                //clear use session
                preferenceManger.logoutUser();

                //clear tip of the day
                preferenceManger.putString(PreferenceManger.TIP_OF_THE_DAY, "");

                //remove chatting related data like handler id, chat query and notify type
                preferenceManger.putString(PreferenceManger.HANDLER_ID, "");
                preferenceManger.putString(PreferenceManger.CHAT_QUERY, "");
                preferenceManger.putString(PreferenceManger.CHAT_SESSION_ID, "");
                preferenceManger.putInt(PreferenceManger.CONVERSATION_NOTIFY_TYPE, ConversationUtils.NOTIFY_NONE);
                preferenceManger.putPendingFeedback(null);
                preferenceManger.putSessionExpiredModel(null);

                //cancel/remove all notifications linked with app during logout
                NotificationUtils.cancelAllNotification();

                //cancel all running jobs
                try {
                    JobManager.instance().cancelAll();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                ToastUtils.shortToast("Logout Success.");
                model.onLogoutSuccess();
            }
        }.execute();
    }


    @Override
    public void onDoLogout() {
        disposable.add(doLogout());
    }

    public boolean isForecastShow() {
        return preferenceManger.getBooleanValue(PreferenceManger.FORECAST_SCREEN_SHOW);
    }
}

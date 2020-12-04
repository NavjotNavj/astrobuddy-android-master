package in.appnow.astrobuddy.ui.activities.main.mvp;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.evernote.android.job.JobManager;
import com.razorpay.Checkout;

import java.util.concurrent.TimeUnit;

import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.base.BasePresenter;
import in.appnow.astrobuddy.conversation_module.activity.mvp.ConversationPresenter;
import in.appnow.astrobuddy.conversation_module.utils.ConversationUtils;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dialog.DialogHelperClass;
import in.appnow.astrobuddy.dialog.UnAuthoriseUserDialog;
import in.appnow.astrobuddy.fcm.NotificationUtils;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.models.LogoutModel;
import in.appnow.astrobuddy.rest.CallbackWrapper;
import in.appnow.astrobuddy.rest.request.BaseRequestModel;
import in.appnow.astrobuddy.rest.response.BaseResponseModel;
import in.appnow.astrobuddy.rest.response.MyAccountResponse;
import in.appnow.astrobuddy.rest.response.StartChatResponse;
import in.appnow.astrobuddy.rest.response.StartChatResponseData;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by sonu on 11:03, 13/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class MainActivityPresenter implements BasePresenter, MainActivityView.LogoutListener {

    private static final String TAG = ConversationPresenter.class.getSimpleName();
    private static final long SIDE_MENU_DELAY = 2000;
    private static final long BUY_CREDIT_TOOLTIP_DELAY = 5100;
    private final MainActivityView view;
    private final MainActivityModel model;
    private final PreferenceManger preferenceManger;
    private final ABDatabase abDatabase;
    private final CompositeDisposable disposable = new CompositeDisposable();

    public MainActivityPresenter(MainActivityView view, MainActivityModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        this.view = view;
        this.model = model;
        this.preferenceManger = preferenceManger;
        this.abDatabase = abDatabase;
    }

    @Override
    public void onCreate() {
        view.setLogoutListener(this);
        updateHeaderData();
        setNavigationAdapter();
        disposable.add(observeBuyCreditsButtonClick());
    }

    public void addTooltipDelay() {
        disposable.add(buyCreditsTooltipDelay());

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
                                    if (responseData.isExistingChat() || responseData.isTopicAvailable() || responseData.getChatStatus().equalsIgnoreCase(ConversationUtils.CHAT_WAITING)) {
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

    public void updateHeaderData() {
        try {
            if (preferenceManger != null && view != null) {
                view.setUpNavigationHeaderView(preferenceManger.getUserDetails().getUserProfile());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private Disposable buyCreditsTooltipDelay() {
        return Observable.timer(BUY_CREDIT_TOOLTIP_DELAY, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(__ -> {
                    view.showBuyCreditsTooltip();
                });
    }


    private void setNavigationAdapter() {
        view.setUpNavigationList(model.getNavigationList());
    }

    public void checkIfSideMenuHintShown(int childPosition) {
     /*   if (preferenceManger.getBooleanValue(PreferenceManger.CHART_TAP_HINT) &&
                !preferenceManger.getBooleanValue(PreferenceManger.SIDE_MENU_HINT)) {
            view.showNavigationMenuPrompt(childPosition);
            preferenceManger.putBoolean(PreferenceManger.SIDE_MENU_HINT, true);
        }*/
        if (preferenceManger.getBooleanValue(PreferenceManger.HOROSCOPE_TAP_HINT) && !preferenceManger.getBooleanValue(PreferenceManger.SIDE_MENU_HINT)) {
            view.showNavigationMenuPrompt(childPosition);
            preferenceManger.putBoolean(PreferenceManger.SIDE_MENU_HINT, true);
        }
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

    @Override
    public void onDestroy() {
        view.hideTooltipMenu();
        disposable.clear();
    }

    @Override
    public void unAuthorizeUserAccess(String message) {
        UnAuthoriseUserDialog.getInstance().showLogOutDialog(
                model.getAppCompatActivity(),
                message,
                preferenceManger,
                abDatabase,
                new UnAuthoriseUserDialog.OnLogoutListener() {
                    @Override
                    public void OnClickLogOut() {
                        model.showProgressBar();
                    }

                    @Override
                    public void OnLogOut() {
                        model.hideProgressBar();
                        FragmentUtils.onLogoutSuccess(model.getAppCompatActivity());
                    }
                });
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

    @SuppressLint("StaticFieldLeak")
    private void clearOtherStuffsOnLogout() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                //Clear Razor pay data when user logout
                model.getAppCompatActivity().runOnUiThread(() -> Checkout.clearUserData(view.getContext()));

                //delete chat table
                abDatabase.conversationDao().deleteChatTable();

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

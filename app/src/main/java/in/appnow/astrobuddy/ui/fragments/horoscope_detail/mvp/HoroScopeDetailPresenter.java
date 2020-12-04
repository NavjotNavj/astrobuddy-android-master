package in.appnow.astrobuddy.ui.fragments.horoscope_detail.mvp;

import android.text.TextUtils;

import in.appnow.astrobuddy.app.AstroAppConstants;
import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.base.BasePresenter;
import in.appnow.astrobuddy.conversation_module.utils.ConversationUtils;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dialog.DialogHelperClass;
import in.appnow.astrobuddy.dialog.UnAuthoriseUserDialog;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.models.PendingFeedbackModel;
import in.appnow.astrobuddy.models.SunSignModel;
import in.appnow.astrobuddy.rest.CallbackWrapper;
import in.appnow.astrobuddy.rest.request.HoroscopeDetailRequest;
import in.appnow.astrobuddy.rest.response.HoroscopeDetailResponse;
import in.appnow.astrobuddy.rest.response.StartChatResponse;
import in.appnow.astrobuddy.rest.response.StartChatResponseData;
import in.appnow.astrobuddy.ui.fragments.horoscope_detail.mvp.view.HoroScopeDetailView;
import in.appnow.astrobuddy.utils.DateUtils;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Abhishek Thanvi on 24/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class HoroScopeDetailPresenter implements BasePresenter {

    private static final String TAG = HoroScopeDetailPresenter.class.getSimpleName();
    private final HoroScopeDetailView view;
    private final HoroScopeDetailModel model;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private SunSignModel sunSignModel;
    private final PreferenceManger preferenceManger;
    private ABDatabase abDatabase;

    public HoroScopeDetailPresenter(HoroScopeDetailView view, HoroScopeDetailModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        this.view = view;
        this.model = model;
        this.preferenceManger = preferenceManger;
        this.abDatabase = abDatabase;
    }

    public void setSunSignModel(SunSignModel sunSignModel) {
        this.sunSignModel = sunSignModel;
        view.updateViews(sunSignModel);
    }

    @Override
    public void onCreate() {
        if (!preferenceManger.getBooleanValue(PreferenceManger.HOROSCOPE_TAP_HINT)) {
            preferenceManger.putBoolean(PreferenceManger.HOROSCOPE_TAP_HINT, true);
        }
        disposable.add(getHoroscopeDetail());
        disposable.add(onTodayDetailButtonClick());
        disposable.add(onTomorrowDetailButtonClick());
        disposable.add(onMonthlyDetailButtonClick());
        disposable.add(onYearlyDetailButtonClick());
        disposable.add(observeStartChatButtonClick());
    }

    private boolean checkPendingFeedback() {
        PendingFeedbackModel pendingFeedbackModel = preferenceManger.getPendingFeedback();
        if (pendingFeedbackModel != null) {
            if (pendingFeedbackModel.isFeedbackPending() && !TextUtils.isEmpty(pendingFeedbackModel.getSessionId())) {
                model.openFeedbackActivity(pendingFeedbackModel.getSessionId(), pendingFeedbackModel.getStartTimeStamp());
                return true;
            }
            return false;
        }
        return false;
    }

    private Disposable observeStartChatButtonClick() {
        return view.observeChatButton()
                .doOnNext(__ -> model.showProgressBar())
                .map(isConnected -> !checkPendingFeedback() && AstroApplication.getInstance().isInternetConnected(true))
                .observeOn(Schedulers.io())
                .switchMap(isConnected -> {
                    if (isConnected) {
                        return model.startChat();
                    } else {
                        return Observable.just(new StartChatResponse());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> model.hideProgressBar())
                .subscribeWith(new CallbackWrapper<StartChatResponse>(view, this) {
                    @Override
                    protected void onSuccess(StartChatResponse data) {
                        if (data != null) {
                            if (!data.isErrorStatus()) {
                                StartChatResponseData responseData = data.getResponseData();
                                if ( responseData.isTopicAvailable()) {
                                    preferenceManger.putString(PreferenceManger.CHAT_SESSION_ID, responseData.getChatSessionId());
                                    model.startConversationActivity(0, responseData.getChatSessionId(), responseData.isExistingChat());
                                } else if (!responseData.isTopicAvailable()) {
                                    if (!TextUtils.isEmpty(data.getErrorMessage()))
                                        model.showAddTopicDialog(data.getErrorMessage());
                                } else {
                                    ToastUtils.shortToast(data.getErrorMessage());
                                }
                            } else {
                                ToastUtils.shortToast(data.getErrorMessage());

                            }

                        }
                    }
                });

    }


    private void showNotifyMeDialog(String message) {
        DialogHelperClass.showMessageOKCancel(model.getAppCompatActivity(), message, "Yes", "No", (dialogInterface, i) -> {
            if (AstroApplication.getInstance().isInternetConnected(true)) {
                disposable.add(updateChatNotifyMe());
            }

        }, null);
    }

    private Disposable updateChatNotifyMe() {
        model.showProgressBar();
        return model.startChat()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> model.hideProgressBar())
                .subscribeWith(new CallbackWrapper<StartChatResponse>(view, this) {
                    @Override
                    protected void onSuccess(StartChatResponse data) {
                        if (data != null) {
                            ToastUtils.shortToast(data.getErrorMessage());
                            preferenceManger.putInt(PreferenceManger.CONVERSATION_NOTIFY_TYPE, ConversationUtils.NOTIFY_ME);
                        }
                    }
                });


    }

    private Disposable onTodayDetailButtonClick() {
        return view.todayDetailClick().subscribe(__ -> view.updateHoroscopeForecast(AstroAppConstants.TODAY));
    }

    private Disposable onTomorrowDetailButtonClick() {
        return view.tomorrowDetailClick().subscribe(__ -> view.updateHoroscopeForecast(AstroAppConstants.TOMORROW));
    }

    private Disposable onMonthlyDetailButtonClick() {
        return view.monthlyDetailClick().subscribe(__ -> view.updateHoroscopeForecast(AstroAppConstants.MONTHLY));
    }

    private Disposable onYearlyDetailButtonClick() {
        return view.yearlyDetailClick().subscribe(__ -> view.updateHoroscopeForecast(AstroAppConstants.YEARLY));
    }

    private Disposable getHoroscopeDetail() {
        HoroscopeDetailRequest horoscopeDetailRequest = new HoroscopeDetailRequest(sunSignModel.getSunId(), DateUtils.getDate(System.currentTimeMillis(), DateUtils.FORECAST_DATE_FORMAT));
        return model.getHoroscopeDetail(horoscopeDetailRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new CallbackWrapper<HoroscopeDetailResponse>(view, this) {
                    @Override
                    protected void onSuccess(HoroscopeDetailResponse data) {
                        if (data != null) {
                            if (data.isErrorStatus()) {
                                ToastUtils.shortToast(data.getErrorMessage());
                            } else {
                                view.setHoroscopeDetailResponse(data);
                            }
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
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
}

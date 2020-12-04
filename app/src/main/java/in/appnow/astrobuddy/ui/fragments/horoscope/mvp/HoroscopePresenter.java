package in.appnow.astrobuddy.ui.fragments.horoscope.mvp;

import android.text.TextUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

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
import in.appnow.astrobuddy.rest.response.StartChatResponse;
import in.appnow.astrobuddy.rest.response.StartChatResponseData;
import in.appnow.astrobuddy.rest.response.TipOfTheDayResponse;
import in.appnow.astrobuddy.rest.request.TipOfTheRequest;
import in.appnow.astrobuddy.utils.DateUtils;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by sonu on 11:17, 13/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class HoroscopePresenter implements BasePresenter {
    private static final long CHAT_ANIM_DELAY = 10200;
    private final HoroscopeView view;
    private final HoroscopeModel model;
    private final PreferenceManger preferenceManger;
    private ABDatabase abDatabase;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private OnHoroscopeTutorialListener onHoroscopeTutorialListener;

    public HoroscopePresenter(HoroscopeView view, HoroscopeModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        this.view = view;
        this.model = model;
        this.preferenceManger = preferenceManger;
        this.abDatabase = abDatabase;
    }

    public void setOnHoroscopeTutorialListener(OnHoroscopeTutorialListener onHoroscopeTutorialListener) {
        this.onHoroscopeTutorialListener = onHoroscopeTutorialListener;
    }

    @Override
    public void onCreate() {
        //  view.startStopLottieAnimation(true);
        //   if (preferenceManger.getBooleanValue(PreferenceManger.CHAT_TAP_HINT)) {
        //  disposable.add(chatAnimationDelay());
        // }
        disposable.add(observeOpenMenuClick());
        disposable.add(observeCloseMenuClick());
        disposable.add(observeChartButtonClick());
        // loadTipOfTheDay();
        try {
            if (AstroApplication.getInstance().isInternetConnected(true)) {
                // disposable.add(getTipOfTheDay());
            }
        } catch (Exception ignored) {

        }
        checkIfHoroscopeHintShown();
    }

    private Disposable chatAnimationDelay() {
        return Observable.timer(CHAT_ANIM_DELAY, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(__ -> {
                    view.animateTalkToAstroBuddyButton();
                });
    }

    private Disposable observeOpenMenuClick() {
        return view.observeMenuClickButton()
                .doOnNext(__ -> view.openMenu())
                .subscribe();
    }

    private Disposable observeCloseMenuClick() {
        return view.observeMainLayoutClick()
                .doOnNext(__ -> view.closeMenu())
                .subscribe(__ -> {
                            preferenceManger.putBoolean(PreferenceManger.HOROSCOPE_TAP_HINT, true);
                            if (onHoroscopeTutorialListener != null) {
                                onHoroscopeTutorialListener.onComplete();
                            }
                        }
                );
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


    private Disposable doStartChat() {
        model.showProgressBar();
        return model.startChat()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> model.hideProgressBar())
                .subscribeWith(new CallbackWrapper<StartChatResponse>(view, this) {
                    @Override
                    protected void onSuccess(StartChatResponse data) {
                        if (data != null) {
                            if (!data.isErrorStatus()) {

                                StartChatResponseData responseData = data.getResponseData();
                                    if (responseData.isExistingChat() || responseData.isTopicAvailable() || responseData.getChatStatus().equalsIgnoreCase(ConversationUtils.CHAT_WAITING)) {
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


    private Disposable observeChartButtonClick() {
        return view.observeChartButtonClick()
                .doOnNext(__ -> {
                    if (AstroApplication.getInstance().isInternetConnected(true))
                        model.replaceTalkAstroBuddyFragment();
                })
                .subscribe();
    }

    @Override
    public void onDestroy() {
        view.startStopLottieAnimation(false);
        if (preferenceManger.getBooleanValue(PreferenceManger.CHAT_TAP_HINT))
            view.clearChatAnimation();
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

    private Disposable getTipOfTheDay() {
        TipOfTheRequest request = new TipOfTheRequest();
        request.setStarSign(preferenceManger.getUserDetails().getUserProfile().getStarSign());
        request.setCurrentDate(DateUtils.getDate(System.currentTimeMillis(), DateUtils.SERVER_ONLY_DATE_FORMAT));
        return model.getTipOfTheDay(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new CallbackWrapper<TipOfTheDayResponse>(view, this) {
                    @Override
                    protected void onSuccess(TipOfTheDayResponse data) {
                        if (data != null) {
                            if (!data.isErrorStatus()) {
                                preferenceManger.putString(PreferenceManger.TIP_OF_THE_DAY, data.getTipOfTheDay());
                                loadTipOfTheDay();
                            }
                        }
                    }
                });
    }

    private void loadTipOfTheDay() {
        String tipOfTheDay = preferenceManger.getStringValue(PreferenceManger.TIP_OF_THE_DAY);
        if (!TextUtils.isEmpty(tipOfTheDay))
            view.updateTipOfTheDay(tipOfTheDay);
    }


    /* Tutorial Tap Prompt */
    private void checkIfHoroscopeHintShown() {
        if (!preferenceManger.getBooleanValue(PreferenceManger.ALL_HINT) ||
                preferenceManger.getBooleanValue(PreferenceManger.HOROSCOPE_TAP_HINT)) {
            //checkIfChartMenuHint();
            view.showForecastTooltip();
        } else {
            view.showHoroscopePrompt();
            preferenceManger.putBoolean(PreferenceManger.HOROSCOPE_TAP_HINT,true);
        }
    }

    private void checkIfChatMenuHint() {
        if (!preferenceManger.getBooleanValue(PreferenceManger.CHAT_TAP_HINT) && preferenceManger.getBooleanValue(PreferenceManger.BUY_CREDITS_HINT)) {
            view.showChatPrompt();
            preferenceManger.putBoolean(PreferenceManger.CHAT_TAP_HINT, true);
        }
    }

    private void checkIfChartMenuHint() {
       /* if (!preferenceManger.getBooleanValue(PreferenceManger.CHART_TAP_HINT) && preferenceManger.getBooleanValue(PreferenceManger.HOROSCOPE_TAP_HINT)) {
            view.showTalkToAstroBuddyPrompt();
            preferenceManger.putBoolean(PreferenceManger.CHART_TAP_HINT, true);
        } else {
            checkIfChatMenuHint();
        }*/
        if (onHoroscopeTutorialListener != null) {
            onHoroscopeTutorialListener.onComplete();
        }
        checkIfChatMenuHint();
    }

    public void openUserForecast() {
        try {
            if (preferenceManger != null && preferenceManger.getUserDetails().getUserProfile() != null) {
                String starSign = preferenceManger.getUserDetails().getUserProfile().getStarSign();
                if (!TextUtils.isEmpty(starSign)) {
                    List<SunSignModel> signModelList = SunSignModel.getSunSignList(view.getContext());
                    for (int i = 0; i < signModelList.size(); i++) {
                        if (signModelList.get(i).getSunSignName().equalsIgnoreCase(starSign)) {
                            view.openForecastDetail(i);
                            return;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnHoroscopeTutorialListener {
        public void onComplete();
    }
}

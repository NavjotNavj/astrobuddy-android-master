package in.appnow.astrobuddy.ui.fragments.panchang.mvp;

import android.text.TextUtils;

import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.base.BasePresenter;
import in.appnow.astrobuddy.conversation_module.utils.ConversationUtils;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dialog.DialogHelperClass;
import in.appnow.astrobuddy.dialog.UnAuthoriseUserDialog;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.models.PendingFeedbackModel;
import in.appnow.astrobuddy.rest.CallbackWrapper;
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
 * Created by sonu on 13:12, 09/04/19
 * Copyright (c) 2019 . All rights reserved.
 */
public class PanchangPresenter implements BasePresenter {
    private final PanchangView view;
    private final PanchangModel model;
    public PreferenceManger preferenceManger;
    private ABDatabase abDatabase;
    private CompositeDisposable disposable = new CompositeDisposable();

    public PanchangPresenter(PanchangView view, PanchangModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        this.view = view;
        this.model = model;
        this.preferenceManger = preferenceManger;
        this.abDatabase = abDatabase;
    }

    @Override
    public void onCreate() {
        disposable.add(observeStartChatButtonClick());
    }

    public void fetchPanchangDetails(String date, String time, String location, String latLong) {
        fetchPanchang(date, time, latLong, null);
        preferenceManger.putString(PreferenceManger.PANCHANG_LOCATION, location);
        preferenceManger.putString(PreferenceManger.PANCHANG_LATLNG, latLong);
    }

    public void fetchPanchang(String date, String time, String latLong, String language) {
        if (!TextUtils.isEmpty(language))
            preferenceManger.putString(PreferenceManger.ASTROLOGY_API_LANGUAGE, language);
        view.getPanchangDetails(date, time, latLong);
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
                           /*     if (data.isFeedbackPending()) {
                                    model.openFeedbackActivity(data.getChatSessionId());
                                } else*/
//                                if (!data.isHandlerAvailable()) {
//                                    if (!TextUtils.isEmpty(data.getMessage()))
//                                        showNotifyMeDialog(data.getMessage());
//                                } else

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

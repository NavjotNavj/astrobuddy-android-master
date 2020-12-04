package in.appnow.astrobuddy.ui.fragments.myth_buster.myth_buster_detail.mvp;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.tbruyelle.rxpermissions2.RxPermissions;

import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.base.BasePresenter;
import in.appnow.astrobuddy.conversation_module.utils.ConversationUtils;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dialog.DialogHelperClass;
import in.appnow.astrobuddy.dialog.UnAuthoriseUserDialog;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.models.PendingFeedbackModel;
import in.appnow.astrobuddy.rest.CallbackWrapper;
import in.appnow.astrobuddy.rest.response.MythBuster;
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
 * Created by sonu on 16:45, 02/08/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class MythBusterDetailPresenter implements BasePresenter {
    private final MythBusterDetailView view;
    private final MythBusterDetailModel model;
    private final PreferenceManger preferenceManger;
    private ABDatabase abDatabase;

    private final CompositeDisposable disposable = new CompositeDisposable();

    private MythBuster mythBuster;

    private RxPermissions rxPermissions;

    public MythBusterDetailPresenter(MythBusterDetailView view, MythBusterDetailModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        this.view = view;
        this.model = model;
        this.preferenceManger = preferenceManger;
        this.abDatabase = abDatabase;
    }

    public void setUpMythBuster(MythBuster mythBuster) {
        this.mythBuster = mythBuster;
    }

    @Override
    public void onCreate() {
        this.rxPermissions = new RxPermissions(model.getAppCompatActivity());
        view.updateViews(mythBuster);
        disposable.add(observeStartChatButtonClick());
        disposable.add(observeShareMythButton());
    }

    private Disposable observeShareMythButton() {
        return view.observeShareButton()
                .compose(rxPermissions.ensureEachCombined(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                .subscribe(permission -> { // will emit 1 Permission object
                            if (permission.granted) {
                                // All permissions are granted !
                                // model.onPermissionGranted();
                                if (AstroApplication.getInstance().isInternetConnected(true))
                                    disposable.add(startDownloadingImage());
                            } else if (permission.shouldShowRequestPermissionRationale) {
                                // At least one denied permission without ask never again
                                model.onPermissionDenied();

                            } else {
                                // At least one denied permission with ask never again
                                // Need to go to the settings
                                model.onPermissionDeniedPermanently();
                            }
                        }
                );
    }

    private Disposable startDownloadingImage() {
        model.showProgressBar();
        return model.downloadImage(mythBuster.getSource())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> model.hideProgressBar())
                .doOnError(error -> ToastUtils.shortToast("Failed to share image. Please try again."))
                .subscribe(data -> {
                            if (data != null) {
                                // display the image data in a ImageView or save it
                                Bitmap bitmap = BitmapFactory.decodeStream(data.byteStream());
                                String messageToShare = mythBuster.getTitle() + "\n\nTo read this myth-buster and more download AstroBuddy app now : https://play.google.com/store/apps/details?id=in.appnow.astrobuddy";
                                model.shareProduct(bitmap, messageToShare);
                            } else {
                                ToastUtils.shortToast("Failed to share image. Please try again.");
                            }
                        },
                        throwable -> ToastUtils.shortToast("Failed to share image. Please try again."));

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
        return view.observeStartChatButton()
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

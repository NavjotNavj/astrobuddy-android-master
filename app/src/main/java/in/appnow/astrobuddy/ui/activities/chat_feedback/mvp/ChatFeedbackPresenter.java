package in.appnow.astrobuddy.ui.activities.chat_feedback.mvp;

import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.base.BasePresenter;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dialog.UnAuthoriseUserDialog;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.rest.CallbackWrapper;
import in.appnow.astrobuddy.rest.request.CallFeedbackRequest;
import in.appnow.astrobuddy.rest.request.ChatFeedbackRequest;
import in.appnow.astrobuddy.rest.response.BaseResponseModel;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by sonu on 17:29, 07/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ChatFeedbackPresenter implements BasePresenter {

    private final ChatFeedbackView view;
    private final ChatFeedbackModel model;
    private final PreferenceManger preferenceManger;
    private ABDatabase abDatabase;

    private final CompositeDisposable disposable = new CompositeDisposable();

    public ChatFeedbackPresenter(ChatFeedbackView view, ChatFeedbackModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        this.view = view;
        this.model = model;
        this.preferenceManger = preferenceManger;
        this.abDatabase = abDatabase;
    }

    @Override
    public void onCreate() {
        view.updateMessageLabel(model.getMessage());
        disposable.add(observeDoneButtonClick());
    }


    private Disposable observeDoneButtonClick() {
        return view.observeSubmitButton()
                .doOnNext(__ -> model.showProgress())
                .map(isValidate -> view.isRatingSelected() && AstroApplication.getInstance().isInternetConnected(true))
                .observeOn(Schedulers.io())
                .switchMap(isValidate -> {
                    if (isValidate) {

                        if (model.getFeedBackType().equalsIgnoreCase(FragmentUtils.FEEDBACK_TYPE_CHAT)) {
                            ChatFeedbackRequest request = new ChatFeedbackRequest(model.getSessionId(), (int) view.getChatRating(), view.getChatFeedback());
                            request.setUserId(preferenceManger.getUserDetails().getUserProfile().getUserId());
                            return model.sendFeedback(request);

                        } else {
                            CallFeedbackRequest request = new CallFeedbackRequest(model.getSessionId(),
                                    String.valueOf(view.getChatRating()), view.getChatFeedback());
                            return model.sendFeedback(request);
                        }

                    } else {
                        BaseResponseModel responseModel = new BaseResponseModel();
                        responseModel.setErrorStatus(true);
                        return Observable.just(responseModel);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> model.hideProgress())
                .subscribeWith(new CallbackWrapper<BaseResponseModel>(view, this) {
                    @Override
                    protected void onSuccess(BaseResponseModel data) {
                        if (data != null) {
                            if (!data.isErrorStatus()) {
                                if (model.getFeedBackType().equalsIgnoreCase(FragmentUtils.FEEDBACK_TYPE_CHAT)) {
                                    preferenceManger.putPendingFeedback(null);
                                }
                                model.closeActivity();
                            }
                            ToastUtils.shortToast(data.getErrorMessage());
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
                        model.showProgress();
                    }

                    @Override
                    public void OnLogOut() {
                        model.hideProgress();
                        FragmentUtils.onLogoutSuccess(model.getAppCompatActivity());
                    }
                });
    }
}

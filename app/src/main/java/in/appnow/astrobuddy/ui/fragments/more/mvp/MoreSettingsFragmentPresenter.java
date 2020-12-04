package in.appnow.astrobuddy.ui.fragments.more.mvp;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.evernote.android.job.JobManager;
import com.razorpay.Checkout;

import in.appnow.astrobuddy.base.BasePresenter;
import in.appnow.astrobuddy.conversation_module.utils.ConversationUtils;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dialog.UnAuthoriseUserDialog;
import in.appnow.astrobuddy.fcm.NotificationUtils;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.rest.CallbackWrapper;
import in.appnow.astrobuddy.rest.request.BaseRequestModel;
import in.appnow.astrobuddy.rest.response.BaseResponseModel;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Abhishek Thanvi on 22/03/19.
 * Copyright © 2019 Abhishek Thanvi. All rights reserved.
 */

public class MoreSettingsFragmentPresenter implements BasePresenter, MoreSettingsFragmentView.LogoutListener {

    private static final String TAG = MoreSettingsFragmentPresenter.class.getSimpleName();
    private final MoreSettingsFragmentView view;
    private final MoreSettingsModel model;
    private PreferenceManger preferenceManger;
    private ABDatabase abDatabase;

    private final CompositeDisposable disposable = new CompositeDisposable();

    public MoreSettingsFragmentPresenter(MoreSettingsFragmentView view, MoreSettingsModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        this.view = view;
        this.model = model;
        this.preferenceManger = preferenceManger;
        this.abDatabase = abDatabase;
    }

    @Override
    public void onCreate() {
        view.setLogoutListener(this);
        view.setUpMoreList(model.getMoreItemList(), preferenceManger);
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
                model.getAppCompatActivity().runOnUiThread(() -> {
                            Checkout.clearUserData(view.getContext());
                        }
                );

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

}

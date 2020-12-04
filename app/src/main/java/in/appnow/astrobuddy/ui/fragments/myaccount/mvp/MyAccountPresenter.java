package in.appnow.astrobuddy.ui.fragments.myaccount.mvp;

import android.view.View;

import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.base.BasePresenter;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dialog.UnAuthoriseUserDialog;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.rest.CallbackWrapper;
import in.appnow.astrobuddy.rest.request.BaseRequestModel;
import in.appnow.astrobuddy.rest.response.MyAccountResponse;
import in.appnow.astrobuddy.rest.response.TransactionHistoryResponse;
import in.appnow.astrobuddy.ui.fragments.myaccount.mvp.view.MyAccountView;
import in.appnow.astrobuddy.utils.FragmentUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by sonu on 11:00, 12/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class MyAccountPresenter implements BasePresenter {
    private static final String TAG = MyAccountPresenter.class.getSimpleName();
    private final MyAccountView view;
    private final MyAccountModel model;
    private PreferenceManger preferenceManger;
    private ABDatabase abDatabase;

    private final CompositeDisposable disposable = new CompositeDisposable();

    public MyAccountPresenter(MyAccountView view, MyAccountModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        this.view = view;
        this.model = model;
        this.preferenceManger = preferenceManger;
        this.abDatabase = abDatabase;
    }

    @Override
    public void onCreate() {
        disposable.add(observeAddTopicPoints());
        disposable.add(observeUpgradePlan());
        disposable.add(getTransactionHistory());
    }

    public PreferenceManger getPreferenceManger() {
        return preferenceManger;
    }

    private Disposable observeAddTopicPoints() {
        return view.observeAddChatTopicsButton()
                .doOnNext(__ -> model.replaceAddTopicPointsFragment(view.getTopicsCount()))
                .subscribe();
    }

    private Disposable observeUpgradePlan() {
        return view.observeUpgradePlanButton()
                .doOnNext(__ -> {
                    if (AstroApplication.getInstance().isInternetConnected(true))
                        model.replaceUpgradePlanFragment();
                })
                .subscribe();
    }


    private Disposable getMyAccountDetails() {
        BaseRequestModel baseRequestModel = new BaseRequestModel();
        baseRequestModel.setUserId(preferenceManger.getUserDetails().getUserProfile().getUserId());
        return model.getMyAccountDetails(baseRequestModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new CallbackWrapper<MyAccountResponse>(view, this) {
                    @Override
                    protected void onSuccess(MyAccountResponse data) {
                        if (data != null) {
                            view.updateMyAccount(data);
                        }
                    }
                });
    }

    private Disposable getTransactionHistory() {
        view.showHideProgressBar(View.VISIBLE);
        BaseRequestModel baseRequestModel = new BaseRequestModel();
        baseRequestModel.setUserId(preferenceManger.getUserDetails().getUserProfile().getUserId());
        return model.getTransactionHistory(baseRequestModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> view.showHideProgressBar(View.GONE))
                .subscribeWith(new CallbackWrapper<TransactionHistoryResponse>(view, this) {
                    @Override
                    protected void onSuccess(TransactionHistoryResponse data) {
                        disposable.add(getMyAccountDetails());
                        if (data != null) {
                            view.setTransactionHistory(data,preferenceManger.getUserDetails().getUserProfile().getUserId(),model.getApiInterface());
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

package in.appnow.astrobuddy.ui.fragments.referral.mvp;

import in.appnow.astrobuddy.base.BasePresenter;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dialog.UnAuthoriseUserDialog;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.rest.CallbackWrapper;
import in.appnow.astrobuddy.rest.request.BaseRequestModel;
import in.appnow.astrobuddy.rest.response.ReferralCodeResponse;
import in.appnow.astrobuddy.ui.fragments.referral.mvp.view.ReferralView;
import in.appnow.astrobuddy.utils.FragmentUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by sonu on 11:00, 12/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ReferralPresenter implements BasePresenter {
    private final ReferralView view;
    private final ReferralModel model;
    private final PreferenceManger preferenceManger;
    private ABDatabase abDatabase;

    private final CompositeDisposable disposable = new CompositeDisposable();

    public ReferralPresenter(ReferralView view, ReferralModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        this.view = view;
        this.model = model;
        this.preferenceManger = preferenceManger;
        this.abDatabase = abDatabase;
    }

    @Override
    public void onCreate() {
        view.startStopLottieAnimation(true);
        setReferralAdapter();
        disposable.add(fetchReferralCode());
        disposable.add(view.onRecyclerClickEvent());
    }

    private Disposable fetchReferralCode() {
        view.updateReferral("");
        BaseRequestModel requestModel = new BaseRequestModel();
        requestModel.setUserId(preferenceManger.getUserDetails().getUserProfile().getUserId());
        return model.fetchReferralCode(requestModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new CallbackWrapper<ReferralCodeResponse>(view, this) {
                    @Override
                    protected void onSuccess(ReferralCodeResponse data) {
                        if (data != null) {
                            if (!data.isErrorStatus() && data.isUserExist()) {
                                view.updateReferral(data.getReferralCode());
                            } else {
                                view.updateReferral("");
                            }
                        } else {
                            view.updateReferral("");
                        }
                    }
                });
    }

    private void setReferralAdapter() {
        view.setReferralData(model.getReferralList());
    }

    @Override
    public void onDestroy() {
        view.startStopLottieAnimation(false);
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

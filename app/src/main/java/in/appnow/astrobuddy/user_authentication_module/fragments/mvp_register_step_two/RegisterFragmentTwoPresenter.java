package in.appnow.astrobuddy.user_authentication_module.fragments.mvp_register_step_two;

import in.appnow.astrobuddy.app.AstroAppConstants;
import in.appnow.astrobuddy.base.BasePresenter;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dialog.UnAuthoriseUserDialog;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.rest.response.ApplyPromoCodeResponse;
import in.appnow.astrobuddy.ui.activities.promo_code.PromoCodeActivity;
import in.appnow.astrobuddy.user_authentication_module.fragments.RegisterFragmentStepTwo;
import in.appnow.astrobuddy.utils.FragmentUtils;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Abhishek Thanvi on 10/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class RegisterFragmentTwoPresenter implements BasePresenter {

    private final RegisterFragmentTwoView registerFragmentTwoView;

    private final RegisterFragmentTwoModel registerFragmentTwoModel;
    private PreferenceManger preferenceManger;
    private ABDatabase abDatabase;

    private final CompositeDisposable disposable = new CompositeDisposable();

    public RegisterFragmentTwoPresenter(RegisterFragmentTwoView registerFragmentTwoView, RegisterFragmentTwoModel registerFragmentTwoModel, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        this.registerFragmentTwoView = registerFragmentTwoView;
        this.registerFragmentTwoModel = registerFragmentTwoModel;
        this.preferenceManger = preferenceManger;
        this.abDatabase = abDatabase;
    }

    @Override
    public void onCreate() {
        disposable.add(observePromoButton());
        disposable.add(observeRemovePromoCode());
    }

    private Disposable observePromoButton() {
        return registerFragmentTwoView.observePromoCodeButton()
                .doOnNext(__ -> PromoCodeActivity.startPromoCodeActivity(registerFragmentTwoModel.getAppCompatActivity(), AstroAppConstants.REGISTER_TYPE, 0, 0, RegisterFragmentStepTwo.APPLY_PROMOCODE_REQUEST_CODE))
                .subscribe();

    }

    private Disposable observeRemovePromoCode() {
        return registerFragmentTwoView.observeRemovePromoCode()
                .doOnNext(__ -> {
                    registerFragmentTwoView.showDialogToRemovePromoCode("Are you sure you want to remove PromoCode?");
                })
                .subscribe();

    }

    @Override
    public void onDestroy() {
        disposable.clear();
    }

    @Override
    public void unAuthorizeUserAccess(String message) {
        UnAuthoriseUserDialog.getInstance().showLogOutDialog(
                registerFragmentTwoModel.getAppCompatActivity(),
                message,
                preferenceManger,
                abDatabase,
                new UnAuthoriseUserDialog.OnLogoutListener() {
                    @Override
                    public void OnClickLogOut() {
                        registerFragmentTwoModel.showProgressBar();
                    }

                    @Override
                    public void OnLogOut() {
                        registerFragmentTwoModel.hideProgressBar();
                        FragmentUtils.onLogoutSuccess(registerFragmentTwoModel.getAppCompatActivity());
                    }
                });
    }

    public void promoCodeApplied(String promoCode, ApplyPromoCodeResponse response, int promoId) {
        registerFragmentTwoView.updatePromoCodeViews(response, promoCode, promoId);
        registerFragmentTwoView.onPromoCodeApplied(true);
    }
}

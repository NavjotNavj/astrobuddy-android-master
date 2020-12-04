package in.appnow.astrobuddy.ui.activities.promo_code.mvp;

import android.text.TextUtils;
import android.view.View;

import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.base.BasePresenter;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.dialog.UnAuthoriseUserDialog;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.rest.CallbackWrapper;
import in.appnow.astrobuddy.rest.request.ApplyPromoCodeRequest;
import in.appnow.astrobuddy.rest.response.ApplyPromoCodeResponse;
import in.appnow.astrobuddy.rest.request.PromoCodeRequest;
import in.appnow.astrobuddy.rest.response.PromoCodeResponse;
import in.appnow.astrobuddy.ui.activities.promo_code.mvp.view.PromoCodeView;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.LocaleUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by sonu on 15:04, 04/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class PromoCodePresenter implements BasePresenter {
    private static final String TAG = PromoCodePresenter.class.getSimpleName();
    private final PromoCodeView view;
    private final PromoCodeModel model;
    private final PreferenceManger preferenceManger;
    private ABDatabase abDatabase;

    private final CompositeDisposable disposable = new CompositeDisposable();

    public PromoCodePresenter(PromoCodeView view, PromoCodeModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        this.view = view;
        this.model = model;
        this.preferenceManger = preferenceManger;
        this.abDatabase = abDatabase;
    }

    @Override
    public void onCreate() {
        disposable.add(observeApplyButtonClick());
        disposable.add(fetchPromoCodes());
        disposable.add(onRecyclerClickEvent());
    }

    private Disposable onRecyclerClickEvent() {
        return view.adapter.getCodesPublishSubject().subscribe(object -> {
            if (AstroApplication.getInstance().isInternetConnected(true)) {
                disposable.add(onPromoCodeSelected(object));
            }
        });
    }

    private Disposable observeApplyButtonClick() {
        return view.observeApplyButtonClick()
                .doOnNext(__ -> ProgressDialogFragment.showProgress(model.getAppCompatActivity().getSupportFragmentManager()))
                .map(isConnected -> AstroApplication.getInstance().isInternetConnected(true))
                .observeOn(Schedulers.io())
                .switchMap(isConnected -> {
                    if (isConnected) {
                        ApplyPromoCodeRequest request = new ApplyPromoCodeRequest();
                        request.setPurpose(model.getPromoType());
                        request.setTopicCount(model.getTopicsCount());//0 for registration
                        String userId = "0";
                        if (preferenceManger.getUserDetails() != null)
                            userId = preferenceManger.getUserDetails().getUserProfile().getUserId();
                        request.setUserId(userId);
                        request.setPromoId(0);
                        request.setPromoCode(view.getPromoCode());
                        request.setAmount(model.getAmount());
                        return model.applyPromoCode(request);
                    } else return Observable.just(new ApplyPromoCodeResponse());
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> ProgressDialogFragment.dismissProgress(model.getAppCompatActivity().getSupportFragmentManager()))
                .subscribeWith(new CallbackWrapper<ApplyPromoCodeResponse>(view, this) {
                    @Override
                    protected void onSuccess(ApplyPromoCodeResponse response) {
                        if (!TextUtils.isEmpty(response.getPromoId()) && !response.getPromoId().equalsIgnoreCase("null")) {
                            onPromoCodeApplied(Integer.parseInt(response.getPromoId()), view.getPromoCode(), response);
                        }else {
                            ToastUtils.shortToast(response.getErrorMessage());
                        }
                    }
                });
    }


    private Disposable onPromoCodeSelected(PromoCodeResponse.PromoCodes promoCodes) {
        ProgressDialogFragment.showProgress(model.getAppCompatActivity().getSupportFragmentManager());
        ApplyPromoCodeRequest request = new ApplyPromoCodeRequest();
        request.setPurpose(model.getPromoType());
        request.setTopicCount(model.getTopicsCount());//0 for registration
        String userId = "0";
        if (preferenceManger.getUserDetails() != null)
            userId = preferenceManger.getUserDetails().getUserProfile().getUserId();
        request.setUserId(userId);
        request.setPromoId(promoCodes.getPromoId());
        request.setPromoCode(promoCodes.getPromoCode());
        request.setAmount(model.getAmount());

        return model.applyPromoCode(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> ProgressDialogFragment.dismissProgress(model.getAppCompatActivity().getSupportFragmentManager()))
                .subscribeWith(new CallbackWrapper<ApplyPromoCodeResponse>(view, this) {
                    @Override
                    protected void onSuccess(ApplyPromoCodeResponse response) {
                        onPromoCodeApplied(promoCodes.getPromoId(), promoCodes.getPromoCode(), response);
                    }
                });

    }

    private void onPromoCodeApplied(int promoId, String promoCode, ApplyPromoCodeResponse data) {
        if (data != null) {
            if (!data.isErrorStatus() && data.isPromoApplied()) {
                model.onPromoCodeAppliedSuccessfully(data, promoCode, promoId);
            } else {
                ToastUtils.shortToast(data.getErrorMessage());
            }
        }
    }

    private Disposable fetchPromoCodes() {
        view.showHideProgressBar(View.VISIBLE);
        PromoCodeRequest request = new PromoCodeRequest();
        String userId = "0";
        if (preferenceManger.getUserDetails() != null)
            userId = preferenceManger.getUserDetails().getUserProfile().getUserId();
        request.setUserId(userId);
        request.setLocale(LocaleUtils.fetchCountryISO(model.getAppCompatActivity()));
        request.setPurpose(model.getPromoType());

        return model.fetchPromoCodes(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> view.showHideProgressBar(View.GONE))
                .subscribeWith(new CallbackWrapper<PromoCodeResponse>(view, this) {
                    @Override
                    protected void onSuccess(PromoCodeResponse data) {
                        if (data != null) {
                            view.updateView(data);
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

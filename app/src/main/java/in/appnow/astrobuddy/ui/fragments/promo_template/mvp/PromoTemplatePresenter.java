package in.appnow.astrobuddy.ui.fragments.promo_template.mvp;

import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.base.BasePresenter;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.rest.CallbackWrapper;
import in.appnow.astrobuddy.rest.request.BaseRequestModel;
import in.appnow.astrobuddy.rest.response.MyAccountResponse;
import in.appnow.astrobuddy.ui.fragments.addcredits.AddCreditsFragment;
import in.appnow.astrobuddy.utils.FragmentUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by sonu on 10:37, 11/04/19
 * Copyright (c) 2019 . All rights reserved.
 */
public class PromoTemplatePresenter implements BasePresenter {
    private final PromoTemplateView view;
    private final PromoTemplateModel model;
    private final PreferenceManger preferenceManger;
    private CompositeDisposable disposable = new CompositeDisposable();

    public PromoTemplatePresenter(PromoTemplateView view, PromoTemplateModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        this.view = view;
        this.model = model;
        this.preferenceManger = preferenceManger;
    }

    @Override
    public void onCreate() {
        disposable.add(observeAddTopicsButtonClick());
    }

    private Disposable observeAddTopicsButtonClick() {
        return view.observeAddTopicsButtonClick()
                .doOnNext(__ -> ProgressDialogFragment.showProgress(model.getAppCompatActivity().getSupportFragmentManager()))
                .map(validate -> AstroApplication.getInstance().isInternetConnected(true))
                .observeOn(Schedulers.io())
                .switchMap(validate -> {
                    if (validate) {
                        BaseRequestModel baseRequestModel = new BaseRequestModel();
                        baseRequestModel.setUserId(preferenceManger.getUserDetails().getUserProfile().getUserId());
                        return model.getTopics(baseRequestModel);
                    } else {
                        return Observable.just(new MyAccountResponse());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> ProgressDialogFragment.dismissProgress(model.getAppCompatActivity().getSupportFragmentManager()))
                .subscribeWith(new CallbackWrapper<MyAccountResponse>(view, this) {
                    @Override
                    protected void onSuccess(MyAccountResponse data) {
                        if (data != null) {
                            if (!data.isErrorStatus() && data.getAccountDetails() != null) {
                                view.replaceFragment(AddCreditsFragment.newInstance(data.getAccountDetails().getUserTopics()), FragmentUtils.BUY_TOPICS_VIA_PROMO_FRAGMENT);
                            } else {
                                view.replaceFragment(AddCreditsFragment.newInstance(0), FragmentUtils.BUY_TOPICS_VIA_PROMO_FRAGMENT);
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
    }

}

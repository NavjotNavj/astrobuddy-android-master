package in.appnow.astrobuddy.ui.activities.payment_result.mvp;

import in.appnow.astrobuddy.base.BasePresenter;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dialog.UnAuthoriseUserDialog;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.utils.FragmentUtils;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by sonu on 17:29, 07/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class PaymentResultPresenter implements BasePresenter {

    private final PaymentResultView view;
    private final PaymentResultModel model;
    private PreferenceManger preferenceManger;
    private ABDatabase abDatabase;

    private final CompositeDisposable disposable = new CompositeDisposable();

    public PaymentResultPresenter(PaymentResultView view, PaymentResultModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        this.view = view;
        this.model = model;
        this.preferenceManger = preferenceManger;
        this.abDatabase = abDatabase;
    }

    @Override
    public void onCreate() {
        updateResultView();
        disposable.add(observeDoneButtonClick());
    }

    private void updateResultView(){
        view.updateViews(model.getOrderId(),model.getTrackingId(),model.getErrorStatus(),model.getStatus(),model.getStatusMessage());
        view.showHideCallBuddy(model.getPaymentType());
    }

    private Disposable observeDoneButtonClick() {
        return view.observeDoneButton()
                .subscribe(__ -> model.closeActivity());
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

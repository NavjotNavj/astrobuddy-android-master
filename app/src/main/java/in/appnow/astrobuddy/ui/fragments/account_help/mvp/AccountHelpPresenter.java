package in.appnow.astrobuddy.ui.fragments.account_help.mvp;

import android.view.View;

import in.appnow.astrobuddy.base.BasePresenter;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dialog.UnAuthoriseUserDialog;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.rest.CallbackWrapper;
import in.appnow.astrobuddy.rest.response.AccountHelpResponse;
import in.appnow.astrobuddy.utils.FragmentUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by sonu on 16:19, 16/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class AccountHelpPresenter implements BasePresenter {
    private static final String TAG = AccountHelpPresenter.class.getSimpleName();
    private final AccountHelpView view;
    private final AccountHelpModel model;
    private PreferenceManger preferenceManger;
    private ABDatabase abDatabase;

    private final CompositeDisposable disposable = new CompositeDisposable();

    public AccountHelpPresenter(AccountHelpView view, AccountHelpModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        this.view = view;
        this.model = model;
        this.preferenceManger = preferenceManger;
        this.abDatabase = abDatabase;
    }


    @Override
    public void onCreate() {
        disposable.add(getAccountHelpInfo());
    }

    private Disposable getAccountHelpInfo() {
        view.showHideProgressBar(View.VISIBLE);
        return model.getAccountHelpInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> view.showHideProgressBar(View.GONE))
                .subscribeWith(new CallbackWrapper<AccountHelpResponse>(view, this) {
                    @Override
                    protected void onSuccess(AccountHelpResponse accountHelpResponse) {
                        if (accountHelpResponse != null) {
                            view.updateView(accountHelpResponse);
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

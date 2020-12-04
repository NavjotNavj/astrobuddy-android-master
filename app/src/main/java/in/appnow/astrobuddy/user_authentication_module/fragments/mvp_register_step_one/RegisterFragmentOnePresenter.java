package in.appnow.astrobuddy.user_authentication_module.fragments.mvp_register_step_one;

import in.appnow.astrobuddy.base.BasePresenter;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dialog.UnAuthoriseUserDialog;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.utils.FragmentUtils;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Abhishek Thanvi on 10/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class RegisterFragmentOnePresenter implements BasePresenter{

    private final RegisterFragmentOneView registerFragmentOneView;

    private final RegisterFragmentOneModel registerFragmentOneModel;
    private PreferenceManger preferenceManger;
    private ABDatabase abDatabase;

    private final CompositeDisposable disposable = new CompositeDisposable();

    public RegisterFragmentOnePresenter(RegisterFragmentOneView registerFragmentOneView, RegisterFragmentOneModel registerFragmentOneModel, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        this.registerFragmentOneView = registerFragmentOneView;
        this.registerFragmentOneModel = registerFragmentOneModel;
        this.preferenceManger = preferenceManger;
        this.abDatabase = abDatabase;
    }

    @Override
    public void onCreate() {
        disposable.add(observeMaleButton());
        disposable.add(observeFemaleButton());
        disposable.add(observeOtherButton());
        disposable.add(observeDOB());
        disposable.add(observeTimePicker());
        disposable.add(observePlacePicker());

    }

    private Disposable observeMaleButton() {
        return registerFragmentOneView.observeMaleButton()
                .subscribe(data -> {
                    registerFragmentOneView.selectGender(0);
                });

    }

    private Disposable observeFemaleButton() {
        return registerFragmentOneView.observeFemaleButton()
                .subscribe(data -> {
                    registerFragmentOneView.selectGender(1);
                });

    }

    private Disposable observeOtherButton() {
        return registerFragmentOneView.observeOtherButton()
                .subscribe(data -> {
                    registerFragmentOneView.selectGender(2);
                });

    }

    private Disposable observeDOB() {
        return registerFragmentOneView.observeDOBEdt()
                .subscribe(data -> {
                    registerFragmentOneView.showDatePicker();
                });

    }

    private Disposable observeTimePicker() {
        return registerFragmentOneView.observeTimePickEdt()
                .subscribe(data -> {
                    registerFragmentOneView.showTimePickerDialog();
                });

    }

    private Disposable observePlacePicker() {
        return registerFragmentOneView.observePlacePickEdt()
                .subscribe(data -> {
                    registerFragmentOneView.openPlacesPicker();
                });

    }
    @Override
    public void onDestroy() {
        disposable.clear();
    }

    @Override
    public void unAuthorizeUserAccess(String message) {
        UnAuthoriseUserDialog.getInstance().showLogOutDialog(
                registerFragmentOneModel.getAppCompatActivity(),
                message,
                preferenceManger,
                abDatabase,
                new UnAuthoriseUserDialog.OnLogoutListener() {
                    @Override
                    public void OnClickLogOut() {
                        registerFragmentOneModel.showProgressBar();
                    }

                    @Override
                    public void OnLogOut() {
                        registerFragmentOneModel.hideProgressBar();
                        FragmentUtils.onLogoutSuccess(registerFragmentOneModel.getAppCompatActivity());
                    }
                });
    }
}

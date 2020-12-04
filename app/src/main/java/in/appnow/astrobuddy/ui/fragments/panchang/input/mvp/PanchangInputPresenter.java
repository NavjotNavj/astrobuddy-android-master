package in.appnow.astrobuddy.ui.fragments.panchang.input.mvp;

import in.appnow.astrobuddy.base.BasePresenter;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dialog.UnAuthoriseUserDialog;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.utils.FragmentUtils;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by sonu on 11:50, 17/04/19
 * Copyright (c) 2019 . All rights reserved.
 */
public class PanchangInputPresenter implements BasePresenter {
    private final PanchangInputView view;
    private final PanchangInputModel model;
    private final PreferenceManger preferenceManger;
    private ABDatabase abDatabase;
    private CompositeDisposable disposable = new CompositeDisposable();

    public PanchangInputPresenter(PanchangInputView view, PanchangInputModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        this.view = view;
        this.model = model;
        this.preferenceManger = preferenceManger;
        this.abDatabase = abDatabase;
    }

    @Override
    public void onCreate() {
        view.setSavedLocation(preferenceManger);
        disposable.add(observePlacePicker());
        disposable.add(observeDate());
        disposable.add(observeTime());
        disposable.add(observeShowPanchang());
    }

    private Disposable observeShowPanchang() {
        return view.observeShowPanchangButton().subscribe(__ -> {
            view.validateData();
        });
    }

    private Disposable observeDate() {
        return view.observeDate()
                .subscribe(data -> {
                    view.showDatePicker();
                });

    }

    private Disposable observeTime() {
        return view.observeTime()
                .subscribe(data -> {
                    view.showTimePickerDialog();
                });

    }

    private Disposable observePlacePicker() {
        return view.observePlacePick()
                .subscribe(data -> {
                    view.openPlacesPicker();
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

package in.appnow.astrobuddy.ui.fragments.match_making.mvp;

import in.appnow.astrobuddy.base.BasePresenter;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dialog.UnAuthoriseUserDialog;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.utils.FragmentUtils;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by sonu on 12:28, 09/04/19
 * Copyright (c) 2019 . All rights reserved.
 */
public class MatchMakingPresenter implements BasePresenter {
    private final MatchMakingView view;
    private final MatchMakingModel model;
    private final PreferenceManger preferenceManger;
    private ABDatabase abDatabase;
    private CompositeDisposable disposable = new CompositeDisposable();

    public MatchMakingPresenter(MatchMakingView view, MatchMakingModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        this.view = view;
        this.model = model;
        this.preferenceManger = preferenceManger;
        this.abDatabase = abDatabase;
    }

    @Override
    public void onCreate() {
        if (view != null && preferenceManger != null)
            view.autoPopulateUserDetails(preferenceManger.getUserDetails().getUserProfile());
        disposable.add(observeMatchDetails());
        disposable.add(observeDOBMale());
        disposable.add(observeTimePickerMale());
        disposable.add(observePlacePickerMale());
        disposable.add(observeDOBFemale());
        disposable.add(observeTimePickerFemale());
        disposable.add(observePlacePickerFemale());
    }

    private Disposable observeMatchDetails() {
        return view.observeGenerateDetailsButton().subscribe(__ -> {
            view.validateData();
        });
    }

    private Disposable observeDOBMale() {
        return view.observeDOBMale()
                .subscribe(data -> {
                    view.showDatePicker(false);
                });

    }

    private Disposable observeTimePickerMale() {
        return view.observeTOBMale()
                .subscribe(data -> {
                    view.showTimePickerDialog(false);
                });

    }

    private Disposable observePlacePickerMale() {
        return view.observePlacePickMale()
                .subscribe(data -> {
                    view.openPlacesPicker(false);
                });

    }


    private Disposable observeDOBFemale() {
        return view.observeDOBFemale()
                .subscribe(data -> {
                    view.showDatePicker(true);
                });

    }

    private Disposable observeTimePickerFemale() {
        return view.observeTOBFemale()
                .subscribe(data -> {
                    view.showTimePickerDialog(true);
                });

    }

    private Disposable observePlacePickerFemale() {
        return view.observePlacePickFemale()
                .subscribe(data -> {
                    view.openPlacesPicker(true);
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

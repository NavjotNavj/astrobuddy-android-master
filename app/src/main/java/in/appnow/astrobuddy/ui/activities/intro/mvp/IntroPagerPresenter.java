package in.appnow.astrobuddy.ui.activities.intro.mvp;

import in.appnow.astrobuddy.base.BasePresenter;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dialog.UnAuthoriseUserDialog;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.utils.FragmentUtils;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by sonu on 17:17, 23/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class IntroPagerPresenter implements BasePresenter {
    private final IntroPagerView view;
    private final IntroPagerModel model;
    private final PreferenceManger preferenceManger;
    private ABDatabase abDatabase;
    private final CompositeDisposable disposable = new CompositeDisposable();

    public IntroPagerPresenter(IntroPagerView view, IntroPagerModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        this.view = view;
        this.model = model;
        this.preferenceManger = preferenceManger;
        this.abDatabase = abDatabase;
    }

    @Override
    public void onCreate() {
        disposable.add(observeNextButtonClick());
        disposable.add(observeSkipButtonClick());
        getIntroList();
    }

    private Disposable observeSkipButtonClick() {
        return view.observeSkipButton()
                .subscribe(__ -> {
                    onSkipClick();
                });
    }

    private Disposable observeNextButtonClick() {
        return view.observeNextButton()
                .subscribe(__ ->
                {
                    if (view.onNextClick()) {
                        onSkipClick();
                    }
                });
    }

    private void onSkipClick() {
        preferenceManger.putBoolean(PreferenceManger.INTRO_SCREEN, true);
        model.launchActivity(preferenceManger.getUserDetails() != null);
    }

    private void getIntroList() {
        view.setUpIntroList(model.getIntroModel());
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

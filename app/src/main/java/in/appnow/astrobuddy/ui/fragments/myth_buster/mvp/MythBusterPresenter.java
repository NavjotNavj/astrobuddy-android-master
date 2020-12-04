package in.appnow.astrobuddy.ui.fragments.myth_buster.mvp;

import in.appnow.astrobuddy.base.BasePresenter;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dialog.UnAuthoriseUserDialog;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.rest.CallbackWrapper;
import in.appnow.astrobuddy.rest.request.BaseRequestModel;
import in.appnow.astrobuddy.rest.response.MythBuster;
import in.appnow.astrobuddy.rest.response.MythBusterResponse;
import in.appnow.astrobuddy.ui.fragments.myth_buster.mvp.view.MythBusterView;
import in.appnow.astrobuddy.utils.FragmentUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by sonu on 16:04, 23/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class MythBusterPresenter implements BasePresenter {
    private final MythBusterView view;
    private final MythBusterModel model;
    private final PreferenceManger preferenceManger;
    private ABDatabase abDatabase;

    private final CompositeDisposable disposable = new CompositeDisposable();
    private int mythId;
    private boolean isMythVideo;

    public MythBusterPresenter(MythBusterView view, MythBusterModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        this.view = view;
        this.model = model;
        this.preferenceManger = preferenceManger;
        this.abDatabase = abDatabase;
    }

    public PreferenceManger getPreferenceManger() {
        return preferenceManger;
    }

    public void setMythId(int mythId) {
        this.mythId = mythId;
    }

    public void setMythType(boolean isVideo){
        this.isMythVideo = isVideo;
    }

    @Override
    public void onCreate() {
        disposable.add(loadMyths());
        disposable.add(onListClick());
    }

    private Disposable loadMyths() {
        model.showProgressBar();
        BaseRequestModel baseRequestModel = new BaseRequestModel();
        baseRequestModel.setUserId(preferenceManger.getUserDetails().getUserProfile() != null ? preferenceManger.getUserDetails().getUserProfile().getUserId() : "0");
        return model.getMythBusters(baseRequestModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> model.hideProgressBar())
                .subscribeWith(new CallbackWrapper<MythBusterResponse>(view, this) {
                    @Override
                    protected void onSuccess(MythBusterResponse data) {
                        if (data != null) {
                            view.updateView(data);
                            openMyth(data);
                        }
                    }
                });

    }

    private void openMyth(MythBusterResponse data) {
        if (mythId > 0 && data != null) {
            if (!data.isErrorStatus() && data.getMythBuster() != null && data.getMythBuster().size() > 0) {
                for (MythBuster mythBuster : data
                        .getMythBuster()) {
                    if (mythBuster.getMythId() == mythId) {
                        mythId = 0;
                        openMythOnSelection(mythBuster);
                        return;
                    }
                }

            }
        }
    }

    private Disposable onListClick() {
        return view.onListClick()
                .subscribe(this::openMythOnSelection);
    }

    private void openMythOnSelection(MythBuster data) {
        if (data.getMythType().equalsIgnoreCase("VIDEO")) {
            model.openYouTubeActivity(data.getSource());
        } else {
            //open detail screen
            model.replaceMythBusterDetailFragment(data);
        }
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

package in.appnow.astrobuddy.ui.activities.splash.mvp;

import android.Manifest;
import android.content.DialogInterface;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.concurrent.TimeUnit;

import in.appnow.astrobuddy.BuildConfig;
import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.base.BasePresenter;
import in.appnow.astrobuddy.dialog.DialogHelperClass;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.jobs.PostUserStatsDailyJob;
import in.appnow.astrobuddy.jobs.TipOfTheDailyJob;
import in.appnow.astrobuddy.models.ABConfigModel;
import in.appnow.astrobuddy.models.AppUpgradeModel;
import in.appnow.astrobuddy.models.ServerStatusModel;
import in.appnow.astrobuddy.utils.Logger;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by sonu on 12:41, 19/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class SplashPresenter implements BasePresenter {

    private static final long SPLASH_DELAY = 2000;
    private static final String TAG = SplashPresenter.class.getSimpleName();
    private final SplashView view;
    private final SplashModel model;
    private final PreferenceManger preferenceManger;
    private RxPermissions rxPermissions;
    private DatabaseReference myRef;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public SplashPresenter(SplashView view, SplashModel model, PreferenceManger preferenceManger) {
        this.view = view;
        this.model = model;
        this.preferenceManger = preferenceManger;
    }

    @Override
    public void onCreate() {
        //Logger.DebugLog(TAG,"TOKEN: "+preferenceManger.getStringValue(PreferenceManger.FCM_TOKEN));
        this.rxPermissions = new RxPermissions(model.getAppCompatActivity());
        if (!AstroApplication.getInstance().isInternetConnected(true)) {
            model.finishActivity();
            return;
        }
        compositeDisposable.add(checkPhoneStatePermission());
        //compositeDisposable.add(waitAndCloseSplash());
//        preferenceManger.putBoolean(PreferenceManger.SIDE_MENU_HINT,false);
//        preferenceManger.putBoolean(PreferenceManger.CHAT_TAP_HINT,false);
//        preferenceManger.putBoolean(PreferenceManger.CHART_TAP_HINT,false);
//        preferenceManger.putBoolean(PreferenceManger.HOROSCOPE_TAP_HINT,false);
//        preferenceManger.putBoolean(PreferenceManger.MYTH_BUSTER_HINT, false);
//        preferenceManger.putBoolean(PreferenceManger.MY_ACCOUNT_HINT, false);
//        preferenceManger.putBoolean(PreferenceManger.BUY_CREDITS_HINT, false);
//
//        preferenceManger.putBoolean(PreferenceManger.FORECAST_TAP_HINT, false);
//        preferenceManger.putBoolean(PreferenceManger.BUY_CREDITS_BACK_PRESS_HINT, false);
//        preferenceManger.putBoolean(PreferenceManger.MY_ACCOUNT_BACK_PRESS_HINT, false);
//        preferenceManger.putBoolean(PreferenceManger.MYTH_BUSTER_BACK_PRESS_HINT, false);
//        preferenceManger.putBoolean(PreferenceManger.CHART_BACK_PRESS_TAP_HINT, false);
//        preferenceManger.putBoolean(PreferenceManger.CHAT_BACK_PRESS_TAP_HINT, false);
//        preferenceManger.putBoolean(PreferenceManger.HOROSCOPE_BACK_PRESS_TAP_HINT, false);

        //preferenceManger.putString(PreferenceManger.CHAT_QUERY,"");

    }

    private Disposable checkPhoneStatePermission() {
        return rxPermissions.request(Manifest.permission.READ_PHONE_STATE)
                .subscribe(granted -> {
                    if (granted) {
                        onPermissionDone();
                    } else {
                        onPermissionDone();
                    }
                });
    }

    private void onPermissionDone() {
        if (!preferenceManger.getBooleanValue(PreferenceManger.APP_DOWNLOAD_API) && AstroApplication.getInstance().isInternetConnected(false)) {
            model.sendAppDownload();
        }
        if (BuildConfig.DEBUG) {
            //  compositeDisposable.add(getAppVersion());
            checkIfServerIsUp();
           // isIntroScreenShown();
        } else {
            checkIfServerIsUp();
        }
    }

   /* private Disposable getAppVersion() {
        AppVersionRequest request = new AppVersionRequest();
        request.setVersionCode(BuildConfig.VERSION_CODE);
        request.setDeviceType("1");
        if (preferenceManger.getUserDetails() != null) {
            request.setUserId(preferenceManger.getUserDetails().getUserProfile().getUserId());
        } else {
            request.setUserId("0");
        }

        return model.getAppVersion(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new CallbackWrapper<AppVersionResponse>(view, this) {
                    @Override
                    protected void onSuccess(AppVersionResponse data) {
                        if (data.isErrorStatus()) {
                            isIntroScreenShown();
                        } else {
                            if (data.isForceUpgrade()) {
                                model.showAppUpgradeAlertDialog(data.getErrorMsg(), true, (dialogInterface, i) -> isIntroScreenShown());
                            } else if (data.isRecommendUpgrade()) {
                                model.showAppUpgradeAlertDialog(data.getErrorMsg(), false, (dialogInterface, i) -> isIntroScreenShown());
                            } else {
                                isIntroScreenShown();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        isIntroScreenShown();
                    }
                });
    }*/

    private Disposable waitAndCloseSplash() {
        return Observable.timer(SPLASH_DELAY, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(__ -> {
                    isIntroScreenShown();
                });
    }

    private void isIntroScreenShown() {
        if (preferenceManger.getBooleanValue(PreferenceManger.INTRO_SCREEN)) {
            checkIfUserIsLogin();
        } else {
            model.startIntroActivity();
        }
    }

    private void checkIfUserIsLogin() {
        if (preferenceManger.getUserDetails() != null) {

            //the below method is to schedule tip of the day daily job for logged in users if not already started
            if (!preferenceManger.getBooleanValue(PreferenceManger.TOD_DAILY_JOB)) {
                //start daily job for tip of the day
                TipOfTheDailyJob.schedule(view.getContext());
                //update tip of the day daily job preference
                preferenceManger.putBoolean(PreferenceManger.TOD_DAILY_JOB, true);
            }

            if (!preferenceManger.getBooleanValue(PreferenceManger.USER_STATS_DAILY_JOB)) {
                //start daily job for user stats
                PostUserStatsDailyJob.schedule(view.getContext());
                //update user stats daily job preference
                preferenceManger.putBoolean(PreferenceManger.USER_STATS_DAILY_JOB, true);
            }

            model.launchActivity(true);
        } else {
            model.launchActivity(false);
        }
    }

    private void checkIfServerIsUp() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("ab_config");

        // Read from the database
        myRef.addValueEventListener(valueEventListener);
    }


    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            ABConfigModel value = dataSnapshot.getValue(ABConfigModel.class);
            if (value != null) {
                Logger.DebugLog(TAG,"DATA : "+value.toString());
                ServerStatusModel statusModel = value.getServerStatusModel();
                if (statusModel != null && statusModel.isRestrictAccess()) {
                    DialogHelperClass.showMessageOKCancel(model.getAppCompatActivity(), statusModel.getMessage(), "Ok", "", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            model.finishActivity();
                        }
                    }, null);
                } else {
                    AppUpgradeModel upgradeModel = value.getAppUpgradeModel();
                    if (upgradeModel != null) {
                        checkAppVersion(upgradeModel);
                    } else {
                        compositeDisposable.add(waitAndCloseSplash());
                    }
                }
            } else {
                compositeDisposable.add(waitAndCloseSplash());
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Logger.ErrorLog(TAG, "Failed to read value : " + databaseError.toException());
//            Crashlytics.logException(databaseError.toException());
            //compositeDisposable.add(getAppVersion());
            databaseError.toException().printStackTrace();
            compositeDisposable.add(waitAndCloseSplash());
        }
    };


    private void checkAppVersion(AppUpgradeModel upgradeModel) {
        int playStoreVersion = upgradeModel.getProdAppVersion();
        int currentVersion = BuildConfig.VERSION_CODE;
        if (playStoreVersion > currentVersion) {
            boolean isForceUpgrade = upgradeModel.isForceUpgrade();
            boolean isRecommendUpgrade = upgradeModel.isRecommendedUpgrade();
            String message = upgradeModel.getMessage();
            if (isForceUpgrade) {
                model.showAppUpgradeAlertDialog(message, true, null);
            } else if (isRecommendUpgrade) {
                model.showAppUpgradeAlertDialog(message, false, (dialogInterface, i) ->                  compositeDisposable.add(waitAndCloseSplash()));
            } else {
                compositeDisposable.add(waitAndCloseSplash());
            }
        } else {
            compositeDisposable.add(waitAndCloseSplash());
        }
    }


    @Override
    public void onDestroy() {
        if (myRef != null && valueEventListener != null) {
            myRef.removeEventListener(valueEventListener);
        }
        compositeDisposable.clear();
    }

    @Override
    public void unAuthorizeUserAccess(String message) {

    }


}

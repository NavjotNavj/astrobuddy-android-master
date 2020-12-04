package in.appnow.astrobuddy.ui.fragments.home.mvp;

import android.text.TextUtils;
import android.webkit.URLUtil;

import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.base.BasePresenter;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dialog.UnAuthoriseUserDialog;
import in.appnow.astrobuddy.fcm.Config;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.rest.CallbackWrapper;
import in.appnow.astrobuddy.rest.request.BaseRequestModel;
import in.appnow.astrobuddy.rest.request.TipOfTheRequest;
import in.appnow.astrobuddy.rest.response.MyAccountResponse;
import in.appnow.astrobuddy.rest.response.PromoBannerResponse;
import in.appnow.astrobuddy.rest.response.TipOfTheDayResponse;
import in.appnow.astrobuddy.ui.fragments.addcredits.AddCreditsFragment;
import in.appnow.astrobuddy.ui.fragments.contact_us.ContactUsFragment;
import in.appnow.astrobuddy.ui.fragments.myprofile.MyProfileFragment;
import in.appnow.astrobuddy.ui.fragments.promo_template.PromoTemplateFragment;
import in.appnow.astrobuddy.ui.fragments.referral.ReferralFragment;
import in.appnow.astrobuddy.utils.DateUtils;
import in.appnow.astrobuddy.utils.FragmentUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Abhishek Thanvi on 19/03/19.
 * Copyright © 2019 Abhishek Thanvi. All rights reserved.
 */

public class UserHomePresenter implements BasePresenter {

    private final UserHomeView view;
    private final UserHomeModel model;
    private final PreferenceManger preferenceManger;
    private ABDatabase abDatabase;
    private final CompositeDisposable disposable = new CompositeDisposable();

    public UserHomePresenter(UserHomeView view, UserHomeModel model,
                             PreferenceManger preferenceManger, ABDatabase abDatabase) {
        this.view = view;
        this.model = model;
        this.preferenceManger = preferenceManger;
        this.abDatabase = abDatabase;
    }

    @Override
    public void onCreate() {
        try {
            if (AstroApplication.getInstance().isInternetConnected(true)) {
                disposable.add(getTipOfTheDay());
            }
            disposable.add(getTopics());
        } catch (Exception ignored) {

        }
        disposable.add(observeUserIcon());
        disposable.add(observeTopicLabel());
        disposable.add(getPromoBanners());
        disposable.add(observePromoBannerClick());
        view.setUpRecycler(model.getHomeList());
        view.setUserProfile(preferenceManger);
    }

    private Disposable observeTopicLabel() {
        return view.observeTopicLabelClick().subscribe(__ -> {
            preferenceManger.putBoolean(PreferenceManger.BUY_CREDITS_HINT, true);
            view.replaceFragment(AddCreditsFragment.newInstance(view.topicsCount), FragmentUtils.BUY_TOPICS_FRAGMENT);
        });
    }

    private Disposable getTopics() {
        BaseRequestModel baseRequestModel = new BaseRequestModel();
        baseRequestModel.setUserId(preferenceManger.getUserDetails().getUserProfile().getUserId());
        return model.getTopics(baseRequestModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new CallbackWrapper<MyAccountResponse>(view, this) {
                    @Override
                    protected void onSuccess(MyAccountResponse data) {
                        if (data != null) {
                            view.updateTopicsCount(data);
                        }
                    }
                });
    }

    private Disposable getPromoBanners() {
        return model.getPromoBanners()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new CallbackWrapper<PromoBannerResponse>(view, this) {
                    @Override
                    protected void onSuccess(PromoBannerResponse data) {
                        view.populatePromoBanners(data);
                    }
                });
    }

    private Disposable observeUserIcon() {
        return view.userIconClick()
                .subscribe(__ -> {
                    view.replaceFragment(MyProfileFragment.newInstance(), FragmentUtils.MY_PROFILE_HOME_FRAGMENT);
                });
    }

    private Disposable observePromoBannerClick() {
        return view.observePromoBannerClick()
                .subscribe(promoBanner -> {
                    if (TextUtils.isEmpty(promoBanner.getAction()))
                        return;
                    view.insertBannerClicks(promoBanner);
                    if (URLUtil.isValidUrl(promoBanner.getAction())) {
                        model.openLink(promoBanner.getTitle(), promoBanner.getAction());
                    } else {
                        String action = promoBanner.getAction();
                        switch (action) {
                            case Config.HOME_ACTION:
                                view.doReplaceFragment(0);
                                break;
                            case Config.FORECAST_ACTION:
                                view.doReplaceFragment(2);
                                break;
                            case Config.TRANS_ACTION:
                                view.doReplaceFragment(6);
                                break;
                            case Config.MY_PROFILE_ACTION:
                                view.replaceFragment(MyProfileFragment.newInstance(), FragmentUtils.MY_PROFILE_HOME_FRAGMENT);
                                break;
                            case Config.MYTH_BUSTER_ACTION:
                                view.doReplaceFragment(3);
                                break;
                            case Config.MYTH_BUSTER_VIDEO_ACTION:
                                view.doReplaceFragment(4);
                                break;
                            case Config.CHAT_ACTION:
                                view.doReplaceFragment(8);
                                break;
                            case Config.CHAT_HISTORY_ACTION:
                                view.doReplaceFragment(8);
                                break;
                            case Config.CONTACT_US_ACTION:
                                view.replaceFragment(ContactUsFragment.newInstance(), FragmentUtils.CONTACT_US_HOME_FRAGMENT);
                                break;
                            case Config.CHART_ACTION:
                                view.doReplaceFragment(0);
                                break;
                            case Config.REFER_ACTION:
                                view.replaceFragment(ReferralFragment.newInstance(), FragmentUtils.REFERRAL_CODE_FRAGMENT);
                                break;
                            case Config.ADD_TOPICS_ACTION:
                                preferenceManger.putPromoBanner(promoBanner);
                                view.addFragment(PromoTemplateFragment.newInstance(promoBanner), FragmentUtils.PROMO_TEMPLATE_FRAGMENT);
                                break;
                            case Config.PANCHANG_ACTION:
                                view.doReplaceFragment(5);
                                break;
                            case Config.MATCH_MAKING_ACTION:
                                view.doReplaceFragment(1);
                                break;
                        }
                    }

                });
    }

    private Disposable getTipOfTheDay() {
        TipOfTheRequest request = new TipOfTheRequest();
        request.setStarSign(preferenceManger.getUserDetails().getUserProfile().getStarSign());
        request.setCurrentDate(DateUtils.getDate(System.currentTimeMillis(), DateUtils.SERVER_ONLY_DATE_FORMAT));
        return model.getTipOfTheDay(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new CallbackWrapper<TipOfTheDayResponse>(view, this) {
                    @Override
                    protected void onSuccess(TipOfTheDayResponse data) {
                        if (data != null) {
                            if (!data.isErrorStatus()) {
                                preferenceManger.putString(PreferenceManger.TIP_OF_THE_DAY, data.getTipOfTheDay());
                                loadTipOfTheDay();
                            }
                        }
                    }
                });
    }

    private void loadTipOfTheDay() {
        String tipOfTheDay = preferenceManger.getStringValue(PreferenceManger.TIP_OF_THE_DAY);
        if (!TextUtils.isEmpty(tipOfTheDay))
            view.updateTipOfTheDay(tipOfTheDay);
    }

    @Override
    public void onDestroy() {
        if (view != null) {
            view.cancelAutoSwipeTimer();
            view.hideTooltipMenu();
        }
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

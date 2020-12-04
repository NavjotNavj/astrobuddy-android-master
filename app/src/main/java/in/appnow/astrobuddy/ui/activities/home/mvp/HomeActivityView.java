package in.appnow.astrobuddy.ui.activities.home.mvp;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseView;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dialog.DialogHelperClass;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.models.SunSignModel;
import in.appnow.astrobuddy.ui.activities.main.mvp.MainActivityView;
import in.appnow.astrobuddy.ui.fragments.call_plans.CallPlansFragment;
import in.appnow.astrobuddy.ui.fragments.chat_topics.ChatTopicsFragment;
import in.appnow.astrobuddy.ui.fragments.contact_us.ContactUsFragment;
import in.appnow.astrobuddy.ui.fragments.home.UserHomeFragment;
import in.appnow.astrobuddy.ui.fragments.horoscope_detail.pager.HoroscopeDetailPagerFragment;
import in.appnow.astrobuddy.ui.fragments.more.MoreSettingsFragment;
import in.appnow.astrobuddy.ui.fragments.myaccount.MyAccountFragment;
import in.appnow.astrobuddy.ui.fragments.myprofile.MyProfileFragment;
import in.appnow.astrobuddy.ui.fragments.myth_buster.MythBusterFragment;
import in.appnow.astrobuddy.ui.fragments.referral.ReferralFragment;
import in.appnow.astrobuddy.ui.fragments.your_chart.HoroscopeChartPagerFragment;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.ImageUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import in.appnow.astrobuddy.utils.VectorUtils;
import io.reactivex.Observable;

/**
 * Created by Abhishek Thanvi on 19/03/19.
 * Copyright © 2019 Abhishek Thanvi. All rights reserved.
 */

public class HomeActivityView extends BaseViewClass implements BaseView {

    private final PreferenceManger preferenceManger;

    private MainActivityView.LogoutListener logoutListener;

    private final AppCompatActivity appCompatActivity;
    private FragmentManager fragmentManager;
    private final ABDatabase abDatabase;

    @BindView(R.id.background_image)
    ImageView backgroundImageView;

    @BindView(R.id.action_home)
    TextView actionHome;
    @BindView(R.id.action_chat)
    TextView actionChat;
    @BindView(R.id.action_call)
    TextView actionCall;
    @BindView(R.id.action_refer)
    TextView actionRefer;
    @BindView(R.id.action_more)
    TextView actionMore;

    @BindView(R.id.bottom_navigation)
    LinearLayout bottomView;

    @BindString(R.string.unknown_error)
    String unknownErrorString;

    @BindView(R.id.buy_credits_button)
    ImageView buyCreditsButton;

    @BindView(R.id.main_toolbar)
    public Toolbar toolbar;

    public HomeActivityView(@NonNull AppCompatActivity appCompatActivity, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        super(appCompatActivity);
        this.appCompatActivity = appCompatActivity;
        this.preferenceManger = preferenceManger;
        this.abDatabase = abDatabase;
        inflate(appCompatActivity, R.layout.activity_home, this);
        ButterKnife.bind(this);
        ImageUtils.setBackgroundImage(appCompatActivity, backgroundImageView);
        fragmentManager = appCompatActivity.getSupportFragmentManager();
        appCompatActivity.setSupportActionBar(toolbar);

        VectorUtils.setVectorCompoundDrawable(actionHome, appCompatActivity, 0, R.drawable.ic_home_black_24dp, 0, 0, R.color.white);
        VectorUtils.setVectorCompoundDrawable(actionChat, appCompatActivity, 0, R.drawable.ic_chat_black_24dp, 0, 0, R.color.white);
        VectorUtils.setVectorCompoundDrawable(actionCall, appCompatActivity, 0, R.drawable.ic_call_black_24dp, 0, 0, R.color.white);
        VectorUtils.setVectorCompoundDrawable(actionRefer, appCompatActivity, 0, R.drawable.ic_share_black_24dp, 0, 0, R.color.white);
        VectorUtils.setVectorCompoundDrawable(actionMore, appCompatActivity, 0, R.drawable.ic_more_horiz_black_24dp, 0, 0, R.color.white);


        // BottomNavigationViewHelper.removeShiftMode(bottomNavigation);
        replaceFragment(UserHomeFragment.newInstance(), FragmentUtils.USER_HOME_FRAGMENT);
    }

    public void openUserForecast() {
        try {
            if (preferenceManger != null && preferenceManger.getUserDetails().getUserProfile() != null) {
                String starSign = preferenceManger.getUserDetails().getUserProfile().getStarSign();
                if (!TextUtils.isEmpty(starSign)) {
                    List<SunSignModel> signModelList = SunSignModel.getSunSignList(getContext());
                    for (int i = 0; i < signModelList.size(); i++) {
                        if (signModelList.get(i).getSunSignName().equalsIgnoreCase(starSign)) {
                            FragmentUtils.onChangeFragment(fragmentManager, R.id.container_view, HoroscopeDetailPagerFragment.newInstance(i), FragmentUtils.HOROSCOPE_DETAIL_FRAGMENT);
                            return;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.action_home, R.id.action_chat, R.id.action_call, R.id.action_refer, R.id.action_more})
    void handleClickEvent(View view) {
        switch (view.getId()) {
            case R.id.action_home:
                replaceFragment(UserHomeFragment.newInstance(), FragmentUtils.USER_HOME_FRAGMENT);
                break;
            case R.id.action_chat:
                replaceChatTopicsFragment();
                break;
            case R.id.action_call:
                replaceTalkAstroBuddyFragment();
                break;
            case R.id.action_refer:
                replaceFragment(ReferralFragment.newInstance(), FragmentUtils.REFERRAL_CODE_FRAGMENT);
                break;
            case R.id.action_more:
                replaceFragment(MoreSettingsFragment.newInstance(), FragmentUtils.MORE_FRAGMENT);
                break;
        }
    }


    @Override
    public void onUnknownError(String error) {
        ToastUtils.shortToast(error);
    }

    @Override
    public void onTimeout() {
        ToastUtils.shortToast(unknownErrorString);
    }

    @Override
    public void onNetworkError() {
        ToastUtils.shortToast(unknownErrorString);
    }

    @Override
    public boolean isNetworkConnected() {
        return false;
    }

    @Override
    public void onConnectionError() {
        ToastUtils.shortToast(unknownErrorString);
    }


    public Observable<Object> observeBuyCreditsButton() {
        return RxView.clicks(buyCreditsButton);
    }

    public void enableViews(boolean enable) {
        try {
            ActionBar actionBar = appCompatActivity.getSupportActionBar();
            if (enable) {
                //  bottomView.setVisibility(GONE);
                // Show back button
                if (actionBar != null)
                    actionBar.setDisplayHomeAsUpEnabled(true);

            } else {
                //  bottomView.setVisibility(VISIBLE);
                // Remove back button
                if (actionBar != null)
                    actionBar.setDisplayHomeAsUpEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setToolbarTitle(String title) {
        try {
            if (toolbar == null || TextUtils.isEmpty(title))
                return;
            toolbar.setTitle(title);
        } catch (Exception ignored) {

        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void showHideBuyCreditsButton(int visibility, boolean show) {
        buyCreditsButton.setVisibility(visibility);
    }

    public void replaceChatTopicsFragment() {
        FragmentUtils.onChangeFragment(appCompatActivity.getSupportFragmentManager(), R.id.container_view, ChatTopicsFragment.newInstance(true), FragmentUtils.CHAT_TOPICS_FRAGMENT);
    }

    public void replaceTalkAstroBuddyFragment() {
        // AppUtils.openWebLink(appCompatActivity,"https://www.astrobuddy.guru/schedual-call.php");
        FragmentUtils.onChangeFragment(appCompatActivity.getSupportFragmentManager(), R.id.container_view, CallPlansFragment.newInstance(), FragmentUtils.CALL_PLAN_FRAGMENT);
        //CommonActivity.openCommonActivity(appCompatActivity, "Talk to AstroBuddy", FragmentUtils.TALK_FRAGMENT);
    }

    public void replaceFragment(Fragment fragment, String fragmentTag) {
        FragmentUtils.onChangeFragment(fragmentManager, R.id.container_view, fragment, fragmentTag);
    }

    public void setLogoutListener(MainActivityView.LogoutListener logoutListener) {
        this.logoutListener = logoutListener;
    }


    public void doReplaceFragment(int position) {
        switch (position) {
            case 0:
                //Home Fragment
                replaceFragment(UserHomeFragment.newInstance(), FragmentUtils.USER_HOME_FRAGMENT);
                break;
            case 1:
                //AstroBuddy Chat
                replaceFragment(ChatTopicsFragment.newInstance(false), FragmentUtils.CHAT_TOPICS_FRAGMENT);
                break;
            case 2:
                //Myth Buster
                replaceFragment(MythBusterFragment.newInstance(0, false), FragmentUtils.MYTH_BUSTER_FRAGMENT);
                break;
            case 3:
                //My Chart
                replaceFragment(HoroscopeChartPagerFragment.newInstance(), FragmentUtils.YOUR_CHART_FRAGMENT);
                break;
            case 4:
                //My Account
                replaceFragment(MyAccountFragment.newInstance(), FragmentUtils.MY_ACCOUNT_FRAGMENT);

                break;

            /*case 4:
                //your chart
                break;*/
            case 5:
                //My Profile
                replaceFragment(MyProfileFragment.newInstance(), FragmentUtils.MY_PROFILE_FRAGMENT);
                break;
            case 6:
                //referral view
                replaceFragment(ReferralFragment.newInstance(), FragmentUtils.REFERRAL_CODE_FRAGMENT);
                break;
            case 7:
                //other services
                replaceFragment(ContactUsFragment.newInstance(), FragmentUtils.CONTACT_US_FRAGMENT);
                //AppUtils.launchCustomTabUrl(appCompatActivity, serviceUrl);
                break;
            case 8:
                //logout
                DialogHelperClass.showMessageOKCancel(appCompatActivity, "Are you sure you want to Logout?", "Logout", "Cancel", (dialogInterface, i) -> {
                    if (logoutListener != null) {
                        logoutListener.onDoLogout();
                    } else {
                        ToastUtils.shortToast("Oops!! Unknown error occurred.");
                    }
                    //doLogout();
                }, (dialogInterface, i) -> {

                });
                break;
        }
    }

    public void hideBottomBar(boolean isHide) {
        if (bottomView != null) {
            bottomView.setVisibility(isHide ? View.GONE : View.VISIBLE);
        }
    }
}

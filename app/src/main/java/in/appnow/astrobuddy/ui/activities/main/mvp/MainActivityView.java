package in.appnow.astrobuddy.ui.activities.main.mvp;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.google.android.material.navigation.NavigationView;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import in.appnow.astrobuddy.BuildConfig;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.adapters.NavigationListAdapter;
import in.appnow.astrobuddy.base.BaseView;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dialog.DialogHelperClass;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.interfaces.OnDrawerOpenListener;
import in.appnow.astrobuddy.models.NavigationModel;
import in.appnow.astrobuddy.rest.response.UserProfile;
import in.appnow.astrobuddy.ui.fragments.chat_topics.ChatTopicsFragment;
import in.appnow.astrobuddy.ui.fragments.contact_us.ContactUsFragment;
import in.appnow.astrobuddy.ui.fragments.myaccount.MyAccountFragment;
import in.appnow.astrobuddy.ui.fragments.myprofile.MyProfileFragment;
import in.appnow.astrobuddy.ui.fragments.myth_buster.MythBusterFragment;
import in.appnow.astrobuddy.ui.fragments.referral.ReferralFragment;
import in.appnow.astrobuddy.ui.fragments.your_chart.HoroscopeChartPagerFragment;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.ImageUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;
import it.sephiroth.android.library.tooltip.Tooltip;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

/**
 * Created by sonu on 12:59, 12/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class MainActivityView extends BaseViewClass implements BaseView, AdapterView.OnItemClickListener {
    private ActionBarDrawerToggle toggle;
    @BindView(R.id.main_toolbar)
    Toolbar toolbar;
    @BindView(R.id.am_drawer)
    DrawerLayout drawerLayout;
    @BindView(R.id.am_navigation)
    NavigationView navigationView;
    @BindView(R.id.side_menu_list_view)
    ListView sideMenuListView;
    @BindView(R.id.nav_header_image_view)
    CircleImageView userProfileImageView;
    @BindView(R.id.nav_user_name_label)
    TextView userNameLabel;
    @BindView(R.id.background_image)
    ImageView backgroundImageView;
    @BindView(R.id.app_version_label)
    TextView appVersionLabel;
    @BindString(R.string.app_url)
    String serviceUrl;
    @BindString(R.string.action_other_services)
    String otherServicesTitle;
    @BindView(R.id.buy_credits_button)
    ImageView buyCreditsButton;

    @BindString(R.string.unknown_error)
    String unknownErrorString;
    @BindString(R.string.action_talk)
    String talkToGuruji;

    private Tooltip.TooltipView tooltip;

    private LogoutListener logoutListener;

    private final AppCompatActivity appCompatActivity;
    private FragmentManager fragmentManager;
    private boolean mToolBarNavigationListenerIsRegistered = false;

    private final PreferenceManger preferenceManger;
    private final ABDatabase abDatabase;

    private final NavigationListAdapter adapter = new NavigationListAdapter(getContext());

    public MainActivityView(@NonNull AppCompatActivity appCompatActivity, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        super(appCompatActivity);
        this.appCompatActivity = appCompatActivity;
        this.preferenceManger = preferenceManger;
        this.abDatabase = abDatabase;
        inflate(appCompatActivity, R.layout.activity_main, this);
        ButterKnife.bind(this);
        ImageUtils.setBackgroundImage(appCompatActivity, backgroundImageView);
        fragmentManager = appCompatActivity.getSupportFragmentManager();
        setUpNavigationView();
        appCompatActivity.getSupportActionBar().setTitle("ASTROBUDDY");
        //doReplaceFragment(0);

        sideMenuListView.setOnItemClickListener(this);

        appVersionLabel.setText("Version : " + BuildConfig.VERSION_NAME);
    }

    public void setLogoutListener(LogoutListener logoutListener) {
        this.logoutListener = logoutListener;
    }

    private void setUpNavigationView() {
        appCompatActivity.setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(appCompatActivity, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //navigationView.setNavigationItemSelectedListener(this);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                checkIfNavigationMythBusterHintShown();
                Fragment horoscopeFragment = appCompatActivity.getSupportFragmentManager().findFragmentByTag(FragmentUtils.HOROSCOPE_FRAGMENT);
                if (horoscopeFragment != null) {
                    ((OnDrawerOpenListener) horoscopeFragment).onDrawerOpen();
                }
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                checkIfBuyCreditsHintShown();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        sideMenuListView.setAdapter(adapter);
    }

    public void setUpNavigationList(List<NavigationModel> navigationModelList) {
        adapter.swapData(navigationModelList);
    }

    public void doReplaceFragment(int position) {
        isDrawerOpen();
        updateAdapterSelection(position);
        switch (position) {
            case 0:
                //Horoscope
                //   replaceFragment(HoroScopeFragment.newInstance(false), FragmentUtils.HOROSCOPE_FRAGMENT);
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

    public void replaceFragment(Fragment fragment, String fragmentTag) {
        FragmentUtils.onChangeFragment(fragmentManager, R.id.container_view, fragment, fragmentTag);
    }


    public void updateAdapterSelection(int position) {
        adapter.setSelectedPosition(position);
    }

    public boolean isDrawerOpen() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

    public void setUpNavigationHeaderView(UserProfile profile) {
        try {
            if (profile == null || userProfileImageView == null || userNameLabel == null)
                return;
            ImageUtils.loadImageUrl(appCompatActivity, userProfileImageView, R.drawable.icon_default_profile, profile.getProfileImage());
            if (TextUtils.isEmpty(profile.getFullName())) {
                userNameLabel.setText("Hello Guest!!");
            } else {
                userNameLabel.setText(profile.getFullName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void enableViews(boolean enable) {
        try {
            ActionBar actionBar = appCompatActivity.getSupportActionBar();

            // To keep states of ActionBar and ActionBarDrawerToggle synchronized,
            // when you enable on one, you disable on the other.
            // And as you may notice, the order for this operation is disable first, then enable - VERY VERY IMPORTANT.
            if (enable) {
                // Remove hamburger
                toggle.setDrawerIndicatorEnabled(false);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                // Show back button
                if (actionBar != null)
                    actionBar.setDisplayHomeAsUpEnabled(true);
                // when DrawerToggle is disabled i.e. setDrawerIndicatorEnabled(false), navigation icon
                // clicks are disabled i.e. the UP button will not work.
                // We need to add a listener, as in below, so DrawerToggle will forward
                // click events to this listener.
                if (!mToolBarNavigationListenerIsRegistered) {
                    toggle.setToolbarNavigationClickListener(v -> {
                        // Doesn't have to be onBackPressed
                        appCompatActivity.onBackPressed();
                    });

                    mToolBarNavigationListenerIsRegistered = true;
                }

            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                // Remove back button
                if (actionBar != null)
                    actionBar.setDisplayHomeAsUpEnabled(false);
                // Show hamburger
                toggle.setDrawerIndicatorEnabled(true);
                // Remove the/any drawer toggle listener
                toggle.setToolbarNavigationClickListener(null);
                mToolBarNavigationListenerIsRegistered = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // So, one may think "Hmm why not simplify to:
        // .....
        // getSupportActionBar().setDisplayHomeAsUpEnabled(enable);
        // mDrawer.setDrawerIndicatorEnabled(!enable);
        // ......
        // To re-iterate, the order in which you enable and disable views IS important #dontSimplify.
    }

    public void setToolbarTitle(String title) {
        try {
            if (toolbar == null || TextUtils.isEmpty(title))
                return;
            toolbar.setTitle(title);
        } catch (Exception ignored) {

        }
    }


    public String getServiceUrl() {
        return serviceUrl;
    }

    public String getOtherServicesTitle() {
        return otherServicesTitle;
    }

    public void showHideBuyCreditsButton(int visibility, boolean show) {
        buyCreditsButton.setVisibility(visibility);
        if (visibility == View.VISIBLE && show) {
            showBuyCreditsTooltip();
        } else {
            hideTooltipMenu();
        }
    }

    public void showBuyCreditsTooltip() {
        if (buyCreditsButton == null || buyCreditsButton.getVisibility() == View.GONE || preferenceManger == null)
            return;
        if (preferenceManger.getBooleanValue(PreferenceManger.BUY_CREDITS_HINT) && preferenceManger.getBooleanValue(PreferenceManger.CHAT_TAP_HINT)) {
            if (appCompatActivity.getSupportFragmentManager().findFragmentByTag(FragmentUtils.HOROSCOPE_FRAGMENT) != null && tooltip == null) {
                tooltip = Tooltip.make(appCompatActivity,
                        new Tooltip.Builder(101)
                                .anchor(buyCreditsButton, Tooltip.Gravity.BOTTOM)
                                .closePolicy(new Tooltip.ClosePolicy()
                                        .insidePolicy(true, false)
                                        .outsidePolicy(false, false), 5000)
                                .activateDelay(800)
                                .withStyleId(R.style.ToolTipLayoutCustomStyle)
                                .showDelay(300)
                                .text("Buy Topics/Plan")
                                .maxWidth(500)
                                .withArrow(true)
                                .withOverlay(true).build());
                tooltip.show();
            }

            //.typeface(mYourCustomFont)
            //.floatingAnimation(Tooltip.AnimationBuilder.DEFAULT)
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        doReplaceFragment(i);
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

    public void hideTooltipMenu() {
        if (tooltip != null) {
            tooltip.hide();
            tooltip = null;
        }
    }

    /* Tutorial Tap Prompt  */
    public void showNavigationMenuPrompt(int childPosition) {
        MaterialTapTargetPrompt.Builder tapTargetPromptBuilder = new MaterialTapTargetPrompt.Builder(appCompatActivity)
                .setPrimaryText("Menu")
                .setSecondaryText("Click here to see more options.")
                .setAutoDismiss(false)
                .setBackgroundColour(appCompatActivity.getResources().getColor(R.color.colorAccent))
                .setAutoFinish(false)
                .setCaptureTouchEventOutsidePrompt(true)
                .setIconDrawableColourFilter(appCompatActivity.getResources().getColor(R.color.colorPrimary))
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                .setIcon(R.drawable.ic_menu);
        tapTargetPromptBuilder.setTarget(toolbar.getChildAt(childPosition));

        tapTargetPromptBuilder.setPromptStateChangeListener((prompt, state) -> {
            if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                prompt.finish();
            }
        });
        tapTargetPromptBuilder.show();
    }

    private void checkIfNavigationMyAccountHintShown() {
        if (!preferenceManger.getBooleanValue(PreferenceManger.MY_ACCOUNT_HINT)) {
            showNavigationMyAccountPrompt();
            preferenceManger.putBoolean(PreferenceManger.MY_ACCOUNT_HINT, true);
        } else {
            checkIfNavigationForecastHintShown();
        }
    }

    private void checkIfNavigationMythBusterHintShown() {
        if (preferenceManger.getBooleanValue(PreferenceManger.MYTH_BUSTER_HINT)) {
            checkIfNavigationMyAccountHintShown();
        } else {
            if (preferenceManger.getBooleanValue(PreferenceManger.SIDE_MENU_HINT)) {
                showNavigationMythBusterPrompt();
                preferenceManger.putBoolean(PreferenceManger.MYTH_BUSTER_HINT, true);
            }
        }
    }

    private void showNavigationMythBusterPrompt() {
        new MaterialTapTargetPrompt.Builder(appCompatActivity)
                .setTarget(sideMenuListView.getChildAt(2))
                .setPrimaryText("Myth Buster")
                .setSecondaryText("Check frequent articles and videos busting superstitions and myths.")
                .setPromptFocal(new RectanglePromptFocal())
                .setBackgroundColour(appCompatActivity.getResources().getColor(R.color.colorAccent))
                .setPromptStateChangeListener((prompt, state) -> {
                    if (state == MaterialTapTargetPrompt.STATE_DISMISSED) {
                        // prompt.finish();
                        checkIfNavigationMyAccountHintShown();
                    }
                })
                .show();
    }

    private void checkIfNavigationForecastHintShown() {
        if (preferenceManger.getBooleanValue(PreferenceManger.MY_ACCOUNT_HINT) && !preferenceManger.getBooleanValue(PreferenceManger.FORECAST_TAP_HINT)) {
            showNavigationForecastPrompt();
            preferenceManger.putBoolean(PreferenceManger.FORECAST_TAP_HINT, true);
        }
    }

    private void showNavigationForecastPrompt() {
        new MaterialTapTargetPrompt.Builder(appCompatActivity)
                .setTarget(sideMenuListView.getChildAt(0))
                .setPrimaryText("Home")
                .setSecondaryText("Go to home.")
                .setPromptFocal(new RectanglePromptFocal())
                .setBackgroundColour(appCompatActivity.getResources().getColor(R.color.colorAccent))
                .setPromptStateChangeListener((prompt, state) -> {
                    if (state == MaterialTapTargetPrompt.STATE_DISMISSED) {
                        prompt.finish();
                    }
                })
                .show();
    }

    private void showNavigationMyAccountPrompt() {
        new MaterialTapTargetPrompt.Builder(appCompatActivity)
                .setTarget(sideMenuListView.getChildAt(4))
                .setPrimaryText("My Account")
                .setBackgroundColour(appCompatActivity.getResources().getColor(R.color.colorAccent))
                .setSecondaryText("Add credits, change plan and check transaction history.")
                .setPromptFocal(new RectanglePromptFocal())
                .setPromptStateChangeListener((prompt, state) -> {
                    if (state == MaterialTapTargetPrompt.STATE_DISMISSED) {
                        // prompt.finish();
                        checkIfNavigationForecastHintShown();
                    }
                })
                .show();
    }

    private void checkIfBuyCreditsHintShown() {
        if (preferenceManger.getBooleanValue(PreferenceManger.FORECAST_TAP_HINT) && !preferenceManger.getBooleanValue(PreferenceManger.BUY_CREDITS_HINT)) {
            if (appCompatActivity.getSupportFragmentManager().findFragmentByTag(FragmentUtils.HOROSCOPE_FRAGMENT) != null) {
                showBuyCreditsPrompt();
                preferenceManger.putBoolean(PreferenceManger.BUY_CREDITS_HINT, true);
            }
        }
    }

    private void showBuyCreditsPrompt() {
        new MaterialTapTargetPrompt.Builder(appCompatActivity)
                .setTarget(buyCreditsButton)
                .setPrimaryText("Buy Topics/Plan")
                .setSecondaryText("Click here to buy chat topics.")
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                .setBackgroundColour(appCompatActivity.getResources().getColor(R.color.colorAccent))
                .setIconDrawableColourFilter(appCompatActivity.getResources().getColor(R.color.colorPrimary))
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                .setIcon(R.drawable.ic_wallet)
                .setCaptureTouchEventOutsidePrompt(true)
                .setPromptStateChangeListener((prompt, state) -> {
                    if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                        prompt.finish();
                    }
                })
                .show();
    }

    public void showBackButtonPrompt(int iconName) {
        MaterialTapTargetPrompt.Builder tapTargetPromptBuilder = new MaterialTapTargetPrompt.Builder(appCompatActivity)
                .setPrimaryText("Back")
                .setSecondaryText("Go back")
                .setBackgroundColour(appCompatActivity.getResources().getColor(R.color.colorAccent))
                .setCaptureTouchEventOutsidePrompt(true)
                .setIconDrawableColourFilter(appCompatActivity.getResources().getColor(R.color.colorPrimary))
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                .setIcon(iconName);
        tapTargetPromptBuilder.setTarget(toolbar.getChildAt(3));

        tapTargetPromptBuilder.setPromptStateChangeListener((prompt, state) -> {
            if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                prompt.finish();
            }
        });
        tapTargetPromptBuilder.show();
    }

    public interface LogoutListener {
        void onDoLogout();
    }
}

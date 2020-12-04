package in.appnow.astrobuddy.ui.activities.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import java.util.List;

import javax.inject.Inject;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.app.AstroAppConstants;
import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.base.BaseActivity;
import in.appnow.astrobuddy.conversation_module.chat_history.ChatHistoryFragment;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dialog.DialogHelperClass;
import in.appnow.astrobuddy.fcm.Config;
import in.appnow.astrobuddy.fcm.NotificationUtils;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.interfaces.OnPaymentListener;
import in.appnow.astrobuddy.interfaces.OnToolbarListener;
import in.appnow.astrobuddy.jobs.TrackNotificationClickJob;
import in.appnow.astrobuddy.models.PendingFeedbackModel;
import in.appnow.astrobuddy.services.MyIntentService;
import in.appnow.astrobuddy.ui.activities.home.dagger.DaggerHomeActivityComponent;
import in.appnow.astrobuddy.ui.activities.home.dagger.HomeActivityComponent;
import in.appnow.astrobuddy.ui.activities.home.dagger.HomeActivityModule;
import in.appnow.astrobuddy.ui.activities.home.mvp.HomeActivityPresenter;
import in.appnow.astrobuddy.ui.activities.home.mvp.HomeActivityView;
import in.appnow.astrobuddy.ui.fragments.addcredits.AddCreditsFragment;
import in.appnow.astrobuddy.ui.fragments.chat_topics.ChatTopicsFragment;
import in.appnow.astrobuddy.ui.fragments.home.UserHomeFragment;
import in.appnow.astrobuddy.ui.fragments.horoscope.HoroScopeFragment;
import in.appnow.astrobuddy.ui.fragments.horoscope.mvp.HoroscopePresenter;
import in.appnow.astrobuddy.ui.fragments.match_making.MatchMakingFragment;
import in.appnow.astrobuddy.ui.fragments.more.MoreSettingsFragment;
import in.appnow.astrobuddy.ui.fragments.myaccount.MyAccountFragment;
import in.appnow.astrobuddy.ui.fragments.myprofile.MyProfileFragment;
import in.appnow.astrobuddy.ui.fragments.myprofile.mvp.MyProfilePresenter;
import in.appnow.astrobuddy.ui.fragments.myth_buster.MythBusterFragment;
import in.appnow.astrobuddy.ui.fragments.panchang.input.PanchangInputFragment;
import in.appnow.astrobuddy.ui.fragments.promo_template.PromoTemplateFragment;
import in.appnow.astrobuddy.ui.fragments.upgrade_plan.mvp.UpgradePlanPresenter;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.Logger;

import static in.appnow.astrobuddy.ui.fragments.upgrade_plan.mvp.UpgradePlanModel.RAZOR_PAY_SUBSCRIPTION_REQUEST_CODE;

/**
 * Created by Abhishek Thanvi on 19/03/19.
 * Copyright © 2019 Abhishek Thanvi. All rights reserved.
 */

public class HomeActivity extends BaseActivity implements OnToolbarListener, MyProfilePresenter.OnImageUploadListener, PaymentResultWithDataListener, HoroscopePresenter.OnHoroscopeTutorialListener {

    private static final String TAG = HomeActivity.class.getSimpleName();

    @Inject
    HomeActivityView view;
    @Inject
    HomeActivityPresenter presenter;

    private HomeActivityComponent component;

    @Inject
    PreferenceManger preferenceManger;

    @Inject
    ABDatabase abDatabase;


    public static void startMainActivity(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        ((AppCompatActivity) context).finish();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        component = DaggerHomeActivityComponent.builder()
                .appComponent(AstroApplication.get(this).component())
                .homeActivityModule(new HomeActivityModule(this))
                .build();
        component.inject(this);
        // ENABLE TO TEST TIP OF THE DAY
        /*Intent resultIntent = new Intent(this, HomeActivity.class);
        resultIntent.putExtra(Config.MESSAGE_TYPE, Config.TOD_APP_NOTIFICATION);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        NotificationUtils notificationUtils = new NotificationUtils(this);
        notificationUtils.showNotificationMessage("Today's Tip & Forecast", "Find your tip of the day and daily forecast. Click here.", resultIntent, Config.TOD_NOTIFICATION_ID, R.drawable.ic_launcher_notification, NotificationUtils.GENERAL_UPDATES_CHANNEL, R.mipmap.ic_tod);*/

        /**
         * Preload payment resources
         */
        try {
            Checkout.preload(getApplicationContext());
        } catch (Exception ignored) {

        }

        setContentView(view);
        handleNotificationClick();
        handleDeepLinkData();

        presenter.onCreate();
    }


    public HomeActivityComponent getComponent() {
        return component;
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    public Toolbar getToolbar() {
        return view.getToolbar();
    }

    @Override
    public void onBackPressed() {
        onBackButtonPressed();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragmentList) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        switch (requestCode) {
            case RAZOR_PAY_SUBSCRIPTION_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    if (view != null) {
                        view.replaceFragment(UserHomeFragment.newInstance(), FragmentUtils.USER_HOME_FRAGMENT);
                    }
                }
                break;
        }
    }


    private void onBackButtonPressed() {
        Fragment addCreditFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.ADD_CREDIT_FRAGMENT);
        Fragment upgradePlanFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.UPGRADE_PLAN_FRAGMENT);
        Fragment callPlanFragment =
                getSupportFragmentManager().findFragmentByTag(FragmentUtils.CALL_PLAN_FRAGMENT);
        Fragment changePasswordFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.CHANGE_PASSWORD_FRAGMENT);
        Fragment accountHelpFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.ACCOUNT_HELP_FRAGMENT);
        Fragment chatHistoryFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.CHAT_HISTORY_FRAGMENT);
        Fragment singleChatHistoryFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.SINGLE_CHAT_HISTORY_FRAGMENT);
        Fragment mythBusterDetailFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.MYTH_BUSTER_DETAIL_FRAGMENT);
        Fragment upgradePlanNewFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.UPGRADE_PLAN_NEW_FRAGMENT);
        Fragment buyTopicsViaPromoFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.BUY_TOPICS_VIA_PROMO_FRAGMENT);
        Fragment chatTopicsFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.CHAT_TOPICS_FORECAST_FRAGMENT);
        Fragment panchangFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.PANCHANG_FRAGMENT);
        Fragment callPlanHistoryFragment = getSupportFragmentManager().
                findFragmentByTag(FragmentUtils.CALL_PLAN_HISTORY_FRAGMENT);

        Fragment myProfileFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.MY_PROFILE_FRAGMENT);
        Fragment contactFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.CONTACT_US_FRAGMENT);
        Fragment referralFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.REFERRAL_CODE_FRAGMENT);
        Fragment horoscopeDetailFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.HOROSCOPE_DETAIL_FRAGMENT);
        Fragment matchMakingDetailFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.MATCH_MAKING_DETAIl_FRAGMENT);

        if (panchangFragment != null) {
            view.replaceFragment(PanchangInputFragment.newInstance(), FragmentUtils.PANCHANG_INPUT_FRAGMENT);
        } else if (chatTopicsFragment != null) {
            view.replaceFragment(HoroScopeFragment.newInstance(false), FragmentUtils.HOROSCOPE_FRAGMENT);
        } else if (matchMakingDetailFragment != null) {
            FragmentUtils.onChangeFragment(getSupportFragmentManager(), R.id.container_view, MatchMakingFragment.newInstance(), FragmentUtils.MATCH_MAKING_FRAGMENT);
        } else if (upgradePlanNewFragment != null) {
            FragmentUtils.onChangeFragment(getSupportFragmentManager(), R.id.container_view, AddCreditsFragment.newInstance(0), FragmentUtils.ADD_CREDIT_FRAGMENT);
        } else if (mythBusterDetailFragment != null) {
            FragmentUtils.onChangeFragment(getSupportFragmentManager(), R.id.container_view, MythBusterFragment.newInstance(0, false), FragmentUtils.MYTH_BUSTER_FRAGMENT);
        } else if (singleChatHistoryFragment != null) {
            FragmentUtils.onChangeFragment(getSupportFragmentManager(), R.id.container_view, ChatHistoryFragment.newInstance(), FragmentUtils.CHAT_HISTORY_FRAGMENT);

        } else if (callPlanHistoryFragment != null) {
            view.replaceTalkAstroBuddyFragment();

        } else if (chatHistoryFragment != null) {
            view.replaceFragment(ChatTopicsFragment.newInstance(false), FragmentUtils.CHAT_TOPICS_FRAGMENT);
        } else if (upgradePlanFragment != null || addCreditFragment != null || accountHelpFragment != null) {
            view.replaceFragment(MyAccountFragment.newInstance(), FragmentUtils.MY_ACCOUNT_FRAGMENT);
        } else if (callPlanFragment != null) {
            replaceHomeFragment();
        } else if (changePasswordFragment != null) {
            view.replaceFragment(MyProfileFragment.newInstance(), FragmentUtils.MY_PROFILE_FRAGMENT);
        } else if (myProfileFragment != null || contactFragment != null) {
            view.replaceFragment(MoreSettingsFragment.newInstance(), FragmentUtils.MORE_FRAGMENT);
        } else if (horoscopeDetailFragment != null) {
            view.replaceFragment(HoroScopeFragment.newInstance(false), FragmentUtils.HOROSCOPE_FRAGMENT);
        } else if (buyTopicsViaPromoFragment != null) {
            PreferenceManger preferenceManger = new PreferenceManger(getSharedPreferences(AstroAppConstants.ASTRO_PREF_MANAGER, Context.MODE_PRIVATE));
            if (preferenceManger.getPromoBanner() != null) {
                view.replaceFragment(PromoTemplateFragment.newInstance(preferenceManger.getPromoBanner()), FragmentUtils.PROMO_TEMPLATE_FRAGMENT);
            } else {
                replaceHomeFragment();
            }
        } else {
            replaceHomeFragment();
        }
    }

    private void replaceHomeFragment() {
        Fragment homeFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.USER_HOME_FRAGMENT);
        // Fragment chartFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.YOUR_CHART_FRAGMENT);
        if (homeFragment == null) {
            view.replaceFragment(UserHomeFragment.newInstance(), FragmentUtils.USER_HOME_FRAGMENT);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackButtonPressed();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void showToolBar(boolean show) {

        try {
            if (view != null) {
                if (show)
                    view.toolbar.setVisibility(View.VISIBLE);
                else
                    view.toolbar.setVisibility(View.GONE);
            }
        } catch (Exception ignored) {

        }
    }

    @Override
    public void onBackButtonChange(boolean isEnable) {
        try {
            if (view != null)
                view.enableViews(isEnable);
        } catch (Exception ignored) {

        }
    }

    @Override
    public void onToolbarTitleChange(String title) {
        try {
            if (!TextUtils.isEmpty(title))
                view.setToolbarTitle(title);
        } catch (Exception ignored) {

        }
    }

    @Override
    public void onBackButtonPress() {
        onBackButtonPressed();
    }

    @Override
    public void showHideBuyCreditsButton(int visibility) {

    }

    @Override
    public void showToolbarBackTutorialPrompt(int iconName) {

    }

    @Override
    public void hideBuyTopicTooltip() {

    }

    @Override
    public void hideBottomBar(boolean isHide) {
        if (view != null) {
            view.hideBottomBar(isHide);
        }
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onImageUploaded() {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleNotificationClick();
        handleDeepLinkData();
    }


    private void handleNotificationClick() {
        try {
            Bundle intent = getIntent().getExtras();
            if (intent != null && intent.containsKey(Config.MESSAGE_TYPE)) {
                String notificationType = intent.getString(Config.MESSAGE_TYPE);
                switch (notificationType) {
                    case Config.PICK_REQUEST_PUSH:
                    case Config.CHAT_MESSAGE_PUSH:
                        if (AstroApplication.getInstance().isInternetConnected(true)) {
                            presenter.startChat(false);
                        }
                        break;
                    case Config.TRANSACTION_REPORT_PUSH:
                    case Config.END_CHAT_PUSH:
                        //open my account page

                        NotificationUtils.cancelAllNotification();
                     //   NotificationUtils.clearNotification(AstroApplication.getInstance(),Config.CHAT_MESSAGE_NOTIFICATION_ID);
                        String chatSessionId  = intent.getString("chat_id");
                        String startTimeStamp = intent.getString("start_time");
                        String endTimestamp = intent.getString("end_time");

                        PendingFeedbackModel pendingFeedbackModel = new PendingFeedbackModel(true, chatSessionId, startTimeStamp, endTimestamp);
                        preferenceManger.putPendingFeedback(pendingFeedbackModel);

                        AsyncTask.execute(() -> {
                            abDatabase.conversationDao().deleteChatTable();
                            preferenceManger.putString(PreferenceManger.HANDLER_ID, "");
                            preferenceManger.putString(PreferenceManger.CHAT_SESSION_ID, "");
                        });
                        view.doReplaceFragment(4);
                        break;
                    case Config.TOD_APP_NOTIFICATION:
                        view.openUserForecast();
                        //view.replaceFragment(HoroScopeFragment.newInstance(true), FragmentUtils.HOROSCOPE_FRAGMENT);
                        break;
                    case Config.OFFER_PUSH:
                    case Config.NORMAL_ALERT_PUSH:
                    case Config.CAMPAIGN_PUSH:
                    case Config.SUBSCRIPTION_PUSH:
                    case Config.VOUCHER_PUSH:
                    case Config.MYTH_BUSTER_PUSH:
                        if (intent.containsKey(Config.ACTION)) {
                            String action = intent.getString(Config.ACTION);
                            int mythId = 0;
                            if (intent.containsKey(Config.MYTH_ACTION_ID)){
                                mythId = intent.getInt(Config.MYTH_ACTION_ID);
                            }

                            switch (action) {
                                case Config.HOME_ACTION:
                                    view.doReplaceFragment(0);
                                    showAlertForNotification(intent.getString(Config.TITLE), intent.getString(Config.BODY));
                                    break;
                                case Config.TRANS_ACTION:
                                    view.doReplaceFragment(4);
                                    showAlertForNotification(intent.getString(Config.TITLE), intent.getString(Config.BODY));
                                    break;
                                case Config.MY_PROFILE_ACTION:
                                    view.doReplaceFragment(5);
                                    showAlertForNotification(intent.getString(Config.TITLE), intent.getString(Config.BODY));
                                    break;
                                case Config.MYTH_BUSTER_ACTION:
                                    view.replaceFragment(MythBusterFragment.newInstance(mythId, false), FragmentUtils.MYTH_BUSTER_FRAGMENT);
                                    break;
                                case Config.MYTH_BUSTER_VIDEO_ACTION:
                                    view.replaceFragment(MythBusterFragment.newInstance(mythId, true), FragmentUtils.MYTH_BUSTER_FRAGMENT);
                                    break;
                                case Config.CHAT_ACTION:
                                case Config.CHAT_HISTORY_ACTION:
                                    view.doReplaceFragment(1);
                                    showAlertForNotification(intent.getString(Config.TITLE), intent.getString(Config.BODY));
                                    break;
                                case Config.CONTACT_US_ACTION:
                                    view.doReplaceFragment(7);
                                    showAlertForNotification(intent.getString(Config.TITLE), intent.getString(Config.BODY));
                                    break;
                                case Config.CHART_ACTION:
                                    view.doReplaceFragment(3);
                                    /*  FragmentUtils.onChangeFragment(getSupportFragmentManager(), R.id.container_view, HoroscopeChartPagerFragment.newInstance(), FragmentUtils.YOUR_CHART_FRAGMENT);*/
                                    break;
                                case Config.REFER_ACTION:
                                    view.doReplaceFragment(6);
                                    showAlertForNotification(intent.getString(Config.TITLE), intent.getString(Config.BODY));
                                    break;
                                case Config.ADD_TOPICS_ACTION:
                                    presenter.openBuyCreditScreen();
                                    showAlertForNotification(intent.getString(Config.TITLE), intent.getString(Config.BODY));
                                    break;
                                case Config.PANCHANG_ACTION:
                                    view.replaceFragment(PanchangInputFragment.newInstance(), FragmentUtils.PANCHANG_INPUT_FRAGMENT);
                                    break;
                                case Config.MATCH_MAKING_ACTION:
                                    view.replaceFragment(MatchMakingFragment.newInstance(), FragmentUtils.MATCH_MAKING_FRAGMENT);
                                    break;
                            }
                        }
                        if (intent.containsKey(MyIntentService.NOTIFICATION_ID)) {
                            trackNotificationClick(intent.getInt(MyIntentService.NOTIFICATION_ID, 0));
                        }
                        break;

                }
                view.showHideBuyCreditsButton(View.GONE, false);

            } else {
                //check if session is expired or not
                view.doReplaceFragment(0);
                presenter.checkForSessionExpired();
            }
        } catch (Exception ignored) {

        }
    }


    private void handleDeepLinkData() {
        try {
            // ATTENTION: This was auto-generated to handle app links.
            Intent appLinkIntent = getIntent();
            String appLinkAction = appLinkIntent.getAction();
            Uri appLinkData = appLinkIntent.getData();

            if (Intent.ACTION_VIEW.equals(appLinkAction) && appLinkData != null) {
                if (appLinkData.toString().contains("myths")) {
                    String mythId = appLinkData.getLastPathSegment();
                    Logger.DebugLog(TAG, "mythId : " + mythId);
                    view.replaceFragment(MythBusterFragment.newInstance(Integer.parseInt(mythId), false), FragmentUtils.MYTH_BUSTER_FRAGMENT);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* method to track notification click event */
    public void trackNotificationClick(int notificationId) {
        TrackNotificationClickJob.schedule(notificationId);
    }

    public void showAlertForNotification(String title, String message) {
        DialogHelperClass.showMessageOKCancel(this, title, message, "OK", "", (dialogInterface, i) -> {

        }, null);
    }

    @Override
    public void onPaymentSuccess(String paymentId, PaymentData paymentData) {
        try {
            Logger.DebugLog(TAG, "Payment Id : " + paymentData.getPaymentId());
            //Add your logic here for a successful payment response
            onPaymentResult(1, "", paymentData.getPaymentId(), paymentData);
        } catch (Exception e) {
            Logger.ErrorLog(TAG, "Exception in onPaymentSuccess : " + e.getLocalizedMessage());
            onPaymentResult(-1, "Exception (S) : " + e.getLocalizedMessage(), "", paymentData);
            // showAlertOnTransactionFailed("Oops!! Some error occurred. If any amount deducted will be refunded in 3-5 business days.");
        }
    }

    @Override
    public void onPaymentError(int code, String response, PaymentData paymentData) {
        try {
            Logger.DebugLog(TAG, "Payment Error : " + code + " - " + response);
            onPaymentResult(code, response, "", paymentData);
            //showAlertOnTransactionFailed(response + "!! Some error occurred. If any amount deducted will be refunded in 3-5 business days.");

// Add your logic here for a failed payment response
        } catch (Exception e) {
            Logger.ErrorLog(TAG, "Exception in onPaymentError : " + e.getLocalizedMessage());
            onPaymentResult(-1, "Exception (E) : " + e.getLocalizedMessage(), "", paymentData);
            //showAlertOnTransactionFailed("Oops!! Some error occurred. If any amount deducted will be refunded in 3-5 business days.");
        }
    }

    private void onPaymentResult(int code, String response, String razorPayPaymentID, PaymentData paymentData) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            Fragment addCreditFragment = fragmentManager.findFragmentByTag(FragmentUtils.ADD_CREDIT_FRAGMENT);
            Fragment callPlanFragment =
                    fragmentManager.findFragmentByTag(FragmentUtils.CALL_PLAN_FRAGMENT);
            Fragment buyCreditsFragment = fragmentManager.findFragmentByTag(FragmentUtils.BUY_TOPICS_FRAGMENT);
            Fragment upgradePlanFragment = fragmentManager.findFragmentByTag(FragmentUtils.UPGRADE_PLAN_FRAGMENT);
            Fragment upgradePlanNewFragment = fragmentManager.findFragmentByTag(FragmentUtils.UPGRADE_PLAN_NEW_FRAGMENT);
            if (upgradePlanFragment != null) {
                UpgradePlanPresenter.OnSubscriptionListener onSubscriptionListener = (UpgradePlanPresenter.OnSubscriptionListener) upgradePlanFragment;
                if (!TextUtils.isEmpty(razorPayPaymentID)) {
                    onSubscriptionListener.onSubscriptionSuccess(razorPayPaymentID, paymentData);
                } else {
                    onSubscriptionListener.onSubscriptionFailed(code, response, paymentData);
                }
            } else if (upgradePlanNewFragment != null) {
                UpgradePlanPresenter.OnSubscriptionListener onSubscriptionListener = (UpgradePlanPresenter.OnSubscriptionListener) upgradePlanNewFragment;
                if (!TextUtils.isEmpty(razorPayPaymentID)) {
                    onSubscriptionListener.onSubscriptionSuccess(razorPayPaymentID, paymentData);
                } else {
                    onSubscriptionListener.onSubscriptionFailed(code, response, paymentData);
                }
            } else if (addCreditFragment != null) {
                OnPaymentListener onPaymentListener = (OnPaymentListener) addCreditFragment;
                if (!TextUtils.isEmpty(razorPayPaymentID)) {
                    onPaymentListener.onPaymentCompleted(razorPayPaymentID);
                } else {
                    onPaymentListener.onPaymentFailed(code, response);
                }
            }  else if (callPlanFragment != null) {
                OnPaymentListener onPaymentListener = (OnPaymentListener) callPlanFragment;
                if (!TextUtils.isEmpty(razorPayPaymentID)) {
                    onPaymentListener.onPaymentCompleted(razorPayPaymentID);
                } else {
                    onPaymentListener.onPaymentFailed(code, response);
                }
            } else if (buyCreditsFragment != null) {
                OnPaymentListener onPaymentListener1 = (OnPaymentListener) buyCreditsFragment;
                if (!TextUtils.isEmpty(razorPayPaymentID)) {
                    onPaymentListener1.onPaymentCompleted(razorPayPaymentID);
                } else {
                    onPaymentListener1.onPaymentFailed(code, response);
                }
            }
        }
    }

}

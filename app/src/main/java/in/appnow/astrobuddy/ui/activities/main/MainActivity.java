package in.appnow.astrobuddy.ui.activities.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import java.util.List;

import javax.inject.Inject;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.base.BaseActivity;
import in.appnow.astrobuddy.conversation_module.chat_history.ChatHistoryFragment;
import in.appnow.astrobuddy.dialog.DialogHelperClass;
import in.appnow.astrobuddy.fcm.Config;
import in.appnow.astrobuddy.fcm.NotificationUtils;
import in.appnow.astrobuddy.interfaces.OnPaymentListener;
import in.appnow.astrobuddy.interfaces.OnToolbarListener;
import in.appnow.astrobuddy.jobs.TrackNotificationClickJob;
import in.appnow.astrobuddy.services.MyIntentService;
import in.appnow.astrobuddy.ui.activities.main.dagger.DaggerMainActivityComponent;
import in.appnow.astrobuddy.ui.activities.main.dagger.MainActivityComponent;
import in.appnow.astrobuddy.ui.activities.main.dagger.MainActivityModule;
import in.appnow.astrobuddy.ui.activities.main.mvp.MainActivityPresenter;
import in.appnow.astrobuddy.ui.activities.main.mvp.MainActivityView;
import in.appnow.astrobuddy.ui.fragments.addcredits.AddCreditsFragment;
import in.appnow.astrobuddy.ui.fragments.horoscope.mvp.HoroscopePresenter;
import in.appnow.astrobuddy.ui.fragments.myprofile.mvp.MyProfilePresenter;
import in.appnow.astrobuddy.ui.fragments.myth_buster.MythBusterFragment;
import in.appnow.astrobuddy.ui.fragments.upgrade_plan.mvp.UpgradePlanPresenter;
import in.appnow.astrobuddy.ui.fragments.your_chart.HoroscopeChartPagerFragment;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.Logger;

import static in.appnow.astrobuddy.ui.fragments.upgrade_plan.mvp.UpgradePlanModel.RAZOR_PAY_SUBSCRIPTION_REQUEST_CODE;

@Deprecated
public class MainActivity extends BaseActivity implements OnToolbarListener, MyProfilePresenter.OnImageUploadListener, PaymentResultWithDataListener, HoroscopePresenter.OnHoroscopeTutorialListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    @Inject
    MainActivityView view;
    @Inject
    MainActivityPresenter presenter;

    private MainActivityComponent component;

    public static void startMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        ((AppCompatActivity) context).finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        component = DaggerMainActivityComponent.builder()
                .appComponent(AstroApplication.get(this).component())
                .mainActivityModule(new MainActivityModule(this))
                .build();
        component.inject(this);

        /**
         * Preload payment resources
         */
        try {
            Checkout.preload(getApplicationContext());
        } catch (Exception ignored) {

        }

        setContentView(view);
        presenter.onCreate();
        handleNotificationClick();
        handleDeepLinkData();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    public MainActivityComponent getComponent() {
        return component;
    }

    @Override
    public void onBackPressed() {
        onBackButtonPressed();
    }

    private void onBackButtonPressed() {
        if (!view.isDrawerOpen()) {
            Fragment addCreditFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.ADD_CREDIT_FRAGMENT);
            Fragment upgradePlanFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.UPGRADE_PLAN_FRAGMENT);
            Fragment changePasswordFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.CHANGE_PASSWORD_FRAGMENT);
            Fragment accountHelpFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.ACCOUNT_HELP_FRAGMENT);
            Fragment chatHistoryFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.CHAT_HISTORY_FRAGMENT);
            Fragment singleChatHistoryFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.SINGLE_CHAT_HISTORY_FRAGMENT);
            Fragment mythBusterDetailFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.MYTH_BUSTER_DETAIL_FRAGMENT);
            Fragment upgradePlanNewFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.UPGRADE_PLAN_NEW_FRAGMENT);
            if (upgradePlanNewFragment != null) {
                FragmentUtils.onChangeFragment(getSupportFragmentManager(), R.id.container_view, AddCreditsFragment.newInstance(0), FragmentUtils.ADD_CREDIT_FRAGMENT);
            } else if (mythBusterDetailFragment != null) {
                FragmentUtils.onChangeFragment(getSupportFragmentManager(), R.id.container_view, MythBusterFragment.newInstance(0,false), FragmentUtils.MYTH_BUSTER_FRAGMENT);
            } else if (singleChatHistoryFragment != null) {
                FragmentUtils.onChangeFragment(getSupportFragmentManager(), R.id.container_view, ChatHistoryFragment.newInstance(), FragmentUtils.CHAT_HISTORY_FRAGMENT);
            } else if (chatHistoryFragment != null) {
                view.doReplaceFragment(1);
            } else if (upgradePlanFragment != null || addCreditFragment != null || accountHelpFragment != null) {
                view.doReplaceFragment(4);
            } else if (changePasswordFragment != null) {
                view.doReplaceFragment(5);
            } else {
                Fragment homeFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.HOROSCOPE_FRAGMENT);
                // Fragment chartFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.YOUR_CHART_FRAGMENT);
                if (homeFragment == null) {

                    //if (chartFragment != null) {
                    presenter.checkIfSideMenuHintShown(3);
                    // }
                    view.doReplaceFragment(0);
                } else {
                    super.onBackPressed();
                }
            }
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
        onBackPressed();
    }

    @Override
    public void showHideBuyCreditsButton(int visibility) {
        if (view != null) {
            view.showHideBuyCreditsButton(visibility, false);
            if (visibility == View.VISIBLE) {
                presenter.addTooltipDelay();
            }
        }
    }

    @Override
    public void showToolbarBackTutorialPrompt(int iconName) {
        if (view != null)
            view.showBackButtonPrompt(iconName);
    }

    @Override
    public void hideBuyTopicTooltip() {
        if (view != null)
            view.hideTooltipMenu();
    }

    @Override
    public void hideBottomBar(boolean isHide) {

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragmentList) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
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
                        view.doReplaceFragment(0);
                    }
                }
                break;
        }
    }

    @Override
    public void onImageUploaded() {
        if (presenter != null)
            presenter.updateHeaderData();
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
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra(Config.MESSAGE_TYPE)) {
                String notificationType = intent.getStringExtra(Config.MESSAGE_TYPE);
                switch (notificationType) {
                    case Config.PICK_REQUEST_PUSH:
                    case Config.CHAT_MESSAGE_PUSH:
                        view.doReplaceFragment(0);
                        if (AstroApplication.getInstance().isInternetConnected(true))
                            presenter.startChat(false);
                        break;
                    case Config.TRANSACTION_REPORT_PUSH:
                    case Config.END_CHAT_PUSH:
                        //open my account page
                        view.doReplaceFragment(3);
                        break;
                    case Config.TOD_APP_NOTIFICATION:
                        view.doReplaceFragment(0);
                        Fragment forecastFragment = getSupportFragmentManager().findFragmentByTag(FragmentUtils.HOROSCOPE_FRAGMENT);
                        if (forecastFragment != null) {
                            OnUserHoroscopeForecastListener onUserHoroscopeForecastListener = (OnUserHoroscopeForecastListener) forecastFragment;
                            onUserHoroscopeForecastListener.openUserSignForecast();
                        }
                        break;
                    case Config.OFFER_PUSH:
                    case Config.NORMAL_ALERT_PUSH:
                    case Config.CAMPAIGN_PUSH:
                    case Config.SUBSCRIPTION_PUSH:
                    case Config.VOUCHER_PUSH:
                    case Config.MYTH_BUSTER_PUSH:
                        if (intent.hasExtra(Config.ACTION)) {
                            String action = intent.getStringExtra(Config.ACTION);
                            switch (action) {
                                case Config.HOME_ACTION:
                                    view.doReplaceFragment(0);
                                    showAlertForNotification(intent.getStringExtra(Config.TITLE), intent.getStringExtra(Config.BODY));
                                    break;
                                case Config.TRANS_ACTION:
                                    view.doReplaceFragment(3);
                                    showAlertForNotification(intent.getStringExtra(Config.TITLE), intent.getStringExtra(Config.BODY));
                                    break;
                                case Config.MY_PROFILE_ACTION:
                                    view.doReplaceFragment(4);
                                    showAlertForNotification(intent.getStringExtra(Config.TITLE), intent.getStringExtra(Config.BODY));
                                    break;
                                case Config.MYTH_BUSTER_ACTION:
                                    view.doReplaceFragment(2);
                                    break;
                                case Config.CHAT_ACTION:
                                    view.doReplaceFragment(1);
                                    showAlertForNotification(intent.getStringExtra(Config.TITLE), intent.getStringExtra(Config.BODY));
                                    break;
                                case Config.CHAT_HISTORY_ACTION:
                                    view.doReplaceFragment(1);
                                    showAlertForNotification(intent.getStringExtra(Config.TITLE), intent.getStringExtra(Config.BODY));
                                    break;
                                case Config.CONTACT_US_ACTION:
                                    view.doReplaceFragment(6);
                                    showAlertForNotification(intent.getStringExtra(Config.TITLE), intent.getStringExtra(Config.BODY));
                                    break;
                                case Config.CHART_ACTION:
                                    FragmentUtils.onChangeFragment(getSupportFragmentManager(), R.id.container_view, HoroscopeChartPagerFragment.newInstance(), FragmentUtils.YOUR_CHART_FRAGMENT);
                                    break;
                                case Config.REFER_ACTION:
                                    view.doReplaceFragment(5);
                                    showAlertForNotification(intent.getStringExtra(Config.TITLE), intent.getStringExtra(Config.BODY));
                                    break;
                                case Config.ADD_TOPICS_ACTION:
                                    presenter.openBuyCreditScreen();
                                    showAlertForNotification(intent.getStringExtra(Config.TITLE), intent.getStringExtra(Config.BODY));
                                    break;
                            }
                        }
                        if (intent.hasExtra(MyIntentService.NOTIFICATION_ID)) {
                            trackNotificationClick(intent.getIntExtra(MyIntentService.NOTIFICATION_ID, 0));
                        }
                        break;

                }
                view.showHideBuyCreditsButton(View.GONE, false);
                view.hideTooltipMenu();

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
                    view.replaceFragment(MythBusterFragment.newInstance(Integer.parseInt(mythId),false), FragmentUtils.MYTH_BUSTER_FRAGMENT);
                    view.updateAdapterSelection(2);
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

 /*   @Override
    public void onPaymentSuccess(String razorPayPaymentID) {
        try {
            Logger.DebugLog(TAG, "Payment Id : " + razorPayPaymentID);
            //Add your logic here for a successful payment response
            onPaymentResult(1, "", razorPayPaymentID);
        } catch (Exception e) {
            Logger.ErrorLog(TAG, "Exception in onPaymentSuccess : " + e.getLocalizedMessage());
            onPaymentResult(-1, "Exception (S) : " + e.getLocalizedMessage(), "");
            // showAlertOnTransactionFailed("Oops!! Some error occurred. If any amount deducted will be refunded in 3-5 business days.");
        }

    }

    @Override
    public void onPaymentError(int code, String response) {
        try {
            Logger.DebugLog(TAG, "Payment Error : " + code + " - " + response);
            onPaymentResult(code, response, "");
            //showAlertOnTransactionFailed(response + "!! Some error occurred. If any amount deducted will be refunded in 3-5 business days.");

// Add your logic here for a failed payment response
        } catch (Exception e) {
            Logger.ErrorLog(TAG, "Exception in onPaymentError : " + e.getLocalizedMessage());
            onPaymentResult(-1, "Exception (E) : " + e.getLocalizedMessage(), "");
            //showAlertOnTransactionFailed("Oops!! Some error occurred. If any amount deducted will be refunded in 3-5 business days.");
        }

    }*/

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

    private void showAlertOnTransactionFailed(String message) {
        DialogHelperClass.showMessageOKCancel(this, message, "OK", null, (dialogInterface, i) -> {

        }, null);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (presenter != null) {
            if (presenter.isForecastShow()) {
                presenter.checkIfSideMenuHintShown(3);
            } else {
                presenter.checkIfSideMenuHintShown(2);
            }
        }
        NotificationUtils.clearSingleNotification(Config.GENERAL_NOTIFICATION);
        NotificationUtils.clearSingleNotification(Config.TOD_NOTIFICATION_ID);
    }

    @Override
    public void onComplete() {
        if (presenter != null) {
            presenter.checkIfSideMenuHintShown(2);
        }
    }


    public interface OnUserHoroscopeForecastListener {
        void openUserSignForecast();
    }

}

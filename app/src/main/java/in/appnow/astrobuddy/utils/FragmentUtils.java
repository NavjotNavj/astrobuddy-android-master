package in.appnow.astrobuddy.utils;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.user_authentication_module.activity.LoginActivity;

/**
 * Created by Sonu on 21/08/17.
 */

public class FragmentUtils {
    public static final String HOME_FRAGMENT = "home_fragment";
    public static final String USER_HOME_FRAGMENT = "user_home_fragment";
    public static final String HOROSCOPE_FRAGMENT = "horoscope_fragment";
    public static final String MY_ACCOUNT_FRAGMENT = "my_account_fragment";
    public static final String ADD_CREDIT_FRAGMENT = "add_credit_fragment";
    public static final String BUY_TOPICS_FRAGMENT = "buy_topics_fragment";
    public static final String BUY_TOPICS_VIA_PROMO_FRAGMENT = "buy_topics_via_promo_fragment";
    public static final String REFERRAL_CODE_FRAGMENT = "referral_code_fragment";
    public static final String MY_PROFILE_FRAGMENT = "my_profile_fragment";
    public static final String MY_PROFILE_HOME_FRAGMENT = "my_profile_home_fragment";
    public static final String PROGRESS_DIALOG_FRAGMENT = "progress_dialog_fragment";
    public static final String KNOW_MORE_DIALOG_FRAGMENT = "know_more_dialog_fragment";
    public static final String BUY_PLAN_DIALOG_FRAGMENT = "buy_plan_dialog_fragment";
    public static final String FORGOT_PASSWORD_FRAGMENT = "forgot_password_fragment";
    public static final String FORGOT_PASSWORD_TWO_FRAGMENT = "forgot_password_two_fragment";
    public static final String HOROSCOPE_DETAIL_FRAGMENT = "horoscope_detail_fragment";
    public static final String LOGIN_FRAGMENT = "login_fragment";
    public static final String CHANGE_PASSWORD_FRAGMENT = "change_password_fragment";
    public static final String UPGRADE_PLAN_FRAGMENT = "upgrade_plan_fragment";
    public static final String UPGRADE_PLAN_NEW_FRAGMENT = "upgrade_plan_new_fragment";
    public static final String WEB_VIEW_FRAGMENT = "web_view_fragment";
    public static final String CHAT_TOPICS_FRAGMENT = "chat_topics_fragment";
    public static final String ACCOUNT_HELP_FRAGMENT = "account_help_fragment";
    public static final String MYTH_BUSTER_FRAGMENT = "myth_buster_fragment";
    public static final String YOUR_CHART_FRAGMENT = "your_chart_fragment";
    public static final String CHAT_HISTORY_FRAGMENT = "chat_history_fragment";
    public static final String SINGLE_CHAT_HISTORY_FRAGMENT = "single_chat_history_fragment";
    public static final String CONTACT_US_FRAGMENT = "contact_us_fragment";
    public static final String MYTH_BUSTER_DETAIL_FRAGMENT = "myth_buster_detail_fragment";
    public static final String TALK_FRAGMENT = "talk_fragment";
    public static final String CALL_PLAN_FRAGMENT = "call_plan_fragment";
    public static final String CALL_PLAN_HISTORY_FRAGMENT = "call_plan_history_fragment";
    public static final String MORE_FRAGMENT = "more_fragment";
    public static final String MATCH_MAKING_FRAGMENT = "match_making_fragment";
    public static final String PANCHANG_INPUT_FRAGMENT = "panchang_input_fragment";
    public static final String PANCHANG_FRAGMENT = "panchang_fragment";
    public static final String BANNER_CLICK_FRAGMENT = "banner_click_fragment";
    public static final String PROMO_TEMPLATE_FRAGMENT = "promo_template_fragment";
    public static final String MATCH_MAKING_DETAIl_FRAGMENT = "match_making_detail_fragment";
    public static final String CHAT_TOPICS_FORECAST_FRAGMENT = "chat_topics_forecast_fragment";
    public static final String CONTACT_US_HOME_FRAGMENT = "contact_us_home_fragment";

    // CHAT TYPE
    public static final String FEEDBACK_TYPE_CHAT = "chat";
    public static final String FEEDBACK_TYPE_CALL = "call";


    public static void onChangeFragment(FragmentManager fragmentManager, int frameId, Fragment fragment, String tag) {
        try {
            replaceFragmentCommon(fragmentManager, frameId, fragment, tag, false);
        } catch (Exception ignored) {

        }
    }

    public static void replaceFragmentCommon(FragmentManager fragmentManager, int containerID, Fragment fragment, String tag, boolean isAddtoBackStack) {
        try {
            Fragment fragmentByTag = fragmentManager.findFragmentByTag(tag);
            boolean fragmentPopped = fragmentManager
                    .popBackStackImmediate(tag, 0);
            Logger.DebugLog("Fragment Utils", "Is popped Out : " + tag + " - " + fragmentPopped);
            if (!fragmentPopped && fragmentByTag == null) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(containerID, fragment, tag);
                if (isAddtoBackStack) {
                    fragmentTransaction.addToBackStack(tag);
                }
                fragmentTransaction.commit();
            } else {
                int index = fragmentManager.getBackStackEntryCount() - 1;
                if (index >= 0) {
                    FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(index);
                    String foundTag = backEntry.getName();
                    Logger.ErrorLog("Replace", "TAG : " + foundTag);
                    if (!tag.equals(HOME_FRAGMENT) && !tag.equals(foundTag))
                        fragmentManager.popBackStackImmediate(foundTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    else if (tag.equals(HOME_FRAGMENT))
                        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
        } catch (Exception ignored) {

        }
    }

    public static void addFragmentCommon(FragmentManager fragmentManager, int containerID, Fragment fragment, String tag, boolean isAddtoBackStack) {
        Fragment fragmentByTag = fragmentManager.findFragmentByTag(tag);
        if (fragmentByTag == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().add(containerID, fragment, tag);
            if (isAddtoBackStack) {
                fragmentTransaction.addToBackStack(tag);
            }
            fragmentTransaction.commit();
        } else {
            int index = fragmentManager.getBackStackEntryCount() - 1;
            if (index >= 0) {
                FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(index);
                String foundTag = backEntry.getName();
                Logger.ErrorLog("Replace", "TAG : " + foundTag);
                if (!tag.equals(HOME_FRAGMENT) && !tag.equals(foundTag))
                    fragmentManager.popBackStackImmediate(foundTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                else if (tag.equals(HOME_FRAGMENT))
                    fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
    }


    public static void onLogoutSuccess(AppCompatActivity appCompatActivity) {
        appCompatActivity.startActivity(new Intent(appCompatActivity, LoginActivity.class));
        appCompatActivity.finish();
    }

    public static void popFragmentFromBackStack(FragmentManager fragmentManager, String tag) {
        int index = fragmentManager.getBackStackEntryCount() - 1;
        if (index >= 0) {
            FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(index);
            String foundTag = backEntry.getName();
            if (foundTag.equals(tag))
                fragmentManager.popBackStackImmediate();
        }
    }
}


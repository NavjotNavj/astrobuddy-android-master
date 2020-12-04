package in.appnow.astrobuddy.ui.common_activity.dagger;

import androidx.appcompat.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;
import in.appnow.astrobuddy.conversation_module.chat_history.mvp.ChatHistoryModel;
import in.appnow.astrobuddy.conversation_module.chat_history.mvp.ChatHistoryPresenter;
import in.appnow.astrobuddy.conversation_module.chat_history.mvp.view.ChatHistoryView;
import in.appnow.astrobuddy.conversation_module.chat_history.single_chat_history.mvp.SingleChatHistoryModel;
import in.appnow.astrobuddy.conversation_module.chat_history.single_chat_history.mvp.SingleChatHistoryPresenter;
import in.appnow.astrobuddy.conversation_module.chat_history.single_chat_history.mvp.view.SingleChatHistoryView;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.ui.activities.main.dagger.MainActivityScope;
import in.appnow.astrobuddy.ui.activities.main.mvp.MainActivityModel;
import in.appnow.astrobuddy.ui.activities.main.mvp.MainActivityPresenter;
import in.appnow.astrobuddy.ui.activities.main.mvp.MainActivityView;
import in.appnow.astrobuddy.ui.fragments.account_help.mvp.AccountHelpModel;
import in.appnow.astrobuddy.ui.fragments.account_help.mvp.AccountHelpPresenter;
import in.appnow.astrobuddy.ui.fragments.account_help.mvp.AccountHelpView;
import in.appnow.astrobuddy.ui.fragments.addcredits.mvp.AddCreditsModel;
import in.appnow.astrobuddy.ui.fragments.addcredits.mvp.AddCreditsPresenter;
import in.appnow.astrobuddy.ui.fragments.addcredits.mvp.AddCreditsView;
import in.appnow.astrobuddy.ui.fragments.changePassword.mvp.ChangePasswordModel;
import in.appnow.astrobuddy.ui.fragments.changePassword.mvp.ChangePasswordPresenter;
import in.appnow.astrobuddy.ui.fragments.changePassword.mvp.ChangePasswordView;
import in.appnow.astrobuddy.ui.fragments.chat_topics.mvp.ChatTopicsModel;
import in.appnow.astrobuddy.ui.fragments.chat_topics.mvp.ChatTopicsPresenter;
import in.appnow.astrobuddy.ui.fragments.chat_topics.mvp.view.ChatTopicsView;
import in.appnow.astrobuddy.ui.fragments.contact_us.mvp.ContactUsModel;
import in.appnow.astrobuddy.ui.fragments.contact_us.mvp.ContactUsPresenter;
import in.appnow.astrobuddy.ui.fragments.contact_us.mvp.ContactUsView;
import in.appnow.astrobuddy.ui.fragments.horoscope.mvp.HoroscopeModel;
import in.appnow.astrobuddy.ui.fragments.horoscope.mvp.HoroscopePresenter;
import in.appnow.astrobuddy.ui.fragments.horoscope.mvp.HoroscopeView;
import in.appnow.astrobuddy.ui.fragments.horoscope_detail.pager.mvp.HoroscopePagerModel;
import in.appnow.astrobuddy.ui.fragments.horoscope_detail.pager.mvp.HoroscopePagerPresenter;
import in.appnow.astrobuddy.ui.fragments.horoscope_detail.pager.mvp.HoroscopePagerView;
import in.appnow.astrobuddy.ui.fragments.myaccount.mvp.MyAccountModel;
import in.appnow.astrobuddy.ui.fragments.myaccount.mvp.MyAccountPresenter;
import in.appnow.astrobuddy.ui.fragments.myaccount.mvp.view.MyAccountView;
import in.appnow.astrobuddy.ui.fragments.myprofile.mvp.MyProfileModel;
import in.appnow.astrobuddy.ui.fragments.myprofile.mvp.MyProfilePresenter;
import in.appnow.astrobuddy.ui.fragments.myprofile.mvp.MyProfileView;
import in.appnow.astrobuddy.ui.fragments.myth_buster.mvp.MythBusterModel;
import in.appnow.astrobuddy.ui.fragments.myth_buster.mvp.MythBusterPresenter;
import in.appnow.astrobuddy.ui.fragments.myth_buster.mvp.view.MythBusterView;
import in.appnow.astrobuddy.ui.fragments.referral.mvp.ReferralModel;
import in.appnow.astrobuddy.ui.fragments.referral.mvp.ReferralPresenter;
import in.appnow.astrobuddy.ui.fragments.referral.mvp.view.ReferralView;
import in.appnow.astrobuddy.ui.fragments.upgrade_plan.mvp.UpgradePlanModel;
import in.appnow.astrobuddy.ui.fragments.upgrade_plan.mvp.UpgradePlanPresenter;
import in.appnow.astrobuddy.ui.fragments.upgrade_plan.mvp.view.UpgradePlanView;
import in.appnow.astrobuddy.ui.fragments.your_chart.mvp.HoroscopeChartPagerModel;
import in.appnow.astrobuddy.ui.fragments.your_chart.mvp.HoroscopeChartPagerPresenter;
import in.appnow.astrobuddy.ui.fragments.your_chart.mvp.HoroscopeChartPagerView;

/**
 * Created by sonu on 11:49, 12/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
@Module
public class CommonActivityModule {

    private final AppCompatActivity appCompatActivity;

    public CommonActivityModule(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

}

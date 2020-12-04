package in.appnow.astrobuddy.ui.activities.main.dagger;

import dagger.Component;
import in.appnow.astrobuddy.dagger.component.AppComponent;
import in.appnow.astrobuddy.ui.activities.main.MainActivity;
import in.appnow.astrobuddy.ui.fragments.account_help.MyAccountHelpFragment;
import in.appnow.astrobuddy.ui.fragments.addcredits.AddCreditsFragment;
import in.appnow.astrobuddy.ui.fragments.changePassword.ChangePasswordFragment;
import in.appnow.astrobuddy.ui.fragments.chat_topics.ChatTopicsFragment;
import in.appnow.astrobuddy.ui.fragments.contact_us.ContactUsFragment;
import in.appnow.astrobuddy.ui.fragments.home.UserHomeFragment;
import in.appnow.astrobuddy.ui.fragments.horoscope.HoroScopeFragment;
import in.appnow.astrobuddy.ui.fragments.horoscope_detail.HoroscopeDetailFragment;
import in.appnow.astrobuddy.ui.fragments.horoscope_detail.pager.HoroscopeDetailPagerFragment;
import in.appnow.astrobuddy.ui.fragments.myaccount.MyAccountFragment;
import in.appnow.astrobuddy.ui.fragments.myprofile.MyProfileFragment;
import in.appnow.astrobuddy.ui.fragments.myth_buster.MythBusterFragment;
import in.appnow.astrobuddy.ui.fragments.myth_buster.myth_buster_detail.MythBusterDetailFragment;
import in.appnow.astrobuddy.ui.fragments.referral.ReferralFragment;
import in.appnow.astrobuddy.ui.fragments.upgrade_plan.UpgradePlanFragment;
import in.appnow.astrobuddy.ui.fragments.your_chart.HoroscopeChartPagerFragment;

/**
 * Created by sonu on 11:48, 12/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
@MainActivityScope
@Component(modules = MainActivityModule.class, dependencies = AppComponent.class)
public interface MainActivityComponent {
   void inject(MainActivity mainActivity);

    /* void inject(UserHomeFragment userHomeFragment);

    void inject(HoroScopeFragment horoScopeFragment);

    void inject(MyAccountFragment myAccountFragment);

    void inject(AddCreditsFragment addCreditsFragment);

    void inject(ReferralFragment referralFragment);

    void inject(MyProfileFragment myProfileFragment);

    void inject(HoroscopeDetailFragment horoscopeDetailFragment);

    void inject(HoroscopeDetailPagerFragment horoscopeDetailPagerFragment);

    void inject(ChangePasswordFragment changePasswordFragment);

    void inject(UpgradePlanFragment upgradePlanFragment);

    void inject(ChatTopicsFragment chatTopicsFragment);

    void inject(MyAccountHelpFragment myAccountHelpFragment);

    void inject(MythBusterFragment mythBustertFragment);

    void inject(ChatHistoryFragment chatHistoryFragment);

    void inject(SingleChatHistoryFragment singleChatHistoryFragment);

    void inject(ContactUsFragment contactUsFragment);

    void inject(HoroscopeChartPagerFragment horoscopeChartFragment);

    void inject(MythBusterDetailFragment mythBusterDetailFragment);*/

}

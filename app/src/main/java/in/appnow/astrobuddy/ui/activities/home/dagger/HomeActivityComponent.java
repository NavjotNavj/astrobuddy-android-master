package in.appnow.astrobuddy.ui.activities.home.dagger;

import dagger.Component;
import in.appnow.astrobuddy.conversation_module.chat_history.ChatHistoryFragment;
import in.appnow.astrobuddy.conversation_module.chat_history.single_chat_history.SingleChatHistoryFragment;
import in.appnow.astrobuddy.dagger.component.AppComponent;
import in.appnow.astrobuddy.ui.activities.home.HomeActivity;
import in.appnow.astrobuddy.ui.fragments.account_help.MyAccountHelpFragment;
import in.appnow.astrobuddy.ui.fragments.addcredits.AddCreditsFragment;
import in.appnow.astrobuddy.ui.fragments.call_plans.CallPlansFragment;
import in.appnow.astrobuddy.ui.fragments.call_plans.call_plan_history.CallPlansHistoryFragment;
import in.appnow.astrobuddy.ui.fragments.changePassword.ChangePasswordFragment;
import in.appnow.astrobuddy.ui.fragments.chat_topics.ChatTopicsFragment;
import in.appnow.astrobuddy.ui.fragments.contact_us.ContactUsFragment;
import in.appnow.astrobuddy.ui.fragments.home.UserHomeFragment;
import in.appnow.astrobuddy.ui.fragments.horoscope.HoroScopeFragment;
import in.appnow.astrobuddy.ui.fragments.horoscope_detail.HoroscopeDetailFragment;
import in.appnow.astrobuddy.ui.fragments.horoscope_detail.pager.HoroscopeDetailPagerFragment;
import in.appnow.astrobuddy.ui.fragments.match_making.MatchMakingDetailFragment;
import in.appnow.astrobuddy.ui.fragments.match_making.MatchMakingFragment;
import in.appnow.astrobuddy.ui.fragments.more.MoreSettingsFragment;
import in.appnow.astrobuddy.ui.fragments.myaccount.MyAccountFragment;
import in.appnow.astrobuddy.ui.fragments.myprofile.MyProfileFragment;
import in.appnow.astrobuddy.ui.fragments.myth_buster.MythBusterFragment;
import in.appnow.astrobuddy.ui.fragments.myth_buster.myth_buster_detail.MythBusterDetailFragment;
import in.appnow.astrobuddy.ui.fragments.panchang.PanchangFragment;
import in.appnow.astrobuddy.ui.fragments.panchang.input.PanchangInputFragment;
import in.appnow.astrobuddy.ui.fragments.promo_template.PromoTemplateFragment;
import in.appnow.astrobuddy.ui.fragments.referral.ReferralFragment;
import in.appnow.astrobuddy.ui.fragments.upgrade_plan.UpgradePlanFragment;
import in.appnow.astrobuddy.ui.fragments.your_chart.HoroscopeChartPagerFragment;

/**
 * Created by Abhishek Thanvi on 19/03/19.
 * Copyright © 2019 Abhishek Thanvi. All rights reserved.
 */

@HomeActivityScope
@Component(modules = HomeActivityModule.class, dependencies = AppComponent.class)
public interface HomeActivityComponent {

    void inject(HomeActivity homeActivity);

    void inject(UserHomeFragment userHomeFragment);

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

    void inject(MythBusterDetailFragment mythBusterDetailFragment);

    void inject(MoreSettingsFragment moreSettingsFragment);

    void inject(MatchMakingFragment matchMakingFragment);

    void inject(PanchangInputFragment panchangInputFragment);

    void inject(PanchangFragment panchangFragment);

    void inject(PromoTemplateFragment promoTemplateFragment);

    void inject(MatchMakingDetailFragment matchMakingDetailFragment);

    void inject(CallPlansFragment callPlansFragment);

    void inject(CallPlansHistoryFragment callPlansHistoryFragment);
}

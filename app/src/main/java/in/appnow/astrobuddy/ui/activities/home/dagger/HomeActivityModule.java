package in.appnow.astrobuddy.ui.activities.home.dagger;

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
import in.appnow.astrobuddy.ui.activities.home.mvp.HomeActivityModel;
import in.appnow.astrobuddy.ui.activities.home.mvp.HomeActivityPresenter;
import in.appnow.astrobuddy.ui.activities.home.mvp.HomeActivityView;
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
import in.appnow.astrobuddy.ui.fragments.home.mvp.UserHomeModel;
import in.appnow.astrobuddy.ui.fragments.home.mvp.UserHomePresenter;
import in.appnow.astrobuddy.ui.fragments.home.mvp.UserHomeView;
import in.appnow.astrobuddy.ui.fragments.horoscope.mvp.HoroscopeModel;
import in.appnow.astrobuddy.ui.fragments.horoscope.mvp.HoroscopePresenter;
import in.appnow.astrobuddy.ui.fragments.horoscope.mvp.HoroscopeView;
import in.appnow.astrobuddy.ui.fragments.horoscope_detail.pager.mvp.HoroscopePagerModel;
import in.appnow.astrobuddy.ui.fragments.horoscope_detail.pager.mvp.HoroscopePagerPresenter;
import in.appnow.astrobuddy.ui.fragments.horoscope_detail.pager.mvp.HoroscopePagerView;
import in.appnow.astrobuddy.ui.fragments.match_making.mvp.MatchMakingDetailModel;
import in.appnow.astrobuddy.ui.fragments.match_making.mvp.MatchMakingDetailPresenter;
import in.appnow.astrobuddy.ui.fragments.match_making.mvp.MatchMakingDetailView;
import in.appnow.astrobuddy.ui.fragments.match_making.mvp.MatchMakingModel;
import in.appnow.astrobuddy.ui.fragments.match_making.mvp.MatchMakingPresenter;
import in.appnow.astrobuddy.ui.fragments.match_making.mvp.MatchMakingView;
import in.appnow.astrobuddy.ui.fragments.more.mvp.MoreSettingsFragmentPresenter;
import in.appnow.astrobuddy.ui.fragments.more.mvp.MoreSettingsFragmentView;
import in.appnow.astrobuddy.ui.fragments.more.mvp.MoreSettingsModel;
import in.appnow.astrobuddy.ui.fragments.myaccount.mvp.MyAccountModel;
import in.appnow.astrobuddy.ui.fragments.myaccount.mvp.MyAccountPresenter;
import in.appnow.astrobuddy.ui.fragments.myaccount.mvp.view.MyAccountView;
import in.appnow.astrobuddy.ui.fragments.myprofile.mvp.MyProfileModel;
import in.appnow.astrobuddy.ui.fragments.myprofile.mvp.MyProfilePresenter;
import in.appnow.astrobuddy.ui.fragments.myprofile.mvp.MyProfileView;
import in.appnow.astrobuddy.ui.fragments.myth_buster.mvp.MythBusterModel;
import in.appnow.astrobuddy.ui.fragments.myth_buster.mvp.MythBusterPresenter;
import in.appnow.astrobuddy.ui.fragments.myth_buster.mvp.view.MythBusterView;
import in.appnow.astrobuddy.ui.fragments.myth_buster.myth_buster_detail.mvp.MythBusterDetailModel;
import in.appnow.astrobuddy.ui.fragments.myth_buster.myth_buster_detail.mvp.MythBusterDetailPresenter;
import in.appnow.astrobuddy.ui.fragments.myth_buster.myth_buster_detail.mvp.MythBusterDetailView;
import in.appnow.astrobuddy.ui.fragments.panchang.input.mvp.PanchangInputModel;
import in.appnow.astrobuddy.ui.fragments.panchang.input.mvp.PanchangInputPresenter;
import in.appnow.astrobuddy.ui.fragments.panchang.input.mvp.PanchangInputView;
import in.appnow.astrobuddy.ui.fragments.panchang.mvp.PanchangModel;
import in.appnow.astrobuddy.ui.fragments.panchang.mvp.PanchangPresenter;
import in.appnow.astrobuddy.ui.fragments.panchang.mvp.PanchangView;
import in.appnow.astrobuddy.ui.fragments.promo_template.mvp.PromoTemplateModel;
import in.appnow.astrobuddy.ui.fragments.promo_template.mvp.PromoTemplatePresenter;
import in.appnow.astrobuddy.ui.fragments.promo_template.mvp.PromoTemplateView;
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
 * Created by Abhishek Thanvi on 19/03/19.
 * Copyright © 2019 Abhishek Thanvi. All rights reserved.
 */

@Module
public class HomeActivityModule {

    private final AppCompatActivity appCompatActivity;

    public HomeActivityModule(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    /* Main Activity MVP Injection */
    @Provides
    @HomeActivityScope
    public HomeActivityView view(PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new HomeActivityView(appCompatActivity, preferenceManger, abDatabase);
    }

    @Provides
    @HomeActivityScope
    public HomeActivityModel mainActivityModel(APIInterface apiInterface) {
        return new HomeActivityModel(appCompatActivity, apiInterface);
    }

    @Provides
    @HomeActivityScope
    public HomeActivityPresenter mainActivityPresenter(HomeActivityView view, HomeActivityModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new HomeActivityPresenter(view, model, preferenceManger, abDatabase);
    }


    /* User Home Fragment MVP Injection */
    @Provides
    @HomeActivityScope
    public UserHomeView provideUserHomeView(PreferenceManger preferenceManger,ABDatabase abDatabase) {
        return new UserHomeView(appCompatActivity,preferenceManger,abDatabase);
    }

    @Provides
    @HomeActivityScope
    public UserHomeModel providesUserHomeModel(APIInterface apiInterface) {
        return new UserHomeModel(appCompatActivity, apiInterface);
    }

    @Provides
    @HomeActivityScope
    public UserHomePresenter providesUserHomePresenter(UserHomeView view, UserHomeModel model,
                                                       PreferenceManger preferenceManger,
                                                       ABDatabase abDatabase) {
        return new UserHomePresenter(view, model, preferenceManger, abDatabase);
    }


    /* Horoscope Fragment MVP Injection */
    @Provides
    @HomeActivityScope
    public HoroscopeView providesHoroscopeView() {
        return new HoroscopeView(appCompatActivity);
    }

    @Provides
    @HomeActivityScope
    public HoroscopeModel providesHoroscopeModel(APIInterface apiInterface) {
        return new HoroscopeModel(appCompatActivity, apiInterface);
    }

    @Provides
    @HomeActivityScope
    public HoroscopePresenter providesHoroscopePresenter(HoroscopeView view, HoroscopeModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new HoroscopePresenter(view, model, preferenceManger, abDatabase);
    }

    /* My Account Fragment MVP Injection */
    @Provides
    @HomeActivityScope
    public MyAccountView providesMyAccountView() {
        return new MyAccountView(appCompatActivity);
    }

    @Provides
    @HomeActivityScope
    public MyAccountModel providesMyAccountModel(APIInterface apiInterface) {
        return new MyAccountModel(appCompatActivity, apiInterface);
    }

    @Provides
    @HomeActivityScope
    public MyAccountPresenter providesMyAccountPresenter(MyAccountView view, MyAccountModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new MyAccountPresenter(view, model, preferenceManger, abDatabase);
    }

    /* Add Credits Fragment MVP Injection */
    @Provides
    @HomeActivityScope
    public AddCreditsView providesAddCreditsView() {
        return new AddCreditsView(appCompatActivity);
    }

    @Provides
    @HomeActivityScope
    public AddCreditsModel providesAddCreditsModel(APIInterface apiInterface) {
        return new AddCreditsModel(appCompatActivity, apiInterface);
    }

    @Provides
    @HomeActivityScope
    public AddCreditsPresenter providesAddCreditsPresenter(AddCreditsView view, AddCreditsModel model, ABDatabase abDatabase, PreferenceManger preferenceManger) {
        return new AddCreditsPresenter(view, model, abDatabase, preferenceManger);
    }

    /* Referral Fragment MVP Injection */
    @Provides
    @HomeActivityScope
    public ReferralView providesReferralView() {
        return new ReferralView(appCompatActivity);
    }

    @Provides
    @HomeActivityScope
    public ReferralModel providesReferralModel(APIInterface apiInterface) {
        return new ReferralModel(appCompatActivity, apiInterface);
    }

    @Provides
    @HomeActivityScope
    public ReferralPresenter providesReferralPresenter(ReferralView view, ReferralModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new ReferralPresenter(view, model, preferenceManger, abDatabase);
    }

    /* My Profile Fragment MVP Injection */
    @Provides
    @HomeActivityScope
    public MyProfileView providesMyProfileView() {
        return new MyProfileView(appCompatActivity);
    }

    @Provides
    @HomeActivityScope
    public MyProfileModel providesMyProfileModel(APIInterface apiInterface) {
        return new MyProfileModel(appCompatActivity, apiInterface);
    }

    @Provides
    @HomeActivityScope
    public MyProfilePresenter providesMyProfilePresenter(MyProfileView view, MyProfileModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new MyProfilePresenter(view, model, preferenceManger, abDatabase);
    }


    /* Horoscope Detail Fragment MVP Injection */
   /* @Provides
    @IntroPagerActivityScope
    public HoroScopeDetailView horoScopeDetailView() {
        return new HoroScopeDetailView(appCompatActivity);
    }

    @Provides
    @IntroPagerActivityScope
    public HoroScopeDetailModel horoScopeDetailModel(APIInterface apiInterface) {
        return new HoroScopeDetailModel(appCompatActivity, apiInterface);
    }

    @Provides
    @IntroPagerActivityScope
    public HoroScopeDetailPresenter horoScopeDetailPresenter(HoroScopeDetailView view, HoroScopeDetailModel model, PreferenceManger preferenceManger) {
        return new HoroScopeDetailPresenter(view, model, preferenceManger);
    }*/


    /* Horoscope Pager Fragment MVP Injection */
    @Provides
    @HomeActivityScope
    public HoroscopePagerView horoscopePagerView() {
        return new HoroscopePagerView(appCompatActivity);
    }

    @Provides
    @HomeActivityScope
    public HoroscopePagerModel horoscopePagerModel(APIInterface apiInterface) {
        return new HoroscopePagerModel(appCompatActivity, apiInterface);
    }

    @Provides
    @HomeActivityScope
    public HoroscopePagerPresenter horoscopePagerPresenter(HoroscopePagerView view, HoroscopePagerModel model,PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new HoroscopePagerPresenter(view, model,preferenceManger, abDatabase);
    }


    /* Change Password Fragment MVP Injection */
    @Provides
    @HomeActivityScope
    public ChangePasswordView changePasswordView() {
        return new ChangePasswordView(appCompatActivity);
    }

    @Provides
    @HomeActivityScope
    public ChangePasswordModel changePasswordModel(APIInterface apiInterface) {
        return new ChangePasswordModel(appCompatActivity, apiInterface);
    }

    @Provides
    @HomeActivityScope
    public ChangePasswordPresenter changePasswordPresenter(ChangePasswordView view, ChangePasswordModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new ChangePasswordPresenter(view, model, preferenceManger, abDatabase);
    }

    /* Upgrade Plan Fragment MVP Injection */
    @Provides
    @HomeActivityScope
    public UpgradePlanView upgradePlanView() {
        return new UpgradePlanView(appCompatActivity);
    }

    @Provides
    @HomeActivityScope
    public UpgradePlanModel upgradePlanModel(APIInterface apiInterface) {
        return new UpgradePlanModel(appCompatActivity, apiInterface);
    }

    @Provides
    @HomeActivityScope
    public UpgradePlanPresenter upgradePlanPresenter(UpgradePlanView view, UpgradePlanModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new UpgradePlanPresenter(view, model, preferenceManger, abDatabase);
    }


    /* Chat Topics Fragment MVP Injection */
    @Provides
    @HomeActivityScope
    public ChatTopicsView chatTopicsView() {
        return new ChatTopicsView(appCompatActivity);
    }

    @Provides
    @HomeActivityScope
    public ChatTopicsModel chatTopicsModel(APIInterface apiInterface) {
        return new ChatTopicsModel(appCompatActivity, apiInterface);
    }

    @Provides
    @HomeActivityScope
    public ChatTopicsPresenter chatTopicsPresenter(ChatTopicsView view, ChatTopicsModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new ChatTopicsPresenter(view, model, preferenceManger, abDatabase);
    }

    /* My Account Help Fragment MVP Injection */
    @Provides
    @HomeActivityScope
    public AccountHelpView accountHelpView() {
        return new AccountHelpView(appCompatActivity);
    }

    @Provides
    @HomeActivityScope
    public AccountHelpModel accountHelpModel(APIInterface apiInterface) {
        return new AccountHelpModel(appCompatActivity, apiInterface);
    }

    @Provides
    @HomeActivityScope
    public AccountHelpPresenter accountHelpPresenter(AccountHelpView view, AccountHelpModel model
            , PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new AccountHelpPresenter(view, model, preferenceManger, abDatabase);
    }

    /* Myth Buster Fragment MVP Injection */
    @Provides
    @HomeActivityScope
    public MythBusterView mythBusterView() {
        return new MythBusterView(appCompatActivity);
    }

    @Provides
    @HomeActivityScope
    public MythBusterModel mythBusterModel(APIInterface apiInterface) {
        return new MythBusterModel(appCompatActivity, apiInterface);
    }

    @Provides
    @HomeActivityScope
    public MythBusterPresenter mythBusterPresenter(MythBusterView view, MythBusterModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new MythBusterPresenter(view, model, preferenceManger, abDatabase);
    }


    /* Chat History Fragment MVP Injection */
    @Provides
    @HomeActivityScope
    public ChatHistoryView chatHistoryView() {
        return new ChatHistoryView(appCompatActivity);
    }

    @Provides
    @HomeActivityScope
    public ChatHistoryModel chatHistoryModel(APIInterface apiInterface) {
        return new ChatHistoryModel(appCompatActivity, apiInterface);
    }

    @Provides
    @HomeActivityScope
    public ChatHistoryPresenter chatHistoryPresenter(ChatHistoryView view, ChatHistoryModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new ChatHistoryPresenter(view, model, preferenceManger, abDatabase);
    }

    /* Single Chat History Fragment MVP Injection */
    @Provides
    @HomeActivityScope
    public SingleChatHistoryView singleChatHistoryView() {
        return new SingleChatHistoryView(appCompatActivity);
    }

    @Provides
    @HomeActivityScope
    public SingleChatHistoryModel singleChatHistoryModel(APIInterface apiInterface) {
        return new SingleChatHistoryModel(appCompatActivity, apiInterface);
    }

    @Provides
    @HomeActivityScope
    public SingleChatHistoryPresenter singleChatHistoryPresenter(SingleChatHistoryView view, SingleChatHistoryModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new SingleChatHistoryPresenter(view, model, preferenceManger, abDatabase);
    }

    /* Contact Us Fragment MVP Injection */
    @Provides
    @HomeActivityScope
    public ContactUsView contactUsView() {
        return new ContactUsView(appCompatActivity);
    }

    @Provides
    @HomeActivityScope
    public ContactUsModel contactUsModel() {
        return new ContactUsModel(appCompatActivity);
    }

    @Provides
    @HomeActivityScope
    public ContactUsPresenter contactUsPresenter(ContactUsView view, ContactUsModel model) {
        return new ContactUsPresenter(view, model);
    }

    /* Contact Us Fragment MVP Injection */
    @Provides
    @HomeActivityScope
    public HoroscopeChartPagerView horoscopeChartView() {
        return new HoroscopeChartPagerView(appCompatActivity);
    }

    @Provides
    @HomeActivityScope
    public HoroscopeChartPagerModel horoscopeChartModel(APIInterface apiInterface) {
        return new HoroscopeChartPagerModel(appCompatActivity, apiInterface);
    }

    @Provides
    @HomeActivityScope
    public HoroscopeChartPagerPresenter horoscopeChartPresenter(HoroscopeChartPagerView view, HoroscopeChartPagerModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new HoroscopeChartPagerPresenter(view, model, preferenceManger, abDatabase);
    }


    /* Myth Buster Detail MVP Injection */
    @Provides
    @HomeActivityScope
    public MythBusterDetailView mythBusterDetailView() {
        return new MythBusterDetailView(appCompatActivity);
    }

    @Provides
    @HomeActivityScope
    public MythBusterDetailModel mythBusterDetailModel(APIInterface apiInterface) {
        return new MythBusterDetailModel(appCompatActivity, apiInterface);
    }

    @Provides
    @HomeActivityScope
    public MythBusterDetailPresenter mythBusterDetailPresenter(MythBusterDetailView view, MythBusterDetailModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new MythBusterDetailPresenter(view, model, preferenceManger, abDatabase);
    }


    /* Match Making MVP Injection */
    @Provides
    @HomeActivityScope
    public MatchMakingView matchMakingView() {
        return new MatchMakingView(appCompatActivity);
    }

    @Provides
    @HomeActivityScope
    public MatchMakingModel matchMakingModel(APIInterface apiInterface) {
        return new MatchMakingModel(appCompatActivity, apiInterface);
    }

    @Provides
    @HomeActivityScope
    public MatchMakingPresenter matchMakingPresenter(MatchMakingView view, MatchMakingModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new MatchMakingPresenter(view, model, preferenceManger, abDatabase);
    }

    /* Match Making Detail Pager MVP Injection */

    @Provides
    @HomeActivityScope
    public MatchMakingDetailView matchMakingDetailView() {
        return new MatchMakingDetailView(appCompatActivity);
    }

    @Provides
    @HomeActivityScope
    public MatchMakingDetailModel matchMakingDetailModel(APIInterface apiInterface) {
        return new MatchMakingDetailModel(appCompatActivity, apiInterface);
    }

    @Provides
    @HomeActivityScope
    public MatchMakingDetailPresenter matchMakingDetailPresenter(MatchMakingDetailView view, MatchMakingDetailModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new MatchMakingDetailPresenter(view, model, preferenceManger, abDatabase);
    }

    /* Panchang Input MVP Injection */
    @Provides
    @HomeActivityScope
    public PanchangInputView panchangInputView() {
        return new PanchangInputView(appCompatActivity);
    }

    @Provides
    @HomeActivityScope
    public PanchangInputModel panchangInputModel() {
        return new PanchangInputModel(appCompatActivity);
    }

    @Provides
    @HomeActivityScope
    public PanchangInputPresenter panchangInputPresenter(PanchangInputView view, PanchangInputModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new PanchangInputPresenter(view, model, preferenceManger, abDatabase);
    }

    /* Panchang MVP Injection */
    @Provides
    @HomeActivityScope
    public PanchangView panchangView() {
        return new PanchangView(appCompatActivity);
    }

    @Provides
    @HomeActivityScope
    public PanchangModel panchangModel(APIInterface apiInterface) {
        return new PanchangModel(appCompatActivity, apiInterface);
    }

    @Provides
    @HomeActivityScope
    public PanchangPresenter panchangPresenter(PanchangView view, PanchangModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new PanchangPresenter(view, model, preferenceManger, abDatabase);
    }


    /* More Settings MVP Injection */
    @Provides
    @HomeActivityScope
    public MoreSettingsFragmentView moreSettingsFragmentView() {
        return new MoreSettingsFragmentView(appCompatActivity);
    }

    @Provides
    @HomeActivityScope
    public MoreSettingsModel moreSettingsModel(APIInterface apiInterface) {
        return new MoreSettingsModel(appCompatActivity, apiInterface);
    }

    @Provides
    @HomeActivityScope
    public MoreSettingsFragmentPresenter moreSettingsFragmentPresenter(MoreSettingsFragmentView view, MoreSettingsModel model, PreferenceManger preferenceManger,ABDatabase abDatabase) {
        return new MoreSettingsFragmentPresenter(view, model, preferenceManger,abDatabase);
    }


    /* Promo Template MVP Injection */
    @Provides
    @HomeActivityScope
    public PromoTemplateView promoTemplateView() {
        return new PromoTemplateView(appCompatActivity);
    }

    @Provides
    @HomeActivityScope
    public PromoTemplateModel promoTemplateModel(APIInterface apiInterface) {
        return new PromoTemplateModel(appCompatActivity, apiInterface);
    }

    @Provides
    @HomeActivityScope
    public PromoTemplatePresenter promoTemplatePresenter(PromoTemplateView view, PromoTemplateModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new PromoTemplatePresenter(view, model, preferenceManger, abDatabase);
    }

}




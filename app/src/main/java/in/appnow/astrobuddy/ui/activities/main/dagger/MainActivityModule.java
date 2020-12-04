package in.appnow.astrobuddy.ui.activities.main.dagger;

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
import in.appnow.astrobuddy.ui.fragments.home.mvp.UserHomePresenter;
import in.appnow.astrobuddy.ui.fragments.home.mvp.UserHomeView;
import in.appnow.astrobuddy.ui.fragments.home.mvp.UserHomeModel;
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
import in.appnow.astrobuddy.ui.fragments.myth_buster.myth_buster_detail.mvp.MythBusterDetailModel;
import in.appnow.astrobuddy.ui.fragments.myth_buster.myth_buster_detail.mvp.MythBusterDetailPresenter;
import in.appnow.astrobuddy.ui.fragments.myth_buster.myth_buster_detail.mvp.MythBusterDetailView;
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
public class MainActivityModule {

    private final AppCompatActivity appCompatActivity;

    public MainActivityModule(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    /* Main Activity MVP Injection */
    @Provides
    @MainActivityScope
    public MainActivityView view(PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new MainActivityView(appCompatActivity, preferenceManger, abDatabase);
    }

    @Provides
    @MainActivityScope
    public MainActivityModel mainActivityModel(APIInterface apiInterface) {
        return new MainActivityModel(appCompatActivity, apiInterface);
    }

    @Provides
    @MainActivityScope
    public MainActivityPresenter mainActivityPresenter(MainActivityView view, MainActivityModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new MainActivityPresenter(view, model, preferenceManger, abDatabase);
    }


    /* User Home Fragment MVP Injection */
    @Provides
    @MainActivityScope
    public UserHomeView provideUserHomeView(PreferenceManger preferenceManger,ABDatabase abDatabase) {
        return new UserHomeView(appCompatActivity,preferenceManger,abDatabase);
    }

    @Provides
    @MainActivityScope
    public UserHomeModel providesUserHomeModel(APIInterface apiInterface) {
        return new UserHomeModel(appCompatActivity, apiInterface);
    }

    @Provides
    @MainActivityScope
    public UserHomePresenter providesUserHomePresenter(UserHomeView view, UserHomeModel model,
                                                       PreferenceManger preferenceManger,
                                                       ABDatabase abDatabase) {
        return new UserHomePresenter(view, model, preferenceManger, abDatabase);
    }


    /* Horoscope Fragment MVP Injection */
    @Provides
    @MainActivityScope
    public HoroscopeView providesHoroscopeView() {
        return new HoroscopeView(appCompatActivity);
    }

    @Provides
    @MainActivityScope
    public HoroscopeModel providesHoroscopeModel(APIInterface apiInterface) {
        return new HoroscopeModel(appCompatActivity, apiInterface);
    }

    @Provides
    @MainActivityScope
    public HoroscopePresenter providesHoroscopePresenter(HoroscopeView view, HoroscopeModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new HoroscopePresenter(view, model, preferenceManger, abDatabase);
    }

    /* My Account Fragment MVP Injection */
    @Provides
    @MainActivityScope
    public MyAccountView providesMyAccountView() {
        return new MyAccountView(appCompatActivity);
    }

    @Provides
    @MainActivityScope
    public MyAccountModel providesMyAccountModel(APIInterface apiInterface) {
        return new MyAccountModel(appCompatActivity, apiInterface);
    }

    @Provides
    @MainActivityScope
    public MyAccountPresenter providesMyAccountPresenter(MyAccountView view, MyAccountModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new MyAccountPresenter(view, model, preferenceManger, abDatabase);
    }

    /* Add Credits Fragment MVP Injection */
    @Provides
    @MainActivityScope
    public AddCreditsView providesAddCreditsView() {
        return new AddCreditsView(appCompatActivity);
    }

    @Provides
    @MainActivityScope
    public AddCreditsModel providesAddCreditsModel(APIInterface apiInterface) {
        return new AddCreditsModel(appCompatActivity, apiInterface);
    }

    @Provides
    @MainActivityScope
    public AddCreditsPresenter providesAddCreditsPresenter(AddCreditsView view, AddCreditsModel model, ABDatabase abDatabase, PreferenceManger preferenceManger) {
        return new AddCreditsPresenter(view, model, abDatabase, preferenceManger);
    }

    /* Referral Fragment MVP Injection */
    @Provides
    @MainActivityScope
    public ReferralView providesReferralView() {
        return new ReferralView(appCompatActivity);
    }

    @Provides
    @MainActivityScope
    public ReferralModel providesReferralModel(APIInterface apiInterface) {
        return new ReferralModel(appCompatActivity, apiInterface);
    }

    @Provides
    @MainActivityScope
    public ReferralPresenter providesReferralPresenter(ReferralView view, ReferralModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new ReferralPresenter(view, model, preferenceManger, abDatabase);
    }

    /* My Profile Fragment MVP Injection */
    @Provides
    @MainActivityScope
    public MyProfileView providesMyProfileView() {
        return new MyProfileView(appCompatActivity);
    }

    @Provides
    @MainActivityScope
    public MyProfileModel providesMyProfileModel(APIInterface apiInterface) {
        return new MyProfileModel(appCompatActivity, apiInterface);
    }

    @Provides
    @MainActivityScope
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
    @MainActivityScope
    public HoroscopePagerView horoscopePagerView() {
        return new HoroscopePagerView(appCompatActivity);
    }

    @Provides
    @MainActivityScope
    public HoroscopePagerModel horoscopePagerModel(APIInterface apiInterface) {
        return new HoroscopePagerModel(appCompatActivity, apiInterface);
    }

    @Provides
    @MainActivityScope
    public HoroscopePagerPresenter horoscopePagerPresenter(HoroscopePagerView view, HoroscopePagerModel model,PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new HoroscopePagerPresenter(view, model,preferenceManger, abDatabase);
    }


    /* Change Password Fragment MVP Injection */
    @Provides
    @MainActivityScope
    public ChangePasswordView changePasswordView() {
        return new ChangePasswordView(appCompatActivity);
    }

    @Provides
    @MainActivityScope
    public ChangePasswordModel changePasswordModel(APIInterface apiInterface) {
        return new ChangePasswordModel(appCompatActivity, apiInterface);
    }

    @Provides
    @MainActivityScope
    public ChangePasswordPresenter changePasswordPresenter(ChangePasswordView view, ChangePasswordModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new ChangePasswordPresenter(view, model, preferenceManger, abDatabase);
    }

    /* Upgrade Plan Fragment MVP Injection */
    @Provides
    @MainActivityScope
    public UpgradePlanView upgradePlanView() {
        return new UpgradePlanView(appCompatActivity);
    }

    @Provides
    @MainActivityScope
    public UpgradePlanModel upgradePlanModel(APIInterface apiInterface) {
        return new UpgradePlanModel(appCompatActivity, apiInterface);
    }

    @Provides
    @MainActivityScope
    public UpgradePlanPresenter upgradePlanPresenter(UpgradePlanView view, UpgradePlanModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new UpgradePlanPresenter(view, model, preferenceManger, abDatabase);
    }


    /* Chat Topics Fragment MVP Injection */
    @Provides
    @MainActivityScope
    public ChatTopicsView chatTopicsView() {
        return new ChatTopicsView(appCompatActivity);
    }

    @Provides
    @MainActivityScope
    public ChatTopicsModel chatTopicsModel(APIInterface apiInterface) {
        return new ChatTopicsModel(appCompatActivity, apiInterface);
    }

    @Provides
    @MainActivityScope
    public ChatTopicsPresenter chatTopicsPresenter(ChatTopicsView view, ChatTopicsModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new ChatTopicsPresenter(view, model, preferenceManger, abDatabase);
    }

    /* My Account Help Fragment MVP Injection */
    @Provides
    @MainActivityScope
    public AccountHelpView accountHelpView() {
        return new AccountHelpView(appCompatActivity);
    }

    @Provides
    @MainActivityScope
    public AccountHelpModel accountHelpModel(APIInterface apiInterface) {
        return new AccountHelpModel(appCompatActivity, apiInterface);
    }

    @Provides
    @MainActivityScope
    public AccountHelpPresenter accountHelpPresenter(AccountHelpView view, AccountHelpModel model
            , PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new AccountHelpPresenter(view, model, preferenceManger, abDatabase);
    }

    /* Myth Buster Fragment MVP Injection */
    @Provides
    @MainActivityScope
    public MythBusterView mythBusterView() {
        return new MythBusterView(appCompatActivity);
    }

    @Provides
    @MainActivityScope
    public MythBusterModel mythBusterModel(APIInterface apiInterface) {
        return new MythBusterModel(appCompatActivity, apiInterface);
    }

    @Provides
    @MainActivityScope
    public MythBusterPresenter mythBusterPresenter(MythBusterView view, MythBusterModel model,PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new MythBusterPresenter(view, model,preferenceManger, abDatabase);
    }


    /* Chat History Fragment MVP Injection */
    @Provides
    @MainActivityScope
    public ChatHistoryView chatHistoryView() {
        return new ChatHistoryView(appCompatActivity);
    }

    @Provides
    @MainActivityScope
    public ChatHistoryModel chatHistoryModel(APIInterface apiInterface) {
        return new ChatHistoryModel(appCompatActivity, apiInterface);
    }

    @Provides
    @MainActivityScope
    public ChatHistoryPresenter chatHistoryPresenter(ChatHistoryView view, ChatHistoryModel model
            , PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new ChatHistoryPresenter(view, model, preferenceManger, abDatabase);
    }

    /* Single Chat History Fragment MVP Injection */
    @Provides
    @MainActivityScope
    public SingleChatHistoryView singleChatHistoryView() {
        return new SingleChatHistoryView(appCompatActivity);
    }

    @Provides
    @MainActivityScope
    public SingleChatHistoryModel singleChatHistoryModel(APIInterface apiInterface) {
        return new SingleChatHistoryModel(appCompatActivity, apiInterface);
    }

    @Provides
    @MainActivityScope
    public SingleChatHistoryPresenter singleChatHistoryPresenter(SingleChatHistoryView view, SingleChatHistoryModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new SingleChatHistoryPresenter(view, model, preferenceManger, abDatabase);
    }

    /* Contact Us Fragment MVP Injection */
    @Provides
    @MainActivityScope
    public ContactUsView contactUsView() {
        return new ContactUsView(appCompatActivity);
    }

    @Provides
    @MainActivityScope
    public ContactUsModel contactUsModel() {
        return new ContactUsModel(appCompatActivity);
    }

    @Provides
    @MainActivityScope
    public ContactUsPresenter contactUsPresenter(ContactUsView view, ContactUsModel model) {
        return new ContactUsPresenter(view, model);
    }

    /* Contact Us Fragment MVP Injection */
    @Provides
    @MainActivityScope
    public HoroscopeChartPagerView horoscopeChartView() {
        return new HoroscopeChartPagerView(appCompatActivity);
    }

    @Provides
    @MainActivityScope
    public HoroscopeChartPagerModel horoscopeChartModel(APIInterface apiInterface) {
        return new HoroscopeChartPagerModel(appCompatActivity, apiInterface);
    }

    @Provides
    @MainActivityScope
    public HoroscopeChartPagerPresenter horoscopeChartPresenter(HoroscopeChartPagerView view, HoroscopeChartPagerModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new HoroscopeChartPagerPresenter(view, model, preferenceManger, abDatabase);
    }


    /* Myth Buster Detail MVP Injection */
    @Provides
    @MainActivityScope
    public MythBusterDetailView mythBusterDetailView() {
        return new MythBusterDetailView(appCompatActivity);
    }

    @Provides
    @MainActivityScope
    public MythBusterDetailModel mythBusterDetailModel(APIInterface apiInterface) {
        return new MythBusterDetailModel(appCompatActivity,apiInterface);
    }

    @Provides
    @MainActivityScope
    public MythBusterDetailPresenter mythBusterDetailPresenter(MythBusterDetailView view, MythBusterDetailModel model,PreferenceManger preferenceManger, ABDatabase abDatabase) {
        return new MythBusterDetailPresenter(view, model,preferenceManger, abDatabase);
    }

}

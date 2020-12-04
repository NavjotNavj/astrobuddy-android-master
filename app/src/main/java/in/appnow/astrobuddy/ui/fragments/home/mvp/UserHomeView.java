package in.appnow.astrobuddy.ui.fragments.home.mvp;

import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import in.appnow.astrobuddy.BuildConfig;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.adapters.BannerAdapter;
import in.appnow.astrobuddy.adapters.HomeItemsRecyclerAdapter;
import in.appnow.astrobuddy.base.BaseView;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.custom_views.LoopViewPager;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dao.BannerStatsDao;
import in.appnow.astrobuddy.dao.ScreenStatsDao;
import in.appnow.astrobuddy.dialog.DialogHelperClass;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.helper.RecyclerItemClickListener;
import in.appnow.astrobuddy.models.BannerStatsModel;
import in.appnow.astrobuddy.models.HomeModel;
import in.appnow.astrobuddy.models.ScreenStatsModel;
import in.appnow.astrobuddy.rest.request.PostUserStatsRequest;
import in.appnow.astrobuddy.rest.response.LoginResponseModel;
import in.appnow.astrobuddy.rest.response.MyAccountResponse;
import in.appnow.astrobuddy.rest.response.PromoBannerResponse;
import in.appnow.astrobuddy.rest.response.UserProfile;
import in.appnow.astrobuddy.ui.fragments.call_plans.CallPlansFragment;
import in.appnow.astrobuddy.ui.fragments.chat_topics.ChatTopicsFragment;
import in.appnow.astrobuddy.ui.fragments.horoscope.HoroScopeFragment;
import in.appnow.astrobuddy.ui.fragments.match_making.MatchMakingFragment;
import in.appnow.astrobuddy.ui.fragments.myaccount.MyAccountFragment;
import in.appnow.astrobuddy.ui.fragments.myth_buster.MythBusterFragment;
import in.appnow.astrobuddy.ui.fragments.panchang.input.PanchangInputFragment;
import in.appnow.astrobuddy.ui.fragments.your_chart.HoroscopeChartPagerFragment;
import in.appnow.astrobuddy.utils.DateUtils;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.ImageUtils;
import in.appnow.astrobuddy.utils.Logger;
import in.appnow.astrobuddy.utils.VectorUtils;
import io.reactivex.Observable;
import it.sephiroth.android.library.tooltip.Tooltip;
import me.relex.circleindicator.CircleIndicator;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

/**
 * Created by Abhishek Thanvi on 19/03/19.
 * Copyright © 2019 Abhishek Thanvi. All rights reserved.
 */

public class UserHomeView extends BaseViewClass implements BaseView {


    private static final String TAG = UserHomeView.class.getSimpleName();
    private final AppCompatActivity context;
    private final PreferenceManger preferenceManger;

    @BindView(R.id.background_image)
    ImageView backgroundImage;

    @BindView(R.id.home_recycler)
    RecyclerView homeRecycler;
    @BindView(R.id.home_nested_scroll_view)
    NestedScrollView nestedScrollView;

    @BindView(R.id.banner_recycler)
    LoopViewPager bannerViewPager;
    @BindView(R.id.banner_circle_indicator)
    CircleIndicator circleIndicator;
    @BindView(R.id.user_icon)
    CircleImageView userImageView;
    @BindView(R.id.user_name_label)
    AppCompatTextView userNameLabel;
    @BindView(R.id.user_star_sign_label)
    AppCompatTextView userStarSignLabel;
    @BindView(R.id.tip_of_day_label)
    AppCompatTextView tipOfTheDayLabel;
    @BindView(R.id.tip_of_the_day_card_view)
    CardView tipCardView;
    @BindView(R.id.user_home_topics_label)
    TextView userHomeTopicsLabel;
    @BindView(R.id.tip_of_day_title_image_one)
    AppCompatImageView todImageOne;
    @BindView(R.id.tip_of_day_title_image_two)
    AppCompatImageView todImageTwo;
    @BindString(R.string.waiting_dash)
    String dashString;
    public FragmentManager fragmentManager;
    private Handler handler;
    private Timer swipeTimer;
    protected int topicsCount = 0;
    private BannerAdapter bannerAdapter = new BannerAdapter(getContext());
    private GridLayoutManager gridLayoutManager;
    private List<PromoBannerResponse.PromoBanner> promoBannerList = new ArrayList<>();
    private HomeItemsRecyclerAdapter homeItemsRecyclerAdapter;
    private int currentPage = 0;
    private int NUM_PAGES = 0;
    private Tooltip.TooltipView tooltip;

    private static final int TIMER_DELAY = 3000;//3sec

    private final ABDatabase abDatabase;

    public UserHomeView(@NonNull AppCompatActivity context, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        super(context);
        this.preferenceManger = preferenceManger;
        this.abDatabase = abDatabase;
        this.context = context;
        inflate(context, R.layout.fragment_user_home, this);
        ButterKnife.bind(this, this);
        ImageUtils.setBackgroundImage(context, backgroundImage);
        userHomeTopicsLabel.setText(dashString);
        VectorUtils.setVectorCompoundDrawable(userHomeTopicsLabel, context, R.drawable.ic_account_balance_wallet_black_24dp, 0, 0, 0, R.color.white);
        ImageUtils.setDrawableImage(context, todImageOne, R.drawable.home_tip);
        ImageUtils.setDrawableImage(context, todImageTwo, R.drawable.home_tip);
        gridLayoutManager = new GridLayoutManager(context, 3);
        homeRecycler.setLayoutManager(gridLayoutManager);
        homeRecycler.setNestedScrollingEnabled(false);
        homeRecycler.addOnItemTouchListener(
                new RecyclerItemClickListener(context, homeRecycler, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        doReplaceFragment(position);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                })
        );

        if (BuildConfig.DEBUG) {
            AsyncTask.execute(() -> fetchUserStatsData());
        }

        preferenceManger.putBoolean(PreferenceManger.WELCOME_INTRO_DIALOG, true);
        preferenceManger.putBoolean(PreferenceManger.ALL_HINT, false);
        checkIfBuyTopicButtonVisible();
    }

    public Observable<Object> observeTopicLabelClick() {
        return RxView.clicks(userHomeTopicsLabel);
    }


    public void setUpRecycler(HomeModel homeModel) {
        homeItemsRecyclerAdapter = new HomeItemsRecyclerAdapter(context, homeModel);
        homeRecycler.setAdapter(homeItemsRecyclerAdapter);
    }

    public Observable<PromoBannerResponse.PromoBanner> observePromoBannerClick() {
        return bannerAdapter.getPromoBannerPublishSubject();
    }


    public Observable<Object> userIconClick() {
        return RxView.clicks(userImageView);
    }

    public void updateTipOfTheDay(String tipOfTheDay) {
        tipCardView.setVisibility(View.VISIBLE);
        tipOfTheDayLabel.setVisibility(View.VISIBLE);
        tipOfTheDayLabel.setText(tipOfTheDay);
    }

    public void setUserProfile(PreferenceManger preferenceManger) {
        try {
            if (preferenceManger == null) return;
            LoginResponseModel loginResponseModel = preferenceManger.getUserDetails();
            if (loginResponseModel == null) return;
            UserProfile userProfile = loginResponseModel.getUserProfile();
            if (userProfile != null) {
                if (userProfile.getFullName() != null) {
                    userNameLabel.setText(userProfile.getFullName());
                }
                userStarSignLabel.setText(userProfile.getStarSign());
                if (userProfile.getGender().equalsIgnoreCase("M")) {
                    ImageUtils.loadImageUrl(context, userImageView, R.drawable.icon_boy, userProfile.getProfileImage());
                } else if (userProfile.getGender().equalsIgnoreCase("F")) {
                    ImageUtils.loadImageUrl(context, userImageView, R.drawable.icon_girl, userProfile.getProfileImage());
                } else {
                    ImageUtils.loadImageUrl(context, userImageView, R.drawable.icon_default_profile, userProfile.getProfileImage());
                }
            }
        } catch (Exception ignored) {

        }

    }

    public void doReplaceFragment(int position) {
        switch (position) {
            case 0:
                //Kundli/Chart
                replaceFragment(HoroscopeChartPagerFragment.newInstance(), FragmentUtils.YOUR_CHART_FRAGMENT);
                break;
            case 1:
                //match making
                preferenceManger.putBoolean(PreferenceManger.MATCH_MAKING_HINT, true);
                replaceFragment(MatchMakingFragment.newInstance(), FragmentUtils.MATCH_MAKING_FRAGMENT);
                break;
            case 2:
                //Forecast
                replaceFragment(HoroScopeFragment.newInstance(false), FragmentUtils.HOROSCOPE_FRAGMENT);
                break;
            case 3:
                //Myth Buster
                preferenceManger.putBoolean(PreferenceManger.MYTH_BUSTER_HINT, true);
                replaceFragment(MythBusterFragment.newInstance(0, false), FragmentUtils.MYTH_BUSTER_FRAGMENT);
                break;
            case 4:
                //Videos
                replaceFragment(MythBusterFragment.newInstance(0, true), FragmentUtils.MYTH_BUSTER_FRAGMENT);
                break;
            case 5:
                //panchang
                replaceFragment(PanchangInputFragment.newInstance(), FragmentUtils.PANCHANG_INPUT_FRAGMENT);
                break;
            case 6:
                //my account
                replaceFragment(MyAccountFragment.newInstance(), FragmentUtils.MY_ACCOUNT_FRAGMENT);
                break;
            case 7:
                //astro call
                preferenceManger.putBoolean(PreferenceManger.LIVE_CALL_HINT, true);
                replaceFragment(CallPlansFragment.newInstance(), FragmentUtils.CALL_PLAN_FRAGMENT);
                //CommonActivity.openCommonActivity(context, "Talk to AstroBuddy", FragmentUtils.TALK_FRAGMENT);
                break;
            case 8:
                //astro chat
                preferenceManger.putBoolean(PreferenceManger.CHAT_TAP_HINT, true);
                replaceFragment(ChatTopicsFragment.newInstance(true), FragmentUtils.CHAT_TOPICS_FRAGMENT);
                break;
        }
    }


    @Override
    public void onUnknownError(String error) {

    }

    @Override
    public void onTimeout() {

    }

    @Override
    public void onNetworkError() {

    }

    @Override
    public boolean isNetworkConnected() {
        return false;
    }

    @Override
    public void onConnectionError() {

    }

    public void replaceFragment(Fragment fragment, String fragmentTag) {
        FragmentUtils.onChangeFragment(fragmentManager, R.id.container_view, fragment, fragmentTag);
    }

    public void addFragment(Fragment fragment, String fragmentTag) {
        FragmentUtils.onChangeFragment(fragmentManager, R.id.container_view, fragment, fragmentTag);
    }

    public void updateTopicsCount(MyAccountResponse data) {
        if (!data.isErrorStatus() && data.getAccountDetails() != null) {
            this.topicsCount = data.getAccountDetails().getUserTopics();
            userHomeTopicsLabel.setText(String.valueOf(data.getAccountDetails().getUserTopics()));
        } else {
            this.topicsCount = 0;
            userHomeTopicsLabel.setText(dashString);
        }
    }

    Runnable autoSwipeRunnable = new Runnable() {
        public void run() {
            if (currentPage == NUM_PAGES) {
                currentPage = 0;
            }
            bannerViewPager.setCurrentItem(currentPage++, true);
        }
    };

    public void populatePromoBanners(PromoBannerResponse data) {
        if (data != null && !data.isErrorStatus() && data.getPromoBannerList() != null && data.getPromoBannerList().size() > 0) {

            promoBannerList.clear();
            promoBannerList.addAll(data.getPromoBannerList());

            Collections.sort(promoBannerList, (promoBanner, t1) -> promoBanner.getSeq() - t1.getSeq());
            bannerAdapter.swapData(promoBannerList);
            bannerViewPager.setAdapter(bannerAdapter);

            circleIndicator.setViewPager(bannerViewPager);

            bannerViewPager.setVisibility(View.VISIBLE);

            NUM_PAGES = data.getPromoBannerList().size();

            handler = new Handler();
            swipeTimer = new Timer();

            swipeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (handler != null) {
                        handler.post(autoSwipeRunnable);
                    }
                }
            }, TIMER_DELAY, TIMER_DELAY);

            bannerViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    currentPage = position;
                    checkIfBannerIsInBounds(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        } else {
            bannerViewPager.setVisibility(View.GONE);
        }
    }

    public void cancelAutoSwipeTimer() {
        if (swipeTimer != null) {
            swipeTimer.cancel();
            swipeTimer = null;
        }
        if (handler != null) {
            handler.removeCallbacks(autoSwipeRunnable);
            handler = null;
        }
    }

    private void checkIfBannerIsInBounds(int position) {
        Rect scrollBounds = new Rect();
        nestedScrollView.getHitRect(scrollBounds);
        if (bannerViewPager.getLocalVisibleRect(scrollBounds)) {
            // Any portion of the imageView, even a single pixel, is within the visible window
            insertBannerImpressions(position);
        } else {
            // NONE of the imageView is within the visible window
        }

    }

    private void insertBannerImpressions(int position) {
        if (position != -1 && promoBannerList != null && promoBannerList.size() > 0 && position < promoBannerList.size()) {
            PromoBannerResponse.PromoBanner promoBanner = promoBannerList.get(position);
            updateBannerStats(promoBanner.getId(), promoBanner.getType(), false);
        }
    }

    public void insertBannerClicks(PromoBannerResponse.PromoBanner promoBanner) {
        if (promoBanner != null) {
            updateBannerStats(promoBanner.getId(), promoBanner.getType(), true);
        }
    }

    /**
     * TUTORIAL METHODS
     **/

    public void isWelcomeDialogShow() {
        //check if myth buster hint is shown or not
        if (preferenceManger.getBooleanValue(PreferenceManger.WELCOME_INTRO_DIALOG)) {

            if (preferenceManger.getBooleanValue(PreferenceManger.ALL_HINT)) {
                checkIfMythBusterHintShown();
            } else {
                checkIfBuyTopicButtonVisible();
            }

        } else {
            DialogHelperClass.showMessageOKCancel(context,
                    "Welcome",
                    "Thank You for downloading AstroBuddy. Let me walk you through the app.",
                    "Ok",
                    "Skip",
                    (dialogInterface, i) -> {
                        preferenceManger.putBoolean(PreferenceManger.WELCOME_INTRO_DIALOG, true);
                        preferenceManger.putBoolean(PreferenceManger.ALL_HINT, true);
                        checkIfMythBusterHintShown();

                    }, (dialog, which) -> {
                        preferenceManger.putBoolean(PreferenceManger.WELCOME_INTRO_DIALOG, true);
                        preferenceManger.putBoolean(PreferenceManger.ALL_HINT, false);
                        checkIfBuyTopicButtonVisible();
                    });
        }
    }

    private void checkIfMythBusterHintShown() {
        //check if myth buster hint is shown or not
        if (preferenceManger.getBooleanValue(PreferenceManger.MYTH_BUSTER_HINT)) {
            checkIfMatchMakingHintShown();
        } else {
            showMythBusterPrompt();
        }
    }

    private void showMythBusterPrompt() {

        new Handler().postDelayed(() -> {
            RelativeLayout card = (RelativeLayout) gridLayoutManager.findViewByPosition(3);
            if (card != null) {
                HomeItemsRecyclerAdapter.HomeItemVH viewHolder = (HomeItemsRecyclerAdapter.HomeItemVH) homeRecycler.getChildViewHolder(card);
                new MaterialTapTargetPrompt.Builder(context)
                        .setTarget(viewHolder.itemImage)
                        //.setClipToView(card.getChildAt(0))
                        .setPrimaryText("Myth Buster")
                        .setMaxTextWidth(R.dimen.home_prompt_text_width)
                        .setSecondaryText("Check frequent articles and videos busting superstitions and myths.")
                        //.setPromptFocal(new RectanglePromptFocal())
                        .setBackgroundColour(context.getResources().getColor(R.color.colorAccent))
                        .setFocalColour(context.getResources().getColor(android.R.color.transparent))
                        //  .setFocalRadius(200f)
                        .setAutoDismiss(false)
                        .setAutoFinish(false)
                        .setPrimaryTextSize(R.dimen.wheel_view_rim_prompt_text_primary_size)
                        .setSecondaryTextSize(R.dimen.wheel_view_rim_prompt_text_secondary_size)
                        .setCaptureTouchEventOutsidePrompt(true)
                        .setPromptStateChangeListener((prompt, state) -> {
                            if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                                prompt.finish();
                            }
                        })
                        .show();
            }
        }, 500);

    }

    private void checkIfMatchMakingHintShown() {
        //check if match making hint is shown or not
        if (preferenceManger.getBooleanValue(PreferenceManger.MATCH_MAKING_HINT)) {
            checkIfChatHintShown();
        } else {
            showMatchMakingPrompt();
        }
    }


    private void showMatchMakingPrompt() {
        new Handler().postDelayed(() -> {
            RelativeLayout card = (RelativeLayout) gridLayoutManager.findViewByPosition(1);
            if (card != null) {
                HomeItemsRecyclerAdapter.HomeItemVH viewHolder = (HomeItemsRecyclerAdapter.HomeItemVH) homeRecycler.getChildViewHolder(card);
                new MaterialTapTargetPrompt.Builder(context)
                        .setTarget(viewHolder.itemImage)
                        //.setClipToView(card.getChildAt(0))
                        .setPrimaryText("Match Making")
                        .setSecondaryText("Check compatibility with your partner.")
                        //.setPromptFocal(new RectanglePromptFocal())
                        .setMaxTextWidth(R.dimen.home_prompt_text_width)
                        .setBackgroundColour(context.getResources().getColor(R.color.colorAccent))
                        .setFocalColour(context.getResources().getColor(android.R.color.transparent))
                        // .setFocalRadius(200f)
                        .setAutoDismiss(false)
                        .setAutoFinish(false)
                        .setPrimaryTextSize(R.dimen.wheel_view_rim_prompt_text_primary_size)
                        .setSecondaryTextSize(R.dimen.wheel_view_rim_prompt_text_secondary_size)
                        .setCaptureTouchEventOutsidePrompt(true)
                        .setPromptStateChangeListener((prompt, state) -> {
                            if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                                prompt.finish();
                            }
                        })
                        .show();
            }
        }, 500);
    }

    private void checkIfChatHintShown() {
        //check if chat hint is shown or not
        if (preferenceManger.getBooleanValue(PreferenceManger.CHAT_TAP_HINT)) {
            checkIfLiveCallHintShown();
        } else {
            showChatPrompt();
        }
    }


    private void showChatPrompt() {
        new Handler().postDelayed(() -> {
            RelativeLayout card = (RelativeLayout) gridLayoutManager.findViewByPosition(8);
            if (card != null) {
                HomeItemsRecyclerAdapter.HomeItemVH viewHolder = (HomeItemsRecyclerAdapter.HomeItemVH) homeRecycler.getChildViewHolder(card);
                new MaterialTapTargetPrompt.Builder(context)
                        .setTarget(viewHolder.itemImage)
                        //.setClipToView(card.getChildAt(0))
                        .setPrimaryText("Chat")
                        .setSecondaryText("Chat live with AstroBuddy.")
                        .setMaxTextWidth(R.dimen.home_prompt_text_width)
                        //.setPromptFocal(new RectanglePromptFocal())
                        .setBackgroundColour(context.getResources().getColor(R.color.colorAccent))
                        .setFocalColour(context.getResources().getColor(android.R.color.transparent))
                        // .setFocalRadius(200f)
                        .setAutoDismiss(false)
                        .setPrimaryTextSize(R.dimen.wheel_view_rim_prompt_text_primary_size)
                        .setSecondaryTextSize(R.dimen.wheel_view_rim_prompt_text_secondary_size)
                        .setAutoFinish(false)
                        .setCaptureTouchEventOutsidePrompt(true)
                        .setPromptStateChangeListener((prompt, state) -> {
                            if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                                prompt.finish();
                            }
                        })
                        .show();
            }
        }, 500);
    }


    private void checkIfBuyCreditsHintShown() {
        //check if buy credits hint is shown or not
        if (preferenceManger.getBooleanValue(PreferenceManger.BUY_CREDITS_HINT)) {
            checkIfBuyTopicButtonVisible();
        } else {
            showBuyCreditsPrompt();
        }
    }

    private void checkIfLiveCallHintShown() {
        if (preferenceManger.getBooleanValue(PreferenceManger.LIVE_CALL_HINT)) {
            checkIfBuyCreditsHintShown();
        } else {
            showLiveCallPrompt();
        }
    }

    private void showLiveCallPrompt() {
        new Handler().postDelayed(() -> {
            RelativeLayout card = (RelativeLayout) gridLayoutManager.findViewByPosition(7);
            if (card != null) {
                HomeItemsRecyclerAdapter.HomeItemVH viewHolder = (HomeItemsRecyclerAdapter.HomeItemVH) homeRecycler.getChildViewHolder(card);
                new MaterialTapTargetPrompt.Builder(context)
                        .setTarget(viewHolder.itemImage)
                        //.setClipToView(card.getChildAt(0))
                        .setPrimaryText("Live Call")
                        .setSecondaryText("Live call with AstroBuddy.")
                        .setMaxTextWidth(R.dimen.home_prompt_text_width)
                        //.setPromptFocal(new RectanglePromptFocal())
                        .setBackgroundColour(context.getResources().getColor(R.color.colorAccent))
                        .setFocalColour(context.getResources().getColor(android.R.color.transparent))
                        // .setFocalRadius(200f)
                        .setAutoDismiss(false)
                        .setPrimaryTextSize(R.dimen.wheel_view_rim_prompt_text_primary_size)
                        .setSecondaryTextSize(R.dimen.wheel_view_rim_prompt_text_secondary_size)
                        .setAutoFinish(false)
                        .setCaptureTouchEventOutsidePrompt(true)
                        .setPromptStateChangeListener((prompt, state) -> {
                            if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                                prompt.finish();
                            }
                        })
                        .show();
            }
        }, 500);
    }

    private void showBuyCreditsPrompt() {
        new MaterialTapTargetPrompt.Builder(context)
                .setTarget(userHomeTopicsLabel)
                .setPrimaryText("Buy credits")
                .setSecondaryText("Click here to buy chat credits.")
                .setMaxTextWidth(R.dimen.home_prompt_text_width)
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                .setBackgroundColour(context.getResources().getColor(R.color.colorAccent))
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                .setCaptureTouchEventOutsidePrompt(true)
                .setAutoDismiss(false)
                .setAutoFinish(false)
                .setPrimaryTextSize(R.dimen.wheel_view_rim_prompt_text_primary_size)
                .setSecondaryTextSize(R.dimen.wheel_view_rim_prompt_text_secondary_size)
                .setIcon(R.drawable.ic_wallet)
                .setIconDrawableColourFilter(context.getResources().getColor(R.color.colorPrimary))
                .setPromptStateChangeListener((prompt, state) -> {
                    if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                        prompt.finish();
                    }
                })
                .show();
    }

    private void checkIfBuyTopicButtonVisible() {
        Rect scrollBounds = new Rect();
        nestedScrollView.getHitRect(scrollBounds);
        if (userHomeTopicsLabel.getLocalVisibleRect(scrollBounds)) {
            // Any portion of the imageView, even a single pixel, is within the visible window
            showBuyCreditsTooltip();
        }

    }

    private void showBuyCreditsTooltip() {
        tooltip = Tooltip.make(context,
                new Tooltip.Builder(101)
                        .anchor(userHomeTopicsLabel, Tooltip.Gravity.BOTTOM)
                        .closePolicy(new Tooltip.ClosePolicy()
                                .insidePolicy(true, false)
                                .outsidePolicy(false, false), 5000)
                        .activateDelay(800)
                        .withStyleId(R.style.ToolTipLayoutCustomLightStyle)
                        .showDelay(300)
                        .text("Buy Credits")
                        .maxWidth(500)
                        .withArrow(true)
                        .withOverlay(true).build());
        tooltip.show();

        //.typeface(mYourCustomFont)
        //.floatingAnimation(Tooltip.AnimationBuilder.DEFAULT)

    }

    public void hideTooltipMenu() {
        if (tooltip != null) {
            tooltip.hide();
            tooltip = null;
        }
    }

    /* STATS METHODS */
    private void updateBannerStats(int postId, String postType, boolean isClickCount) {
        if (TextUtils.isEmpty(postType))
            return;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                if (postId != -1) {
                    BannerStatsModel model = abDatabase.bannerStatsDao().fetchingSingleBannerStats(postId);
                    long timeStamp = System.currentTimeMillis();
                    ;
                    if (model != null) {
                        //post already exist in db
                        if (isClickCount) {
                            updateClickCount(model.getId(), postId, postType, model, timeStamp);
                        } else {
                            updateImpressionsCount(model.getId(), postId, postType, model, timeStamp);
                        }

                    } else {
                        //new post impression
                        insertNewEntry(postId, postType, isClickCount, timeStamp);
                    }
                }
            }
        });

    }

    private void updateClickCount(long rowId, int postId, String postType, BannerStatsModel model, long timeStamp) {

        long previousTimeStamp = model.getTimeStamp();

        if (DateUtils.getDate(previousTimeStamp, DateUtils.SERVER_ONLY_DATE_FORMAT).equalsIgnoreCase(DateUtils.getDate(timeStamp, DateUtils.SERVER_ONLY_DATE_FORMAT))) {
            //both date are same means entry is of same day. Update the entry
            int previousCount = model.getClickCount();
            previousCount += 1;
            long id = abDatabase.bannerStatsDao().updateClickCount(rowId, previousCount, timeStamp);
            Logger.DebugLog(TAG, "POST UPDATE CLICK : " + id);
        } else {
            //both date are not same do a new entry
            insertNewEntry(postId, postType, true, timeStamp);
        }

    }

    private void updateImpressionsCount(long rowId, int postId, String postType, BannerStatsModel model, long timeStamp) {
        long previousTimeStamp = model.getTimeStamp();

        if (DateUtils.getDate(previousTimeStamp, DateUtils.SERVER_ONLY_DATE_FORMAT).equalsIgnoreCase(DateUtils.getDate(timeStamp, DateUtils.SERVER_ONLY_DATE_FORMAT))) {
            //both date are same means entry is of same day. Update the entry
            int previousCount = model.getImpressionsCount();
            previousCount += 1;
            long id = abDatabase.bannerStatsDao().updateBannerImpressions(rowId, previousCount, timeStamp);
            Logger.DebugLog(TAG, "POST UPDATE IMPRESSIONS : " + id);
        } else {
            insertNewEntry(postId, postType, false, timeStamp);
        }

    }

    private void insertNewEntry(int postId, String postType, boolean isClickCount, long timeStamp) {
        BannerStatsModel bannerImpressionsModel = new BannerStatsModel();
        bannerImpressionsModel.setPostId(postId);
        bannerImpressionsModel.setTimeStamp(timeStamp);
        bannerImpressionsModel.setPostType(postType);
        if (isClickCount) {
            bannerImpressionsModel.setClickCount(1);
        } else {
            bannerImpressionsModel.setImpressionsCount(1);
        }
        long id = abDatabase.bannerStatsDao().insert(bannerImpressionsModel);
        Logger.DebugLog(TAG, "POST INSERT : " + id);
    }


    private List<Long> statsDateList = new ArrayList<>();

    private void fetchUserStatsData() {
        statsDateList.clear();

        //abDatabase.bannerStatsDao().deleteTable();
        //abDatabase.screenStatsDao().deleteTable();

        ScreenStatsDao screenStatsDao = abDatabase.screenStatsDao();

        List<ScreenStatsModel> screenStatsModelList = screenStatsDao.fetchScreenStats();
        if (screenStatsModelList != null && screenStatsModelList.size() > 0) {
            for (ScreenStatsModel model : screenStatsModelList) {
                if (isDateNoExist(model.getTimeStamp())) {
                    statsDateList.add(model.getTimeStamp());
                }
            }
        }

        BannerStatsDao bannerStatsDao = abDatabase.bannerStatsDao();
        List<BannerStatsModel> bannerStatsModelList = bannerStatsDao.fetchAllBannerStats();

        if (bannerStatsModelList != null && bannerStatsModelList.size() > 0) {
            for (BannerStatsModel model : bannerStatsModelList) {
                if (isDateNoExist(model.getTimeStamp())) {
                    statsDateList.add(model.getTimeStamp());
                }
            }
        }

        if (statsDateList != null && statsDateList.size() > 0) {

            List<PostUserStatsRequest> postUserStatsRequestList = new ArrayList<>();

            for (long timeStamp : statsDateList) {

                PostUserStatsRequest postUserStatsRequest = new PostUserStatsRequest();
                postUserStatsRequest.setUserId(preferenceManger.getUserDetails().getUserId());
                postUserStatsRequest.setStatDate(DateUtils.getDate(timeStamp, DateUtils.SERVER_DATE_FORMAT));
                postUserStatsRequest.setAppVersion(BuildConfig.VERSION_NAME);
                postUserStatsRequest.setSourceId(1);

                String date = DateUtils.getDate(timeStamp, DateUtils.SERVER_ONLY_DATE_FORMAT);

                if (screenStatsModelList != null && screenStatsModelList.size() > 0) {
                    List<ScreenStatsModel> screenStatsModels = new ArrayList<>();
                    for (ScreenStatsModel model : screenStatsModelList) {
                        if (DateUtils.getDate(model.getTimeStamp(), DateUtils.SERVER_ONLY_DATE_FORMAT).equalsIgnoreCase(date)) {
                            screenStatsModels.add(model);
                        }
                    }
                    if (screenStatsModels.size() > 0) {
                        postUserStatsRequest.setScreenStatsModelList(screenStatsModels);
                    } else {
                        postUserStatsRequest.setScreenStatsModelList(new ArrayList<>());
                    }
                } else {
                    postUserStatsRequest.setScreenStatsModelList(new ArrayList<>());
                }

                if (bannerStatsModelList != null && bannerStatsModelList.size() > 0) {
                    List<BannerStatsModel> bannerStatsModels = new ArrayList<>();
                    for (BannerStatsModel model : bannerStatsModelList) {
                        if (DateUtils.getDate(model.getTimeStamp(), DateUtils.SERVER_ONLY_DATE_FORMAT).equalsIgnoreCase(date)) {
                            bannerStatsModels.add(model);
                        }
                    }
                    if (bannerStatsModels.size() > 0) {
                        postUserStatsRequest.setBannerStatsModelList(bannerStatsModels);
                    } else {
                        postUserStatsRequest.setBannerStatsModelList(new ArrayList<>());
                    }
                } else {
                    postUserStatsRequest.setBannerStatsModelList(new ArrayList<>());
                }
                postUserStatsRequestList.add(postUserStatsRequest);
            }
            Logger.DebugLog("STATS DATA", "JSON : " + postUserStatsRequestList.toString());
        }

    }

    /**
     * check if stat date already exist in state_date_list
     *
     * @param timeStamp
     * @return
     */
    private boolean isDateNoExist(long timeStamp) {
        if (statsDateList == null || statsDateList.size() == 0)
            return true;
        String date = DateUtils.getDate(timeStamp, DateUtils.SERVER_ONLY_DATE_FORMAT);
        for (long time : statsDateList) {
            String saveDate = DateUtils.getDate(time, DateUtils.SERVER_ONLY_DATE_FORMAT);
            if (date.equalsIgnoreCase(saveDate)) {
                return false;
            }
        }
        return true;
    }
}

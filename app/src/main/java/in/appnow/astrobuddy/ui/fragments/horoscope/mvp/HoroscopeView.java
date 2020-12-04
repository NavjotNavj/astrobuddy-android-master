package in.appnow.astrobuddy.ui.fragments.horoscope.mvp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jakewharton.rxbinding2.view.RxView;
import com.lukedeighton.wheelview.WheelView;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.adapters.MaterialColorAdapter;
import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.base.BaseView;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.models.SunSignModel;
import in.appnow.astrobuddy.ui.fragments.horoscope_detail.pager.HoroscopeDetailPagerFragment;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.ImageUtils;
import io.reactivex.Observable;
import it.sephiroth.android.library.tooltip.Tooltip;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

import static in.appnow.astrobuddy.app.AstroAppConstants.ITEM_COUNT;

/**
 * Created by sonu on 11:14, 13/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class HoroscopeView extends BaseViewClass implements BaseView {

    private static final String TAG = HoroscopeView.class.getSimpleName();
    private static final int CHAT_ANIMATION_COUNT = 0;
    private static final double CHAT_ANIMATION_DURATION = 5 * 1000;
    @BindView(R.id.wheel_view)
    WheelView wheelView;
    @BindView(R.id.menu_button)
    ImageView menu_button;
    @BindView(R.id.main_layout)
    RelativeLayout main_layout;
    @BindView(R.id.horoscope_chat_fab)
    FloatingActionButton chatButton;
    @BindView(R.id.horoscope_talk_astrobuddy_fab)
    FloatingActionButton horoscopeTalkAstroBuddyButton;
    @BindView(R.id.background_image)
    ImageView backgroundImage;
    @BindView(R.id.tip_of_the_day_card_view)
    RelativeLayout tipOfTheCardView;
    @BindView(R.id.tip_of_day_user_name_label)
    TextView tipOfTheDayUserNameLabel;
    @BindView(R.id.tip_of_day_label)
    TextView tipOfTheDayLabel;
   /* @BindView(R.id.tip_of_day_anim_one)
    LottieAnimationView lottieAnimationViewOne;
    @BindView(R.id.tip_of_day_anim_two)
    LottieAnimationView lottieAnimationViewTwo;*/

    private MaterialColorAdapter adapter;

    private Tooltip.TooltipView tooltipView, forecastTooltip;

    private List<SunSignModel> sunSignModelList;

    private final Context context;

    private int selectedPosition = 0;
    private boolean isWheelVisible;

    @BindDimen(R.dimen.wheel_label_text_size)
    float wheelLabelFontSize;
    @BindDimen(R.dimen.wheel_label_x_axis)
    float centerX;
    @BindDimen(R.dimen.wheel_label_y_axis)
    float centerY;
    @BindDimen(R.dimen.wheel_label_selected_x_axis)
    float selectedCenterX;
    @BindDimen(R.dimen.wheel_label_selected_y_axis)
    float selectedCenterY;

    public HoroscopeView(@NonNull Context context) {
        super(context);
        this.context = context;
        inflate(context, R.layout.fragment_horoscope, this);
        ButterKnife.bind(this, this);
        ImageUtils.setDrawableImage(context, menu_button, R.drawable.half_rim);
        ImageUtils.setBackgroundImage(context, backgroundImage);
        initViews();
    }

    public void initViews() {
        sunSignModelList = SunSignModel.getSunSignList(context);
        final List<Map.Entry<String, Integer>> entries = setWheelAdapter();

        //a listener for receiving a callback for when the item closest to the selection angle changes
        wheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectListener() {
            @Override
            public void onWheelItemSelected(WheelView parent, Drawable itemDrawable, int position) {
                //get the item at this position
                Map.Entry<String, Integer> selectedEntry = ((MaterialColorAdapter) parent.getAdapter()).getItem(position);
                parent.setSelectionColor(getContrastColor());
                //wheelView.setSelectionColor(getContrastColor(entries.get(0)));
            }

            @Override
            public void onWheelItemSettled(WheelView parent, int position) {
                //  parent.setSelectedWithoutAnimation(position);

            }
        });

        wheelView.setOnWheelItemClickListener((parent, position, isSelected) -> {
            if (!isSelected) {
                parent.setSelected(position);
                new Handler().postDelayed(() -> {
                            updateAdapter(position);
                        }
                        , WheelView.VALUE_ANIMATION_DURATION);
            }
        });

        wheelView.setOnWheelItemSelected(this::updateAdapter);


        wheelView.setSelectionColor(getContrastColor());

    }

    private void updateAdapter(int position) {
        try {
            if (selectedPosition != position) {
                selectedPosition = position;
                setWheelAdapter();
            } else {
                openForecastDetail(position);
            }
        } catch (Exception ignored) {

        }
    }

    public void openForecastDetail(int position) {
        if (AstroApplication.getInstance().isInternetConnected(true)) {
            hideTooltipMenu();
            FragmentUtils.onChangeFragment(((AppCompatActivity) context).getSupportFragmentManager(), R.id.container_view, HoroscopeDetailPagerFragment.newInstance(position), FragmentUtils.HOROSCOPE_DETAIL_FRAGMENT);
        }
    }

    private int getContrastColor() {
        return getResources().getColor(android.R.color.holo_orange_dark);
    }

    private List<Map.Entry<String, Integer>> setWheelAdapter() {
        //create data for the adapter
        final List<Map.Entry<String, Integer>> entries = new ArrayList<>(ITEM_COUNT);
        for (int i = 0; i < ITEM_COUNT; i++) {
            SunSignModel sunSignModel = sunSignModelList.get(i);
            entries.add(new AbstractMap.SimpleEntry<>(sunSignModel.getSunSignName(), sunSignModel.getSunIcon()));
        }
        if (adapter == null) {
            adapter = new MaterialColorAdapter(entries, context, selectedPosition, wheelLabelFontSize, selectedCenterX, centerX, selectedCenterY, centerY);
        } else {
            adapter.setSelectedPosition(selectedPosition);
        }

        //populate the adapter, that knows how to draw each item (as you would do with a ListAdapter)
        wheelView.setAdapter(adapter);
        return entries;
    }

    public Observable<Object> observeMenuClickButton() {
        return RxView.clicks(menu_button);
    }

    public Observable<Object> observeMainLayoutClick() {
        return RxView.clicks(main_layout);
    }

    public Observable<Object> observeChatButtonClick() {
        return RxView.clicks(chatButton);
    }

    public Observable<Object> observeChartButtonClick() {
        return RxView.clicks(horoscopeTalkAstroBuddyButton);
    }

    public void showHideContentOnHoroscopeCircleShow(boolean animationEnd) {
        if (animationEnd) {
            wheelView.setVisibility(View.GONE);
            menu_button.setVisibility(View.VISIBLE);
            tipOfTheCardView.setVisibility(View.VISIBLE);
            horoscopeTalkAstroBuddyButton.hide();
        } else {
            wheelView.setVisibility(View.VISIBLE);
            menu_button.setVisibility(View.GONE);
            tipOfTheCardView.setVisibility(View.GONE);
            horoscopeTalkAstroBuddyButton.hide();
        }
    }


    public void openMenu() {
        if (isWheelVisible) {
            closeMenu();
        } else {
            if (forecastTooltip != null) {
                forecastTooltip.hide();
                forecastTooltip = null;
            }
            menu_button.setEnabled(false);
            //  stopRotatingCircle();
            showHideContentOnHoroscopeCircleShow(false);
            // horoscope_title.setVisibility(View.GONE);
            TranslateAnimation animate = new TranslateAnimation(
                    -1000,                 // fromXDelta
                    0,                 // toXDelta
                    0,  // fromYDelta
                    0);                // toYDelta
            animate.setDuration(500);
            animate.setFillAfter(true);
            wheelView.startAnimation(animate);
            animate.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    isWheelVisible = true;
                    menu_button.setEnabled(true);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    public void closeMenu() {
        if (isWheelVisible) {
            menu_button.setEnabled(false);
            TranslateAnimation translateAnimation = new TranslateAnimation(0, -1000, 0, 0);
            translateAnimation.setDuration(500);
            wheelView.startAnimation(translateAnimation);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    showHideContentOnHoroscopeCircleShow(true);
                    //  horoscope_title.setVisibility(View.VISIBLE);
                    // startRotatingCircle();
                    isWheelVisible = false;
                    menu_button.setEnabled(true);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    public void updateTipOfTheDay(String tipOfTheDay) {
        tipOfTheDayUserNameLabel.setVisibility(View.VISIBLE);
        tipOfTheDayLabel.setVisibility(View.VISIBLE);
        tipOfTheDayLabel.setText(tipOfTheDay);
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



    /*  Chat Animation */

    public void animateChatButton() {
        // Load the animation
        Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.shake);

        // Animate the button
        chatButton.startAnimation(myAnim);

        // Run button animation again after it finished
        myAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
                showChatToolTip();
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                //hide tooltip
            }
        });

    }

    public void clearChatAnimation() {
        chatButton.clearAnimation();
    }

    private void showChatToolTip() {
        tooltipView = Tooltip.make(getContext(),
                new Tooltip.Builder(101)
                        .anchor(chatButton, Tooltip.Gravity.TOP)
                        .closePolicy(new Tooltip.ClosePolicy()
                                .insidePolicy(true, false)
                                .outsidePolicy(false, false), 5000)
                        .activateDelay(800)
                        .withStyleId(R.style.ToolTipLayoutCustomStyle)
                        .showDelay(300)
                        .text("Chat with AstroBuddy")
                        .maxWidth(700)
                        .withArrow(true)
                        .withOverlay(true)
                        //.typeface(mYourCustomFont)
                        //.floatingAnimation(Tooltip.AnimationBuilder.DEFAULT)
                        .build());
        tooltipView.show();
    }

    public void animateTalkToAstroBuddyButton() {
        // Load the animation
        Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.shake);

        // Animate the button
        horoscopeTalkAstroBuddyButton.startAnimation(myAnim);

        // Run button animation again after it finished
        myAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
                showTalkToAstroBuddyToolTip();
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                //hide tooltip
            }
        });

    }

    private void showTalkToAstroBuddyToolTip() {
        tooltipView = Tooltip.make(getContext(),
                new Tooltip.Builder(101)
                        .anchor(horoscopeTalkAstroBuddyButton, Tooltip.Gravity.TOP)
                        .closePolicy(new Tooltip.ClosePolicy()
                                .insidePolicy(true, false)
                                .outsidePolicy(false, false), 5000)
                        .activateDelay(800)
                        .withStyleId(R.style.ToolTipLayoutCustomStyle)
                        .showDelay(300)
                        .text("Talk to AstroBuddy")
                        .maxWidth(700)
                        .withArrow(true)
                        .withOverlay(true)
                        .withCallback(new Tooltip.Callback() {
                            @Override
                            public void onTooltipClose(Tooltip.TooltipView tooltipView, boolean b, boolean b1) {
                                animateChatButton();
                            }

                            @Override
                            public void onTooltipFailed(Tooltip.TooltipView tooltipView) {

                            }

                            @Override
                            public void onTooltipShown(Tooltip.TooltipView tooltipView) {

                            }

                            @Override
                            public void onTooltipHidden(Tooltip.TooltipView tooltipView) {

                            }
                        })
                        .build());
        tooltipView.show();
    }

    public void showForecastTooltip() {
        if (isWheelVisible)
            return;
        forecastTooltip = Tooltip.make(getContext(),
                new Tooltip.Builder(101)
                        .anchor(menu_button, Tooltip.Gravity.RIGHT)
                        .closePolicy(new Tooltip.ClosePolicy()
                                .insidePolicy(true, false)
                                .outsidePolicy(false, false), 5000)
                        .activateDelay(800)
                        .withStyleId(R.style.ToolTipLayoutCustomStyle)
                        .showDelay(300)
                        .text("Prediction")
                        .maxWidth(700)
                        .withArrow(true)
                        .withOverlay(true)
                        //.typeface(mYourCustomFont)
                        //.floatingAnimation(Tooltip.AnimationBuilder.DEFAULT)
                        .build());
        forecastTooltip.show();
    }

    public void hideTooltipMenu() {
        if (tooltipView != null) {
            tooltipView.hide();
            tooltipView = null;
        }
        if (forecastTooltip != null) {
            forecastTooltip.hide();
            forecastTooltip = null;
        }
    }


    /* Tutorial Tap Prompt */

    public void startStopLottieAnimation(boolean isPlay) {
       /* if (isPlay) {
            lottieAnimationViewOne.playAnimation();
            lottieAnimationViewTwo.playAnimation();
        } else {
            lottieAnimationViewOne.pauseAnimation();
            lottieAnimationViewTwo.pauseAnimation();
        }*/
    }

    public void showChatPrompt() {
        new MaterialTapTargetPrompt.Builder(((AppCompatActivity) context))
                .setTarget(chatButton)
                .setPrimaryText("Chat")
                .setSecondaryText("Chat with AstroBuddy.")
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                .setAutoDismiss(false)
                .setAutoFinish(false)
                .setBackgroundColour(context.getResources().getColor(R.color.colorAccent))
                .setCaptureTouchEventOutsidePrompt(true)
                .setPromptStateChangeListener((prompt, state) -> {
                    if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                        prompt.finish();
                    }
                })
                .show();
    }

    public void showHoroscopePrompt() {
        new MaterialTapTargetPrompt.Builder(((AppCompatActivity) context))
                .setTarget(menu_button)
                .setPrimaryText("AstroBuddy Prediction")
                .setBackButtonDismissEnabled(true)//catch backpress
                .setSecondaryText("Check your daily, monthly and yearly prediction here.")
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                .setAutoDismiss(true)
                .setPrimaryTextSize(R.dimen.wheel_view_rim_prompt_text_primary_size)
                .setSecondaryTextSize(R.dimen.wheel_view_rim_prompt_text_secondary_size)
                .setBackgroundColour(context.getResources().getColor(R.color.colorAccent))
                .setMaxTextWidth(R.dimen.wheel_view_rim_prompt_text_width)
                .setAutoFinish(false)
                //.setFocalColour(Color.TRANSPARENT)
                //.setPromptBackground(new FullscreenPromptBackground())
                //.setPromptFocal(new RectanglePromptFocal())
                .setFocalRadius(R.dimen.wheel_view_rim_prompt)
                .setCaptureTouchEventOutsidePrompt(true)
                .setPromptStateChangeListener((prompt, state) -> {
                    if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                        prompt.finish();
                    }
                })
                .show();
    }

    public void showTalkToAstroBuddyPrompt() {
        closeMenu();
        new MaterialTapTargetPrompt.Builder(((AppCompatActivity) context))
                .setTarget(horoscopeTalkAstroBuddyButton)
                .setPrimaryText("Talk")
                .setSecondaryText("Speak with AstroBuddy.")
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                .setAutoDismiss(false)
                .setAutoFinish(false)
                .setBackgroundColour(context.getResources().getColor(R.color.colorAccent))
                .setCaptureTouchEventOutsidePrompt(true)
                .setPromptStateChangeListener((prompt, state) -> {
                    if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                        prompt.finish();
                    }
                })
                .show();
    }

}

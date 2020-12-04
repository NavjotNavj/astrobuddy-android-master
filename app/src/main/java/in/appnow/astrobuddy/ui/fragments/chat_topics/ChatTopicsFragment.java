package in.appnow.astrobuddy.ui.fragments.chat_topics;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import javax.inject.Inject;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.base.BaseFragment;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.ui.activities.home.HomeActivity;
import in.appnow.astrobuddy.ui.fragments.chat_topics.mvp.ChatTopicsPresenter;
import in.appnow.astrobuddy.ui.fragments.chat_topics.mvp.view.ChatTopicsView;
import it.sephiroth.android.library.tooltip.Tooltip;

/**
 * Created by sonu on 15:43, 17/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ChatTopicsFragment extends BaseFragment {

    private final static String ARG_BACK_BUTTON_SHOW = "back_button_show";

    @Inject
    ChatTopicsView view;
    @Inject
    ChatTopicsPresenter presenter;

    private boolean isBackButtonShow;
    MenuItem chatHistoryItem;
    private Tooltip.TooltipView tooltip;
    Context context;

    public static ChatTopicsFragment newInstance(boolean isBackButtonShow) {

        Bundle args = new Bundle();
        args.putBoolean(ARG_BACK_BUTTON_SHOW, isBackButtonShow);
        ChatTopicsFragment fragment = new ChatTopicsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.context = context;
            showHideToolbar(true);
            updateToolbarTitle(context.getResources().getString(R.string.action_ab_chat));
            hideBottomBar(true);
        } catch (Exception ignored) {

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isBackButtonShow = getArguments().getBoolean(ARG_BACK_BUTTON_SHOW, false);
            showHideBackButton(isBackButtonShow);
        } else
            showHideBackButton(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((HomeActivity) getActivity()).getComponent().inject(this);
        setHasOptionsMenu(true);
        presenter.onCreate();
        // checkIfBackPromptIsShown();
        return view;
    }

    private void checkIfBackPromptIsShown() {
        if (presenter != null) {
            PreferenceManger preferenceManger = presenter.getPreferenceManger();
            if (preferenceManger != null) {
                if (preferenceManger.getBooleanValue(PreferenceManger.CHAT_TAP_HINT) && !preferenceManger.getBooleanValue(PreferenceManger.CHAT_BACK_PRESS_TAP_HINT)) {
                    // showToolbarBackPrompt(R.drawable.ic_action_navigation_white);
                    preferenceManger.putBoolean(PreferenceManger.CHAT_BACK_PRESS_TAP_HINT, true);
                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.chat_history_menu, menu);

//        chatHistoryItem = menu.findItem(R.id.action_history);
//        chatHistoryItem.setActionView(R.layout.custom_menu_item);
//        chatHistoryItem.getActionView().setPadding(0,0,0, (int) getResources().getDimension(R.dimen._1sdp));
//        ImageView history = chatHistoryItem.getActionView().findViewById(R.id.history_image_view);
//        history.setOnClickListener(view -> {
//            if (AstroApplication.getInstance().isInternetConnected(true)) {
//                presenter.replaceChatHistoryFragment();
//            }
//        });
//        showChatHistoryToolTip();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_history:
                if (AstroApplication.getInstance().isInternetConnected(true)) {
                    presenter.replaceChatHistoryFragment();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        Toolbar scToolbar = ((HomeActivity) getActivity()).getToolbar();

        scToolbar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                scToolbar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                View menuItem = scToolbar.findViewById(R.id.action_history);
                showChatHistoryToolTip(menuItem);
            }
        });
    }
    private void showChatHistoryToolTip(View menu) {
        tooltip = Tooltip.make(context,
                new Tooltip.Builder(109)
                        .anchor(menu, Tooltip.Gravity.BOTTOM)
                        .closePolicy(new Tooltip.ClosePolicy()
                                .insidePolicy(true, false)
                                .outsidePolicy(false, false), 5000)
                        .activateDelay(800)
                        .withStyleId(R.style.ToolTipLayoutCustomLightStyle)
                        .showDelay(300)
                        .text("Chat History")
                        .maxWidth(500)
                        .withArrow(true)
                        .withOverlay(true).build());
        tooltip.show();

        //.typeface(mYourCustomFont)
        //.floatingAnimation(Tooltip.AnimationBuilder.DEFAULT)

    }

    @Override
    public void onDetach() {
        // showHideBackButton(false);
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }
}

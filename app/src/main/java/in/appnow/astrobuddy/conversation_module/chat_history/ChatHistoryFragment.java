package in.appnow.astrobuddy.conversation_module.chat_history;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import javax.inject.Inject;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseFragment;
import in.appnow.astrobuddy.conversation_module.chat_history.mvp.ChatHistoryPresenter;
import in.appnow.astrobuddy.conversation_module.chat_history.mvp.view.ChatHistoryView;
import in.appnow.astrobuddy.ui.activities.home.HomeActivity;

/**
 * Created by sonu on 13:56, 31/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ChatHistoryFragment extends BaseFragment {

    @Inject
    ChatHistoryView view;
    @Inject
    ChatHistoryPresenter presenter;

    public static ChatHistoryFragment newInstance() {

        Bundle args = new Bundle();

        ChatHistoryFragment fragment = new ChatHistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        showHideToolbar(true);
        updateToolbarTitle(context.getResources().getString(R.string.chat_history));
        hideBottomBar(true);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((HomeActivity) getActivity()).getComponent().inject(this);
        showHideBackButton(true);
        presenter.onCreate();
        return view;
    }

    @Override
    public void onDetach() {
        showHideBackButton(true);
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }
}

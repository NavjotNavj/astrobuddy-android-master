package in.appnow.astrobuddy.conversation_module.chat_history.single_chat_history;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;

import javax.inject.Inject;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseFragment;
import in.appnow.astrobuddy.conversation_module.chat_history.single_chat_history.mvp.SingleChatHistoryPresenter;
import in.appnow.astrobuddy.conversation_module.chat_history.single_chat_history.mvp.view.SingleChatHistoryView;
import in.appnow.astrobuddy.rest.response.Conversations;
import in.appnow.astrobuddy.ui.activities.home.HomeActivity;
import in.appnow.astrobuddy.utils.DateUtils;

/**
 * Created by sonu on 13:56, 31/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class SingleChatHistoryFragment extends BaseFragment {

    public static final String CHAT_DATA = "chat_data";
    private Context context;

    @Inject
    SingleChatHistoryView view;
    @Inject
    SingleChatHistoryPresenter presenter;


    public static SingleChatHistoryFragment newInstance(Conversations conversations) {
        Bundle args = new Bundle();
        args.putParcelable(CHAT_DATA, conversations);
        SingleChatHistoryFragment fragment = new SingleChatHistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((HomeActivity) getActivity()).getComponent().inject(this);
        showHideBackButton(true);
        presenter.onCreate();
        if (getArguments() != null) {
            Conversations conversations = getArguments().getParcelable(CHAT_DATA);
            showHideToolbar(true);
            updateToolbarTitle(String.format(context.getResources().getString(R.string.chat_history_date), DateUtils.parseStringDate(conversations.getStartTimestamp(), DateUtils.SERVER_DATE_FORMAT,DateUtils.SINGLE_CHAT_DATE_FORMAT)));
            presenter.loadChats(conversations);
        }
        return view;
    }

    @Override
    public void onDetach() {
        showHideBackButton(false);
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }
}

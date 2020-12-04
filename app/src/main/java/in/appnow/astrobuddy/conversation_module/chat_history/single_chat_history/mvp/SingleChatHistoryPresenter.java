package in.appnow.astrobuddy.conversation_module.chat_history.single_chat_history.mvp;

import in.appnow.astrobuddy.base.BasePresenter;
import in.appnow.astrobuddy.conversation_module.chat_history.single_chat_history.mvp.view.SingleChatHistoryView;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dialog.UnAuthoriseUserDialog;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.rest.response.Conversations;
import in.appnow.astrobuddy.utils.FragmentUtils;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by sonu on 14:01, 31/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class SingleChatHistoryPresenter implements BasePresenter {
    private final SingleChatHistoryView view;
    private final SingleChatHistoryModel model;
    private final PreferenceManger preferenceManger;
    private ABDatabase abDatabase;
    private final CompositeDisposable disposable = new CompositeDisposable();

    public SingleChatHistoryPresenter(SingleChatHistoryView view, SingleChatHistoryModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        this.view = view;
        this.model = model;
        this.preferenceManger = preferenceManger;
        this.abDatabase = abDatabase;
    }

    @Override
    public void onCreate() {

    }


    @Override
    public void onDestroy() {
        disposable.clear();
    }

    @Override
    public void unAuthorizeUserAccess(String message) {
        UnAuthoriseUserDialog.getInstance().showLogOutDialog(
                model.getAppCompatActivity(),
                message,
                preferenceManger,
                abDatabase,
                new UnAuthoriseUserDialog.OnLogoutListener() {
                    @Override
                    public void OnClickLogOut() {
                        model.showProgressBar();
                    }

                    @Override
                    public void OnLogOut() {
                        model.hideProgressBar();
                        FragmentUtils.onLogoutSuccess(model.getAppCompatActivity());
                    }
                });
    }

    public void loadChats(Conversations conversations) {
        view.loadChats(conversations, preferenceManger);
    }
}

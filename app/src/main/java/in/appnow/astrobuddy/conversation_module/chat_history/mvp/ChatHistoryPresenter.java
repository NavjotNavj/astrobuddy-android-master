package in.appnow.astrobuddy.conversation_module.chat_history.mvp;

import in.appnow.astrobuddy.base.BasePresenter;
import in.appnow.astrobuddy.conversation_module.chat_history.mvp.view.ChatHistoryView;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dialog.UnAuthoriseUserDialog;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.rest.CallbackWrapper;
import in.appnow.astrobuddy.rest.request.BaseRequestModel;
import in.appnow.astrobuddy.rest.response.ChatHistoryResponse;
import in.appnow.astrobuddy.utils.FragmentUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by sonu on 14:01, 31/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ChatHistoryPresenter implements BasePresenter {
    private final ChatHistoryView view;
    private final ChatHistoryModel model;
    private final PreferenceManger preferenceManger;
    private ABDatabase abDatabase;

    private final CompositeDisposable disposable = new CompositeDisposable();

    public ChatHistoryPresenter(ChatHistoryView view, ChatHistoryModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        this.view = view;
        this.model = model;
        this.preferenceManger = preferenceManger;
        this.abDatabase = abDatabase;
    }

    @Override
    public void onCreate() {
        disposable.add(fetchChatHistory());
        disposable.add(seeAllChats());
    }

    private Disposable fetchChatHistory() {
        model.showProgressBar();
        BaseRequestModel requestModel = new BaseRequestModel();
        requestModel.setUserId(preferenceManger.getUserDetails().getUserProfile().getUserId());
        return model.getChatHistory(requestModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> model.hideProgressBar())
                .subscribeWith(new CallbackWrapper<ChatHistoryResponse>(view, this) {
                    @Override
                    protected void onSuccess(ChatHistoryResponse chatHistoryResponse) {
                        view.loadChats(chatHistoryResponse);
                    }
                });

    }

    private Disposable seeAllChats() {
        return view.onViewAllChats()
                .subscribe(model::openSingleChatFragment);
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
}

package in.appnow.astrobuddy.ui.fragments.call_plans.call_plan_history.mvp;

import in.appnow.astrobuddy.base.BasePresenter;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dialog.UnAuthoriseUserDialog;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.rest.CallbackWrapper;
import in.appnow.astrobuddy.rest.request.BaseRequestModel;
import in.appnow.astrobuddy.rest.response.CallConversationHistory;
import in.appnow.astrobuddy.rest.response.CallHistoryResponse;
import in.appnow.astrobuddy.ui.fragments.call_plans.call_plan_history.mvp.view.CallPlansHistoryView;
import in.appnow.astrobuddy.utils.FragmentUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by NILESH BHARODIYA on 03-09-2019.
 */
public class CallPlansHistoryPresenter implements BasePresenter {

    private final CallPlansHistoryView view;
    private final CallPlansHistoryModel model;
    private final PreferenceManger preferenceManger;
    private ABDatabase abDatabase;

    private final CompositeDisposable disposable = new CompositeDisposable();


    public CallPlansHistoryPresenter(CallPlansHistoryView view, CallPlansHistoryModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        this.view = view;
        this.model = model;
        this.preferenceManger = preferenceManger;
        this.abDatabase = abDatabase;
    }

    public PreferenceManger getPreferenceManger() {
        return preferenceManger;
    }

    @Override
    public void onCreate() {
        disposable.add(loadHistory());
        disposable.add(onRateNowButtonClick());
    }

    private Disposable loadHistory() {

        model.showProgressBar();

        BaseRequestModel baseRequestModel = new BaseRequestModel();

        baseRequestModel.setUserId(preferenceManger.getUserDetails().getUserProfile() != null ?
                preferenceManger.getUserDetails().getUserProfile().getUserId() :
                "0");

        return model.getCallPlanHistoryResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> model.hideProgressBar())
                .subscribeWith(new CallbackWrapper<CallHistoryResponse>(view, this) {
                    @Override
                    protected void onSuccess(CallHistoryResponse callHistoryResponse) {
                        if (callHistoryResponse != null) {
                            view.updateView(callHistoryResponse);
                        }
                    }
                });
    }

    private Disposable onRateNowButtonClick() {
        return view.onRateNowButtonClick().subscribe(this::showRateNowDialog);
    }

    private void showRateNowDialog(CallConversationHistory conversationHistory) {
        model.showRateNowDialog(conversationHistory);
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

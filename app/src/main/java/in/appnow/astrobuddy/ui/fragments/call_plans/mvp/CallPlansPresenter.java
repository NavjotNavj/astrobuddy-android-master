package in.appnow.astrobuddy.ui.fragments.call_plans.mvp;

import java.util.Locale;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BasePresenter;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dialog.DialogHelperClass;
import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.dialog.UnAuthoriseUserDialog;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.payment.utility.Constants;
import in.appnow.astrobuddy.rest.CallbackWrapper;
import in.appnow.astrobuddy.rest.request.AbortPaymentRequest;
import in.appnow.astrobuddy.rest.request.AuthorizePaymentRequest;
import in.appnow.astrobuddy.rest.request.BaseRequestModel;
import in.appnow.astrobuddy.rest.request.CaptureCallPaymentRequest;
import in.appnow.astrobuddy.rest.request.InitiatePaymentRequest;
import in.appnow.astrobuddy.rest.response.BaseResponseModel;
import in.appnow.astrobuddy.rest.response.CallPlan;
import in.appnow.astrobuddy.rest.response.CallPlansResponse;
import in.appnow.astrobuddy.rest.response.InitiatePaymentResponse;
import in.appnow.astrobuddy.ui.fragments.call_plans.mvp.view.CallPlansView;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by NILESH BHARODIYA on 28-08-2019.
 */
public class CallPlansPresenter implements BasePresenter {

    private final CallPlansView view;
    private final CallPlansModel model;
    private final PreferenceManger preferenceManger;
    private final ABDatabase abDatabase;

    // Payment
    private String orderId = "";
    private String currency = "";
    private double amount = 0;
    private String selectedCallPlanId = "";
    private String selectedCallMinutes = "";
    private String selectedPlanName = "";
    private String callNumber = "";

    private final CompositeDisposable disposable = new CompositeDisposable();

    public CallPlansPresenter(CallPlansView view, CallPlansModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
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
        disposable.add(loadCallPlans());
        disposable.add(onKnowMoreClick());
        disposable.add(onBuyButtonClick());
    }

    private Disposable loadCallPlans() {

        model.showProgressBar();
        BaseRequestModel baseRequestModel = new BaseRequestModel();

        baseRequestModel.setUserId(preferenceManger.getUserDetails().getUserProfile() != null ?
                preferenceManger.getUserDetails().getUserProfile().getUserId() :
                "0");

        return model.getCallPlanResponse(baseRequestModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> model.hideProgressBar())
                .subscribeWith(new CallbackWrapper<CallPlansResponse>(view, this) {

                    @Override
                    protected void onSuccess(CallPlansResponse callPlansResponse) {
                        if (callPlansResponse != null) {
                            view.updateView(callPlansResponse);
                        }
                    }
                });
    }

    private Disposable onKnowMoreClick() {
        return view.onKnowMoreClick().subscribe(this::openKnowMore);
    }

    private Disposable onBuyButtonClick() {
        return view.onBuyButtonClick().subscribe(this::buyPlanButtonClick);
    }

    private void buyPlanButtonClick(CallPlan callPlan) {
        if (callPlan.getAvailable() == 1) {
            callPlan.setMobileNumber(getNumber());
            callPlan.setCountryCode(getCountryCode());
            model.showBuyPlanDialog(callPlan
                    , (callMinutes,
                       planAmount,
                       callPlan1) ->
                            disposable.add(onBuyNowFlow(callMinutes, planAmount, callPlan)));

        } else {

            DialogHelperClass.showMessageOKCancel(model.getAppCompatActivity(),
                    model.getAppCompatActivity().getResources().getString(R.string.astrobuddy_unavailable),
                    "Continue",
                    "Cancel",
                    (dialogInterface, i) -> {
                        callPlan.setMobileNumber(getNumber());
                        callPlan.setCountryCode(getCountryCode());
                        model.showBuyPlanDialog(callPlan
                                , (callMinutes,
                                   planAmount,
                                   callPlan1) ->
                                        disposable.add(onBuyNowFlow(callMinutes, planAmount, callPlan)));

                    }, (dialogInterface, i) -> {

                    });

            /*
            Toast.makeText(view.getContext(), callPlan.getName() + " is not available right now" +
                            ".\nPlease try after some time!",
                    Toast.LENGTH_SHORT).show();
            */
        }
    }

    private String getNumber() {
        return preferenceManger.getUserDetails() != null ?
                preferenceManger.getUserDetails().getUserProfile() != null ?
                        preferenceManger.getUserDetails().getUserProfile().getMobileNumber() :
                        "" : "";
    }

    private String getCountryCode() {
        return preferenceManger.getUserDetails() != null ?
                preferenceManger.getUserDetails().getUserProfile() != null ?
                        preferenceManger.getUserDetails().getUserProfile().getCountryCode() :
                        "" : "";
    }

    private void openKnowMore(CallPlan callPlan) {
        model.showKnowMoreDialog(callPlan);
    }

    public void replaceHistoryFragment() {
        model.replaceCallPlanHistory();
    }

    // Payment

    private Disposable onBuyNowFlow(String callMinutes, String planAmount, CallPlan callPlan) {
        model.showProgressBar();

        InitiatePaymentRequest initiatePaymentRequest = new InitiatePaymentRequest();
        initiatePaymentRequest.setAmount(String.format(
                Locale.getDefault(),
                "%.02f",
                amount = Float.valueOf(planAmount)));
        initiatePaymentRequest.setCurrency(currency = "INR");
        initiatePaymentRequest.setUniqueReferenceId("sjslakjdlksjlk1312lkdsdjalksdj");

        selectedCallMinutes = callMinutes;
        selectedCallPlanId = callPlan.getPlanId();
        selectedPlanName = callPlan.getName();
        callNumber = callPlan.getMobileNumber();

        return model.initiatePayment(initiatePaymentRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> model.hideProgressBar())
                .subscribeWith(new CallbackWrapper<InitiatePaymentResponse>(view, this) {
                    @Override
                    protected void onSuccess(InitiatePaymentResponse response) {
                        if (response != null) {
                            if (!response.isErrorStatus()) {

                                orderId = response.getResponseData().getCustomReferenceId();
                                if (!currency.equalsIgnoreCase("INR")) {
                                    // Initiate for no-INR currency payment, after showing dialog

                                } else {
                                    model.startPayment(
                                            preferenceManger.getUserDetails().getUserProfile().getFullName(),
                                            preferenceManger.getUserDetails().getUserProfile().getEmail(),
                                            preferenceManger.getUserDetails().getUserProfile().getMobileNumber(),
                                            response.getResponseData().getCustomReferenceId(),
                                            (int) (amount * 100),
                                            currency);
                                }

                            } else {
                                ToastUtils.shortToast(response.getErrorMessage());
                            }
                        }
                    }
                });

    }

    public void onPaymentDone(int code, String pgPaymentId, String status) {
        if (status.equalsIgnoreCase("success")) {
            disposable.add(onSuccessPaymentStatus(code, pgPaymentId, status));
        } else {
            disposable.add(onAbortPaymentStatus(code, pgPaymentId, status));
        }
    }


    private Disposable onAbortPaymentStatus(int code, String pgPaymentId, String status) {

        AbortPaymentRequest abortPaymentRequest = new AbortPaymentRequest();
        abortPaymentRequest.setCustomReferenceId(orderId);

        return model.abortPaymentStatus(abortPaymentRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> ProgressDialogFragment.dismissProgress(model.getAppCompatActivity().getSupportFragmentManager()))
                .subscribeWith(new CallbackWrapper<BaseResponseModel>(view, this) {
                    @Override
                    protected void onSuccess(BaseResponseModel data) {
                        if (data != null) {
                            if (data.isErrorStatus()) {
                                model.showAlertOnTransactionFailed(data.getErrorMessage());
                            } else {
                                if (status.equalsIgnoreCase("success")) {
                                    //Transaction success
                                    model.openResultActivity(false, Constants.SUCCESS, orderId, pgPaymentId, data.getErrorMessage());
                                } else {
                                    model.showAlertOnTransactionFailed(data.getErrorMessage());
                                }
                            }
                        }
                    }
                });

    }


    private Disposable onSuccessPaymentStatus(int code, String pgPaymentId, String status) {

        AuthorizePaymentRequest authorizePaymentRequest = new AuthorizePaymentRequest();
        authorizePaymentRequest.setCustomReferenceId(orderId);
        authorizePaymentRequest.setPaymentId(pgPaymentId);

        return model.authorizePayment(authorizePaymentRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> ProgressDialogFragment.dismissProgress(model.getAppCompatActivity().getSupportFragmentManager()))
                .subscribeWith(new CallbackWrapper<BaseResponseModel>(view, this) {
                    @Override
                    protected void onSuccess(BaseResponseModel data) {
                        if (data != null) {
                            if (data.isErrorStatus()) {
                                model.showAlertOnTransactionFailed(data.getErrorMessage());
                            } else {
                                if (status.equalsIgnoreCase("success")) {
                                    //Transaction success
                                    disposable.add(onCapturePaymentStatus(pgPaymentId));
                                    // model.openResultActivity(false, Constants.SUCCESS, orderId, pgPaymentId, data.getErrorMsg());
                                } else {
                                    model.showAlertOnTransactionFailed(data.getErrorMessage());
                                }
                            }
                        }
                    }
                });

    }


    private Disposable onCapturePaymentStatus(String pgPaymentId) {
        CaptureCallPaymentRequest capturePaymentRequest = new CaptureCallPaymentRequest();
        capturePaymentRequest.setCustomReferenceId(orderId);
        capturePaymentRequest.setPaymentId(pgPaymentId);
        capturePaymentRequest.setTopics("0");
        capturePaymentRequest.setCallMinutes(selectedCallMinutes.split(" ")[0]);
        capturePaymentRequest.setPlanId(selectedCallPlanId);
        capturePaymentRequest.setAmount(String.format("%.02f", amount));
        capturePaymentRequest.setPlanName(selectedPlanName);
        capturePaymentRequest.setCallNumber(callNumber);

        return model.captureCallPayment(capturePaymentRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> ProgressDialogFragment.dismissProgress(model.getAppCompatActivity().getSupportFragmentManager()))
                .subscribeWith(new CallbackWrapper<BaseResponseModel>(view, this) {
                    @Override
                    protected void onSuccess(BaseResponseModel data) {
                        if (data != null) {
                            if (data.isErrorStatus()) {
                                model.showAlertOnTransactionFailed(data.getErrorMessage());
                            } else {
                                model.openResultActivity(false, Constants.SUCCESS, orderId, pgPaymentId,
                                        successMessage());
                            }
                        }
                    }
                });

    }

    public String successMessage() {
        String message = "Your payment has been processed successfully for a <b>"
                + selectedCallMinutes.split(" ")[0] + " minutes </b> call plan with <b>"
                + selectedPlanName + "</b>. <br> <br> You will receive a call at the registered mobile number in next 10 to 15 minutes (9 AM to 9 PM only)";

        return message;
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

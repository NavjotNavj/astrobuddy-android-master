package in.appnow.astrobuddy.ui.fragments.addcredits.mvp;

import android.text.TextUtils;

import in.appnow.astrobuddy.app.AstroAppConstants;
import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.base.BasePresenter;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.dialog.UnAuthoriseUserDialog;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.payment.utility.Constants;
import in.appnow.astrobuddy.rest.CallbackWrapper;
import in.appnow.astrobuddy.rest.request.AbortPaymentRequest;
import in.appnow.astrobuddy.rest.request.AuthorizePaymentRequest;
import in.appnow.astrobuddy.rest.request.BaseRequestModel;
import in.appnow.astrobuddy.rest.request.CapturePaymentRequest;
import in.appnow.astrobuddy.rest.request.InitiatePaymentRequest;
import in.appnow.astrobuddy.rest.request.ProcessTransactionRequest;
import in.appnow.astrobuddy.rest.response.ApplyPromoCodeResponse;
import in.appnow.astrobuddy.rest.response.BaseResponseModel;
import in.appnow.astrobuddy.rest.response.InitiatePaymentResponse;
import in.appnow.astrobuddy.rest.response.MyAccountResponse;
import in.appnow.astrobuddy.rest.response.ProcessTransactionResponse;
import in.appnow.astrobuddy.rest.response.UserPaymentCheckResponse;
import in.appnow.astrobuddy.ui.activities.promo_code.PromoCodeActivity;
import in.appnow.astrobuddy.ui.fragments.addcredits.AddCreditsFragment;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by sonu on 11:00, 12/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class AddCreditsPresenter implements BasePresenter {
    private static final String TAG = AddCreditsPresenter.class.getSimpleName();
    private final AddCreditsView view;
    private final AddCreditsModel model;
    private final ABDatabase abDatabase;
    private final PreferenceManger preferenceManger;
    private String currency = "";
    private String orderId = "";
    private int totalAmount = 0;
    private final CompositeDisposable disposable = new CompositeDisposable();

    public AddCreditsPresenter(AddCreditsView view, AddCreditsModel model, ABDatabase abDatabase, PreferenceManger preferenceManger) {
        this.view = view;
        this.model = model;
        this.abDatabase = abDatabase;
        this.preferenceManger = preferenceManger;
    }

    public void setTopicsCount(int topicsCount) {
        if (topicsCount == 0) {
            disposable.add(getMyAccountDetails());
        }
        view.updateTopicsCount(topicsCount);
    }

    public PreferenceManger getPreferenceManger() {
        return preferenceManger;
    }

    @Override
    public void onCreate() {
        view.updateEditTextTopicText("");
        view.updatePromoCodeViews(null, "", 1);
        disposable.add(fetchTopicRate());
        disposable.add(observeChatTopicsInput());
        disposable.add(observePromoCodeButtonClick());
        disposable.add(observeViewPlansButtonClick());
        disposable.add(observeRemovePromoCode());
        disposable.add(observeMakePaymentButtonClick());
    }

    private Disposable getMyAccountDetails() {
        BaseRequestModel baseRequestModel = new BaseRequestModel();
        baseRequestModel.setUserId(preferenceManger.getUserDetails().getUserProfile().getUserId());
        return model.getMyAccountDetails(baseRequestModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new CallbackWrapper<MyAccountResponse>(view, this) {
                    @Override
                    protected void onSuccess(MyAccountResponse data) {
                        if (data != null) {
                            view.updateTopicsCount(data.getAccountDetails().getUserTopics());
                        }
                    }
                });
    }

    private Disposable fetchTopicRate() {
        ProgressDialogFragment.showProgress(model.getAppCompatActivity().getSupportFragmentManager());
        BaseRequestModel responseModel = new BaseRequestModel();
        responseModel.setUserId(preferenceManger.getUserDetails().getUserProfile().getUserId());
        return model.getTopicRate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> ProgressDialogFragment.dismissProgress(model.getAppCompatActivity().getSupportFragmentManager()))
                .subscribeWith(new CallbackWrapper<UserPaymentCheckResponse>(view, this) {
                    @Override
                    protected void onSuccess(UserPaymentCheckResponse data) {
                        if (data != null) {
                            if (data.isErrorStatus()) {
                                ToastUtils.shortToast(data.getErrorMessage());
                            } else {
                                currency = data.getCheckUserPaymentInfo().getCurr();
                                view.updateRateChart(data.getCheckUserPaymentInfo());
                            }
                        }
                    }
                });
    }

    /*private Disposable observeMakePaymentButtonClick() {
        final String[] paymentMode = {""};
        return view.observeMakePaymentButton()
                .map(isConnected -> view.isChatTopicEntered() && AstroApplication.getInstance().isInternetConnected(true))
                .subscribe(isConnected -> {
                    if (isConnected) {
                        KeyboardUtils.hideSoftKeyboard(model.getAppCompatActivity());
                        model.showChoosePaymentModeDialog(view.getPaymentModeArray(), 0, (dialogInterface, i) -> {
                            switch (i) {
                                case 0:
                                    paymentMode[0] = AstroAppConstants.RAZOR_PAY;
                                    break;
                                case 1:
                                    paymentMode[0] = AstroAppConstants.CCAVENUE;
                                    break;
                            }
                        }, (dialogInterface, i) -> {
                            if (!TextUtils.isEmpty(paymentMode[0]))
                                disposable.add(doProcessTransaction(paymentMode[0]));
                            else ToastUtils.shortToast("Please select payment mode.");
                        });
                    }
                });
    }*/
    private Disposable observeMakePaymentButtonClick() {
        return view.observeMakePaymentButton()
                .doOnNext(__ -> ProgressDialogFragment.showProgress(model.getAppCompatActivity().getSupportFragmentManager()))
                .map(isConnected -> view.isChatTopicEntered() && AstroApplication.getInstance().isInternetConnected(true))
                .observeOn(Schedulers.io())
                .switchMap(isConnected -> {
                    if (isConnected) {
                        ProcessTransactionRequest request = new ProcessTransactionRequest();
                        request.setPromoPurpose(AstroAppConstants.TRANSACT_TYPE);
                        request.setAction(AstroAppConstants.ADD_TOPICS);

                        request.setPromoId(view.getPromoId());
                        request.setPromoCode(view.getPromoCode());

                        request.setAmount(view.getApplyPromoCodeResponse() != null ? view.getApplyPromoCodeResponse().getAmount() : String.format("%.02f", view.getTotalAmount()));
                        request.setTopicCount(view.getApplyPromoCodeResponse() != null ? view.getApplyPromoCodeResponse().getTopicsCount() : String.valueOf(view.getTopicCount()));

                        request.setFinalAmount(String.format("%.02f", view.getTotalAmount()));
                        request.setFinalTopicCount(String.valueOf(view.getTopicCount() + ((view.getApplyPromoCodeResponse() != null && !TextUtils.isEmpty(view.getApplyPromoCodeResponse().getPromoTopicsCount())) ? Integer.parseInt(view.getApplyPromoCodeResponse().getPromoTopicsCount()) : 0)));

                        request.setUserId(preferenceManger.getUserDetails().getUserProfile().getUserId());
                        request.setCurrency(currency);
                        request.setPgName("rzp");

                        request.setAmountINR(String.format("%.02f", view.getAmountINR()));
                        request.setFinalAmountINR(String.format("%.02f", view.getFinalAmountINR()));

                        InitiatePaymentRequest initiatePaymentRequest = new InitiatePaymentRequest();
                        initiatePaymentRequest.setAmount(String.format("%.02f", view.getTotalAmount()));
                        initiatePaymentRequest.setCurrency(currency);
                        initiatePaymentRequest.setUniqueReferenceId("");


                        return model.initiatePayment(initiatePaymentRequest);
                    } else {
                        return Observable.just(new InitiatePaymentResponse());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> ProgressDialogFragment.dismissProgress(model.getAppCompatActivity().getSupportFragmentManager()))
                .subscribeWith(new CallbackWrapper<InitiatePaymentResponse>(view, this) {
                    @Override
                    protected void onSuccess(InitiatePaymentResponse data) {
                        if (data.getResponseData() != null) {
                            if (!data.isErrorStatus()) {
                                double amount = view.getTotalAmount();
                                orderId = data.getResponseData().getCustomReferenceId();
                                if (!currency.equalsIgnoreCase("INR")) {
                                    view.showPaymentDialog((dialogInterface, i) -> {
                                        double finalAmountInPaise = view.getFinalAmountINR() * 100;
                                        totalAmount = (int) finalAmountInPaise;
                                        model.startPayment(preferenceManger.getUserDetails().getUserProfile().getFullName(), preferenceManger.getUserDetails().getUserProfile().getEmail(), preferenceManger.getUserDetails().getUserProfile().getMobileNumber(), data.getResponseData().getCustomReferenceId(), totalAmount, currency);
                                    });
                                } else {
                                    double finalAmountInPaise = amount * 100;
                                    totalAmount = (int) finalAmountInPaise;
                                    //totalAmount = finalAmountInPaise;
                                    model.startPayment(preferenceManger.getUserDetails().getUserProfile().getFullName(), preferenceManger.getUserDetails().getUserProfile().getEmail(), preferenceManger.getUserDetails().getUserProfile().getMobileNumber(), data.getResponseData().getCustomReferenceId(), totalAmount, currency);
                                }

                                //model.openPaymentActivity(data.getTransactionId(), currency, amount);
                            } else {
                                ToastUtils.shortToast(data.getErrorMessage());
                            }
                        }
                    }
                });

    }

    private Disposable doProcessTransaction(String paymentMode) {
        ProgressDialogFragment.showProgress(model.getAppCompatActivity().getSupportFragmentManager());

        ProcessTransactionRequest request = new ProcessTransactionRequest();
        request.setPromoPurpose(AstroAppConstants.TRANSACT_TYPE);
        request.setAction(AstroAppConstants.ADD_TOPICS);
        request.setPromoId(view.getPromoId());
        request.setPromoCode(view.getPromoCode());
        request.setAmount(view.getApplyPromoCodeResponse() != null ? view.getApplyPromoCodeResponse().getAmount() : String.valueOf(view.getTotalAmount()));
        request.setTopicCount(view.getApplyPromoCodeResponse() != null ? view.getApplyPromoCodeResponse().getTopicsCount() : String.valueOf(view.getTopicCount()));
        request.setFinalAmount(String.valueOf(view.getTotalAmount()));
        request.setFinalTopicCount(String.valueOf(view.getTopicCount() + ((view.getApplyPromoCodeResponse() != null && !TextUtils.isEmpty(view.getApplyPromoCodeResponse().getPromoTopicsCount())) ? Integer.parseInt(view.getApplyPromoCodeResponse().getPromoTopicsCount()) : 0)));
        request.setUserId(preferenceManger.getUserDetails().getUserProfile().getUserId());
        request.setCurrency(currency);
        request.setPgName(paymentMode);

        return model.processTransaction(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> ProgressDialogFragment.dismissProgress(model.getAppCompatActivity().getSupportFragmentManager()))
                .subscribeWith(new CallbackWrapper<ProcessTransactionResponse>(view, this) {
                    @Override
                    protected void onSuccess(ProcessTransactionResponse data) {
                        if (data != null) {
                            if (!data.isErrorStatus() && data.isUserExist() && data.isTransactionCreated()) {
                                /* int topicCount = view.getTopicCount() + ((view.getApplyPromoCodeResponse() != null && !TextUtils.isEmpty(view.getApplyPromoCodeResponse().getPromoTopicsCount())) ? Integer.parseInt(view.getApplyPromoCodeResponse().getPromoTopicsCount()) : 0);*/
                                double amount = view.getTotalAmount();
                                orderId = data.getTransactionId();
                                //totalAmount = String.valueOf((int) amount * 100);
                                //  if (paymentMode.equalsIgnoreCase(AstroAppConstants.RAZOR_PAY))
                                // model.startPayment(preferenceManger.getUserDetails().getUserProfile().getEmail(), preferenceManger.getUserDetails().getUserProfile().getMobileNumber(), data.getTransactionId(), amount, currency);
                                // else
                                //   model.openPaymentActivity(data.getTransactionId(), currency, String.valueOf(amount));
                            } else {
                                ToastUtils.shortToast(data.getErrorMessage());
                            }
                        }
                    }
                });
    }

    public void onPaymentDone(int code, String pgPaymentId, String status) {
        if (status.equalsIgnoreCase("success")){
            disposable.add(onSuccessPaymentStatus(code, pgPaymentId, status));
        }else {
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
        CapturePaymentRequest capturePaymentRequest = new CapturePaymentRequest();
        capturePaymentRequest.setCustomReferenceId(orderId);
        capturePaymentRequest.setPaymentId(pgPaymentId);
        capturePaymentRequest.setTopics(view.getApplyPromoCodeResponse() != null ? view.getApplyPromoCodeResponse().getTopicsCount() : String.valueOf(view.getTopicCount()));
        capturePaymentRequest.setAmount(String.format("%.02f", view.getTotalAmount()));

        return model.capturePayment(capturePaymentRequest)
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
                                model.openResultActivity(false, Constants.SUCCESS, orderId, pgPaymentId, data.getErrorMessage());
                            }
                        }
                    }
                });

    }


    private Disposable observePromoCodeButtonClick() {
        return view.observePromoCodeButton()
                .map(isValidated -> view.isChatTopicEntered())
                .doOnNext(isValidated -> {
                    if (isValidated)
                        PromoCodeActivity.startPromoCodeActivity(model.getAppCompatActivity(), AstroAppConstants.TRANSACT_TYPE, view.getTopicCount(), view.getTotalAmount(), AddCreditsFragment.APPLY_PROMOCODE_REQUEST_CODE);
                    // else ToastUtils.shortToast("Please enter chat topics to apply promocode.");
                })
                .subscribe();
    }

    private Disposable observeViewPlansButtonClick() {
        return view.observeViewPlansutton()
                .subscribe(__ -> {
                    model.replaceUpgradePlanFragment();
                });
    }


    private Disposable observeRemovePromoCode() {
        return view.observeRemovePromoCode()
                .doOnNext(__ -> {
                    view.showDialogToRemovePromoCode("Are you sure you want to remove PromoCode?");
                })
                .subscribe();

    }


    private Disposable observeChatTopicsInput() {
        return view.observeChatTopicsEditText()
                .subscribe(text -> {
                    view.updatePaymentButton(text.toString().trim());
                });
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

    public void promoCodeApplied(ApplyPromoCodeResponse response, String promoCode, int promoId) {
        view.updatePromoCodeViews(response, promoCode, promoId);
        view.onPromoCodeApplied(true);
    }

    private void showChoosePaymentModeDialog() {

    }


}

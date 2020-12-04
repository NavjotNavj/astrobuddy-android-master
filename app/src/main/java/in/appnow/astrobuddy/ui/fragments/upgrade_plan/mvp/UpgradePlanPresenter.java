package in.appnow.astrobuddy.ui.fragments.upgrade_plan.mvp;

import android.text.TextUtils;
import android.view.View;

import com.razorpay.PaymentData;

import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.base.BasePresenter;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.dialog.UnAuthoriseUserDialog;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.payment.utility.Constants;
import in.appnow.astrobuddy.rest.CallbackWrapper;
import in.appnow.astrobuddy.rest.request.BaseRequestModel;
import in.appnow.astrobuddy.rest.request.CreateSubscriptionRequest;
import in.appnow.astrobuddy.rest.request.PaymentStatusRequest;
import in.appnow.astrobuddy.rest.response.BaseResponseModel;
import in.appnow.astrobuddy.rest.response.CreateSubscriptionResponse;
import in.appnow.astrobuddy.rest.response.PlanResponse;
import in.appnow.astrobuddy.rest.response.TopicRateResponse;
import in.appnow.astrobuddy.ui.fragments.upgrade_plan.mvp.view.UpgradePlanView;
import in.appnow.astrobuddy.rest.response.UserProfile;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by sonu on 18:42, 03/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class UpgradePlanPresenter implements BasePresenter {

    private static final String TAG = UpgradePlanPresenter.class.getSimpleName();
    private final UpgradePlanView view;
    private final UpgradePlanModel model;
    private final PreferenceManger preferenceManger;
    private final ABDatabase abDatabase;
    private String orderId;
    private Double amount;

    private final CompositeDisposable disposable = new CompositeDisposable();

    private String currency;

    public UpgradePlanPresenter(UpgradePlanView view, UpgradePlanModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        this.view = view;
        this.model = model;
        this.preferenceManger = preferenceManger;
        this.abDatabase = abDatabase;
    }

    @Override
    public void onCreate() {
        // disposable.add(fetchTopicRate());
        disposable.add(onRecyclerClickEvent());
        disposable.add(fetchSubscriptionPlans());
        disposable.add(onShowHideMore());
        disposable.add(observeMoreLessButtonClick());
        disposable.add(observeCancelCurrentPlanClick());
    }

    private Disposable fetchTopicRate() {
        ProgressDialogFragment.showProgress(model.getAppCompatActivity().getSupportFragmentManager());
        BaseRequestModel responseModel = new BaseRequestModel();
        responseModel.setUserId(preferenceManger.getUserDetails().getUserProfile().getUserId());
        return model.getTopicRate(responseModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> ProgressDialogFragment.dismissProgress(model.getAppCompatActivity().getSupportFragmentManager()))
                .subscribeWith(new CallbackWrapper<TopicRateResponse>(view, this) {
                    @Override
                    protected void onSuccess(TopicRateResponse data) {
                        if (data != null) {
                            if (data.isErrorStatus()) {
                                ToastUtils.shortToast(data.getErrorMessage());
                            } else {
                                currency = data.getCurrency();
                            }
                        }
                    }
                });
    }

    private Disposable observeMoreLessButtonClick() {
        return view.observeShowMoreLessButton()
                .subscribe(__ -> view.showHideCurrentPlanDesc());
    }

    private Disposable observeCancelCurrentPlanClick() {
        return view.observeCancelCurrentPlanButton()
                .subscribe(__ -> view.showCancelCurrentPlanDialog((dialogInterface, i) -> {
                    if (AstroApplication.getInstance().isInternetConnected(true)) {
                        disposable.add(cancelCurrentPlan());
                    }
                }));
    }

    private Disposable cancelCurrentPlan() {
        ProgressDialogFragment.showProgress(model.getAppCompatActivity().getSupportFragmentManager());
        BaseRequestModel requestModel = new BaseRequestModel();
        requestModel.setUserId(preferenceManger.getUserDetails().getUserProfile().getUserId());
        return model.cancelSubscription(requestModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> ProgressDialogFragment.dismissProgress(model.getAppCompatActivity().getSupportFragmentManager()))
                .subscribeWith(new CallbackWrapper<BaseResponseModel>(view, this) {
                    @Override
                    protected void onSuccess(BaseResponseModel data) {
                        if (data != null) {
                            ToastUtils.longToast(data.getErrorMessage());
                            if (!data.isErrorStatus()) {
                                disposable.add(fetchSubscriptionPlans());
                            }
                        }
                    }
                });
    }


    private Disposable onRecyclerClickEvent() {
        return view.adapter.getPlansPublishSubject().subscribe(object -> {
            if (AstroApplication.getInstance().isInternetConnected(true)) {
                //model.openResultActivity(object.getTopicCount());
                if (object.getPlanName().equalsIgnoreCase("Astro Basic")) {
                    disposable.add(changePlanToBasic());
                } else {

                    //check if current plan is not equal to Astro Basic that means user is already subscribed to some other plan.
                    //then show him dialog
                  if (view.getCurrentPlan()!=null && !view.getCurrentPlan().getPlanName().equalsIgnoreCase("Astro Basic")) {
                      view.showDialogForChangePlan((dialogInterface, i) -> disposable.add(observeMakePaymentButtonClick(object)));
                  }
                  else {
                      disposable.add(observeMakePaymentButtonClick(object));
                  }
                }
            }
        });
    }


    private Disposable onShowHideMore() {
        return view.adapter.getShowMoreLessPublishSubject().subscribe(view.adapter::showHide);
    }

    private Disposable observeMakePaymentButtonClick(PlanResponse.Plans plans) {
        ProgressDialogFragment.showProgress(model.getAppCompatActivity().getSupportFragmentManager());
        CreateSubscriptionRequest request = new CreateSubscriptionRequest();
        request.setUserId(preferenceManger.getUserDetails().getUserProfile().getUserId());
        request.setPromoId("1");
        request.setPlanId(plans.getPlanId());
        return model.createSubscription(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> ProgressDialogFragment.dismissProgress(model.getAppCompatActivity().getSupportFragmentManager()))
                .subscribeWith(new CallbackWrapper<CreateSubscriptionResponse>(view, this) {
                    @Override
                    protected void onSuccess(CreateSubscriptionResponse data) {
                        if (data != null) {
                            if (!data.isErrorStatus() && data.isSubscriptionCreate()) {
                                orderId = data.getSubscriptionTransId();
                                amount = plans.getAmount();
                                UserProfile userProfile = preferenceManger.getUserDetails().getUserProfile();
                                if (!plans.getCurrency().equalsIgnoreCase("INR")) {
                                    view.showPaymentDialog((dialogInterface, i) -> {
                                        model.startPayment(userProfile.getFullName(), userProfile.getEmail(), userProfile.getMobileNumber(), plans.getPlanDescription(), data.getSubscriptionId());
                                    }, plans.getAmountINR(), plans.getAmount());
                                } else {
                                    model.startPayment(userProfile.getFullName(), userProfile.getEmail(), userProfile.getMobileNumber(), plans.getPlanDescription(), data.getSubscriptionId());
                                }
                                //model.openPaymentActivity(data.getTransactionId(), view.getCurrency(), plans.getAmount());
                            } else {
                                ToastUtils.shortToast(data.getErrorMessage());
                            }
                        }
                    }
                });

    }


/*
    private Disposable observeMakePaymentButtonClick(SubscriptionPlanResponse.SubscriptionPlans plans) {
        ProgressDialogFragment.showDialog(model.getAppCompatActivity().getSupportFragmentManager());
        ProcessTransactionRequest request = new ProcessTransactionRequest();

        request.setPromoPurpose(AstroAppConstants.TRANSACT_TYPE);
        request.setAction(AstroAppConstants.CHG_PLAN);

        request.setPromoId(1);
        request.setPromoCode("");

        request.setSubPlanId(plans.getSubscriptionPlanId());

        request.setAmount(plans.getAmount());
        request.setTopicCount(String.valueOf(plans.getTopicCount()));
        request.setFinalAmount(plans.getAmount());
        request.setFinalTopicCount(String.valueOf(plans.getTopicCount()));

        request.setUserId(preferenceManger.getUserDetails().getUserProfile().getUserId());
        request.setCurrency(currency);

        return model.processTransaction(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> ProgressDialogFragment.dismissProgress(model.getAppCompatActivity().getSupportFragmentManager()))
                .subscribeWith(new CallbackWrapper<ProcessTransactionResponse>(view, this) {
                    @Override
                    protected void onSuccess(ProcessTransactionResponse data) {
                        if (data != null) {
                            if (!data.isErrorStatus() && data.isUserExist()) {
                                model.openPaymentActivity(data.getTransactionId(), view.getCurrency(), plans.getAmount());
                            } else {
                                ToastUtils.shortToast(data.getErrorMsg());
                            }
                        }
                    }
                });

    }
*/

    private Disposable changePlanToBasic() {
        ProgressDialogFragment.showProgress(model.getAppCompatActivity().getSupportFragmentManager());
        BaseRequestModel requestModel = new BaseRequestModel();
        requestModel.setUserId(preferenceManger.getUserDetails().getUserProfile().getUserId());
        return model.changePlanToBasic(requestModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> ProgressDialogFragment.dismissProgress(model.getAppCompatActivity().getSupportFragmentManager()))
                .subscribeWith(new CallbackWrapper<BaseResponseModel>(view, this) {
                    @Override
                    protected void onSuccess(BaseResponseModel data) {
                        if (data != null) {
                            if (!data.isErrorStatus()) {
                                model.onBackPress();
                            }
                            ToastUtils.shortToast(data.getErrorMessage());
                        } else {
                            ToastUtils.shortToast("Failed to change plan. Please try again.");
                        }
                    }
                });
    }

    /*  private Disposable fetchSubscriptionPlans() {
          view.showHideProgressBar(View.VISIBLE);
          BaseRequestModel baseRequestModel = new BaseRequestModel();
          baseRequestModel.setUserId(preferenceManger.getUserDetails().getUserProfile().getUserId());
          return model.getSubscriptionPlans(baseRequestModel)
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .doOnEach(__ -> view.showHideProgressBar(View.GONE))
                  .subscribeWith(new CallbackWrapper<SubscriptionPlanResponse>(view, this) {
                      @Override
                      protected void onSuccess(SubscriptionPlanResponse data) {
                          if (data != null) {
                              view.updateView(data);
                          }
                      }
                  });
      }
  */
    private Disposable fetchSubscriptionPlans() {
        view.showHideProgressBar(View.VISIBLE);
        BaseRequestModel baseRequestModel = new BaseRequestModel();
        baseRequestModel.setUserId(preferenceManger.getUserDetails().getUserProfile().getUserId());
        return model.getSubscriptionPlans(baseRequestModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> view.showHideProgressBar(View.GONE))
                .subscribeWith(new CallbackWrapper<PlanResponse>(view, this) {
                    @Override
                    protected void onSuccess(PlanResponse data) {
                        if (data != null) {
                            view.updateView(data);
                        }
                    }
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

    public void onPaymentDone(int code, String pgPaymentId, String status, PaymentData paymentData) {
        disposable.add(onSubmitPaymentStatus(code, pgPaymentId, status, paymentData));
    }

    private Disposable onSubmitPaymentStatus(int code, String pgPaymentId, String status, PaymentData paymentData) {
        ProgressDialogFragment.showProgress(model.getAppCompatActivity().getSupportFragmentManager());
        PaymentStatusRequest request = new PaymentStatusRequest();
        request.setUserId(preferenceManger.getUserDetails().getUserProfile().getUserId());
        request.setStatus(status);
        if (!TextUtils.isEmpty(orderId))
            request.setOrderId(orderId);
        request.setPgTransId(pgPaymentId);
        request.setAmount(String.valueOf(amount));
        request.setSignature(paymentData.getSignature());
        request.setCode(code);

        return model.submitPGStatus(request)
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
                                orderId = "";
                                amount = 0.0;
                            }
                        }
                    }
                });

    }


    public interface OnSubscriptionListener {
        public void onSubscriptionSuccess(String paymentId, PaymentData paymentData);

        public void onSubscriptionFailed(int code, String response, PaymentData paymentData);
    }
}

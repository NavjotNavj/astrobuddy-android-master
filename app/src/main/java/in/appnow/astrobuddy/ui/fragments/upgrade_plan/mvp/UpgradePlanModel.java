package in.appnow.astrobuddy.ui.fragments.upgrade_plan.mvp;

import androidx.appcompat.app.AppCompatActivity;

import com.razorpay.Checkout;

import org.json.JSONObject;

import in.appnow.astrobuddy.dialog.DialogHelperClass;
import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.fcm.Config;
import in.appnow.astrobuddy.payment.PaymentWebViewActivity;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.request.BaseRequestModel;
import in.appnow.astrobuddy.rest.request.CreateSubscriptionRequest;
import in.appnow.astrobuddy.rest.request.PaymentStatusRequest;
import in.appnow.astrobuddy.rest.request.ProcessTransactionRequest;
import in.appnow.astrobuddy.rest.response.BaseResponseModel;
import in.appnow.astrobuddy.rest.response.CreateSubscriptionResponse;
import in.appnow.astrobuddy.rest.response.PlanResponse;
import in.appnow.astrobuddy.rest.response.ProcessTransactionResponse;
import in.appnow.astrobuddy.rest.response.TopicRateResponse;
import in.appnow.astrobuddy.ui.activities.payment_result.PaymentResultActivity;
import in.appnow.astrobuddy.ui.fragments.upgrade_plan.UpgradePlanFragment;
import in.appnow.astrobuddy.utils.Logger;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;

/**
 * Created by sonu on 18:41, 03/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class UpgradePlanModel {
    private static final String TAG = UpgradePlanModel.class.getSimpleName();
    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;
    public static final int RAZOR_PAY_SUBSCRIPTION_REQUEST_CODE = 7;


    public UpgradePlanModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
        this.appCompatActivity = appCompatActivity;
        this.apiInterface = apiInterface;
    }

    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    public void showProgressBar() {
        ProgressDialogFragment.showProgress(appCompatActivity.getSupportFragmentManager());
    }

    public void hideProgressBar() {
        ProgressDialogFragment.dismissProgress(appCompatActivity.getSupportFragmentManager());

    }
   /* public Observable<SubscriptionPlanResponse> getSubscriptionPlans(BaseRequestModel baseRequestModel) {
        return apiInterface.getSubscriptionPlans(baseRequestModel);
    }*/

    public Observable<PlanResponse> getSubscriptionPlans(BaseRequestModel baseRequestModel) {
        return apiInterface.getSubscriptionPlans(baseRequestModel);
    }

    public Observable<CreateSubscriptionResponse> createSubscription(CreateSubscriptionRequest createSubscriptionRequest) {
        return apiInterface.createSubscription(createSubscriptionRequest);
    }

    public Observable<BaseResponseModel> cancelSubscription(BaseRequestModel requestModel) {
        return apiInterface.cancelSubscription(requestModel);
    }

    public Observable<BaseResponseModel> submitPGStatus(PaymentStatusRequest request) {
        return apiInterface.submitPGStatus(request);
    }

    public Observable<TopicRateResponse> getTopicRate(BaseRequestModel requestModel) {
        return apiInterface.getUserTopicRate(requestModel);
    }

    public Observable<BaseResponseModel> changePlanToBasic(BaseRequestModel requestModel) {
        return apiInterface.changePlanToBasic(requestModel);
    }

    public Observable<ProcessTransactionResponse> processTransaction(ProcessTransactionRequest transactionRequest) {
        return apiInterface.processTransaction(transactionRequest);
    }

    public void openPaymentActivity(String orderId, String currency, String amount) {
        PaymentWebViewActivity.startPaymentActivity(appCompatActivity, orderId, currency, amount, UpgradePlanFragment.PAYMENT_REQUEST_CODE);
    }

    public void onBackPress() {
        appCompatActivity.onBackPressed();
    }

    public void startPayment(String customerName, String emailId, String mobileNumber, String planDescription, String subscriptionId) {

        try {
            /**
             * Instantiate Checkout
             */
            Checkout checkout = new Checkout();

            /**
             * Set your logo here
             */
            //checkout.setImage(R.drawable.logo);


            /**
             * Pass your payment options to the Razorpay Checkout as a JSONObject
             */
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: Rentomojo || HasGeek etc.
             */
            options.put("name", "Astrobuddy");
            options.put("subscription_id", subscriptionId);


            //You can omit the image option to fetch the image from dashboard
            //options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");

            /**
             * Description can be anything
             * eg: Order #123123
             *     Invoice Payment
             *     etc.
             */
            options.put("description", planDescription);

            //options.put("currency", currency.toUpperCase());
            //  options.put("currency", "INR");

            /**
             * Amount is always passed in PAISE
             * Eg: "500" = Rs 5.00
             */
            // int amountToPay = Integer.parseInt(String.format("%.02f", (amount * 100)));
            //Logger.DebugLog(TAG, "Amount to pay : " + amountToPay);
//            options.put("amount", amount);

            JSONObject preFill = new JSONObject();
            preFill.put("name", customerName);
            preFill.put("email", emailId);
            preFill.put("contact", mobileNumber);

            options.put("prefill", preFill);

            JSONObject theme = new JSONObject();
            theme.put("color", "#f86c36");

            options.put("theme", theme);

            checkout.open(appCompatActivity, options);

        } catch (Exception e) {
            Logger.ErrorLog(TAG, "Error in starting Razorpay Checkout : " + e.getLocalizedMessage());
            ToastUtils.longToast("Error in starting Razorpay Checkout : " + e.getLocalizedMessage());
        }
    }

    public void showAlertOnTransactionFailed(String message) {
        DialogHelperClass.showMessageOKCancel(appCompatActivity, message, "OK", null, (dialogInterface, i) -> {

        }, null);
    }

    public void openResultActivity(boolean errorStatus, String orderStatus, String orderId, String paymentId, String statusMessage) {
        PaymentResultActivity.openPaymentResultActivity(appCompatActivity, errorStatus, orderStatus, statusMessage, orderId, paymentId, Config.SUBSCRIPTION, RAZOR_PAY_SUBSCRIPTION_REQUEST_CODE);
    }


}

package in.appnow.astrobuddy.ui.fragments.addcredits.mvp;

import android.content.DialogInterface;
import androidx.appcompat.app.AppCompatActivity;

import com.razorpay.Checkout;

import org.json.JSONObject;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.dialog.DialogHelperClass;
import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.fcm.Config;
import in.appnow.astrobuddy.payment.PaymentWebViewActivity;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.request.AbortPaymentRequest;
import in.appnow.astrobuddy.rest.request.AuthorizePaymentRequest;
import in.appnow.astrobuddy.rest.request.BaseRequestModel;
import in.appnow.astrobuddy.rest.request.CapturePaymentRequest;
import in.appnow.astrobuddy.rest.request.InitiatePaymentRequest;
import in.appnow.astrobuddy.rest.response.BaseResponseModel;
import in.appnow.astrobuddy.rest.request.PaymentStatusRequest;
import in.appnow.astrobuddy.rest.request.ProcessTransactionRequest;
import in.appnow.astrobuddy.rest.response.InitiatePaymentResponse;
import in.appnow.astrobuddy.rest.response.MyAccountResponse;
import in.appnow.astrobuddy.rest.response.ProcessTransactionResponse;
import in.appnow.astrobuddy.rest.response.TopicRateResponse;
import in.appnow.astrobuddy.rest.response.UserPaymentCheckResponse;
import in.appnow.astrobuddy.ui.activities.payment_result.PaymentResultActivity;
import in.appnow.astrobuddy.ui.fragments.addcredits.AddCreditsFragment;
import in.appnow.astrobuddy.ui.fragments.upgrade_plan.UpgradePlanFragment;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.LocaleUtils;
import in.appnow.astrobuddy.utils.Logger;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;

/**
 * Created by sonu on 10:59, 12/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class AddCreditsModel {
    private static final String TAG = AddCreditsModel.class.getSimpleName();
    public static final int RAZOR_PAY_REQUEST_CODE = 5;
    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;


    public AddCreditsModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
        this.appCompatActivity = appCompatActivity;
        this.apiInterface = apiInterface;
    }

    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    public Observable<ProcessTransactionResponse> processTransaction(ProcessTransactionRequest transactionRequest) {
        return apiInterface.processTransaction(transactionRequest);
    }

    public Observable<InitiatePaymentResponse> initiatePayment(InitiatePaymentRequest initiatePaymentRequest) {
        return apiInterface.initiatePayment(initiatePaymentRequest);
    }

    public Observable<UserPaymentCheckResponse> getTopicRate() {
        return apiInterface.checkUserPayment();
    }

    public Observable<TopicRateResponse> getTopicRate(BaseRequestModel requestModel) {
        return apiInterface.getUserTopicRate(requestModel);
    }

    public Observable<MyAccountResponse> getMyAccountDetails(BaseRequestModel baseRequestModel) {
        return apiInterface.getMyAccountDetails(baseRequestModel);
    }


    public void replaceUpgradePlanFragment() {
        FragmentUtils.onChangeFragment(appCompatActivity.getSupportFragmentManager(), R.id.container_view, UpgradePlanFragment.newInstance(), FragmentUtils.UPGRADE_PLAN_NEW_FRAGMENT);
    }
    public Observable<BaseResponseModel> abortPaymentStatus(AbortPaymentRequest request) {
        return apiInterface.abortPayment(request);
    }

    public Observable<BaseResponseModel> authorizePayment(AuthorizePaymentRequest request) {
        return apiInterface.authorizePayment(request);
    }

    public Observable<BaseResponseModel> capturePayment(CapturePaymentRequest request) {
        return apiInterface.capturePayment(request);
    }

    public void showChoosePaymentModeDialog(String[] maritalStatusArray, int checkedItem, DialogInterface.OnClickListener selectListener, DialogInterface.OnClickListener onClickListener) {
        DialogHelperClass.showSingleChoiceListDialog(appCompatActivity, "Select Payment Mode", maritalStatusArray, checkedItem, "Proceed", "Cancel", selectListener, onClickListener);
    }

    public String getCountry() {
        return LocaleUtils.fetchCountryISO(appCompatActivity);
    }

    public void openResultActivity(boolean errorStatus, String orderStatus, String orderId, String paymentId, String statusMessage) {
        PaymentResultActivity.openPaymentResultActivity(appCompatActivity, errorStatus, orderStatus, statusMessage, orderId, paymentId, Config.CREDITS, RAZOR_PAY_REQUEST_CODE);
    }

    public void openPaymentActivity(String orderId, String currency, String amount) {
        PaymentWebViewActivity.startPaymentActivity(appCompatActivity, orderId, currency, amount, AddCreditsFragment.CCAVENUE_PAYMENT_REQUEST_CODE);
    }

    public void showProgressBar() {
        ProgressDialogFragment.showProgress(appCompatActivity.getSupportFragmentManager());
    }

    public void hideProgressBar() {
        ProgressDialogFragment.dismissProgress(appCompatActivity.getSupportFragmentManager());

    }

    public void startPayment(String customerName,String emailId, String mobileNumber, String orderId, int amount, String currency) {

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


            //You can omit the image option to fetch the image from dashboard
            //options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");

            /**
             * Description can be anything
             * eg: Order #123123
             *     Invoice Payment
             *     etc.
             */
            options.put("description", "Order #" + orderId);

            //options.put("currency", currency.toUpperCase());
            options.put("currency", "INR");

            /**
             * Amount is always passed in PAISE
             * Eg: "500" = Rs 5.00
             */
           // int amountToPay = Integer.parseInt(String.format("%.02f", (amount * 100)));
            //Logger.DebugLog(TAG, "Amount to pay : " + amountToPay);
            options.put("amount", amount);

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
}

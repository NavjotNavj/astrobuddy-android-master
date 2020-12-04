package in.appnow.astrobuddy.ui.fragments.call_plans.mvp;

import androidx.appcompat.app.AppCompatActivity;

import com.razorpay.Checkout;

import org.json.JSONObject;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.dialog.DialogHelperClass;
import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.fcm.Config;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.request.AbortPaymentRequest;
import in.appnow.astrobuddy.rest.request.AuthorizePaymentRequest;
import in.appnow.astrobuddy.rest.request.BaseRequestModel;
import in.appnow.astrobuddy.rest.request.CaptureCallPaymentRequest;
import in.appnow.astrobuddy.rest.request.CapturePaymentRequest;
import in.appnow.astrobuddy.rest.request.InitiatePaymentRequest;
import in.appnow.astrobuddy.rest.response.BaseResponseModel;
import in.appnow.astrobuddy.rest.response.CallPlan;
import in.appnow.astrobuddy.rest.response.CallPlansResponse;
import in.appnow.astrobuddy.rest.response.InitiatePaymentResponse;
import in.appnow.astrobuddy.ui.activities.payment_result.PaymentResultActivity;
import in.appnow.astrobuddy.ui.fragments.call_plans.OnCallPlanFlowLaunch;
import in.appnow.astrobuddy.ui.fragments.call_plans.call_plan_history.CallPlansHistoryFragment;
import in.appnow.astrobuddy.ui.fragments.call_plans.dialog.BuyPlanDialogFragment;
import in.appnow.astrobuddy.ui.fragments.call_plans.dialog.KnowMoreDialogFragment;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.Logger;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;

/**
 * Created by NILESH BHARODIYA on 28-08-2019.
 */
public class CallPlansModel {
    private static final String TAG = "CallPlansModel";

    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;

    // Payment
    public static final int RAZOR_PAY_REQUEST_CODE = 6;

    public CallPlansModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
        this.appCompatActivity = appCompatActivity;
        this.apiInterface = apiInterface;
    }

    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    public Observable<CallPlansResponse> getCallPlanResponse(BaseRequestModel baseRequestModel) {
        return apiInterface.getCallPlans(baseRequestModel);
    }

    public void showKnowMoreDialog(CallPlan callPlan) {

        BuyPlanDialogFragment.dismissDialog(appCompatActivity.getSupportFragmentManager());

        KnowMoreDialogFragment.showDialog(appCompatActivity.getSupportFragmentManager(), callPlan);
    }

    public void showProgressBar() {
        ProgressDialogFragment.showProgress(appCompatActivity.getSupportFragmentManager());
    }

    public void hideProgressBar() {
        ProgressDialogFragment.dismissProgress(appCompatActivity.getSupportFragmentManager());

    }

    public void showBuyPlanDialog(CallPlan callPlan,
                                  OnCallPlanFlowLaunch onCallPlanFlowLaunch) {

        KnowMoreDialogFragment.dismissDialog(appCompatActivity.getSupportFragmentManager());

        BuyPlanDialogFragment.showDialog(appCompatActivity.getSupportFragmentManager(),
                callPlan,
                onCallPlanFlowLaunch);
    }

    public void replaceCallPlanHistory() {
        FragmentUtils.onChangeFragment(appCompatActivity.getSupportFragmentManager(),
                R.id.container_view,
                CallPlansHistoryFragment.newInstance(),
                FragmentUtils.CALL_PLAN_HISTORY_FRAGMENT);
    }

    // Payment

    public Observable<InitiatePaymentResponse> initiatePayment(InitiatePaymentRequest initiatePaymentRequest) {
        return apiInterface.initiatePayment(initiatePaymentRequest);
    }


    public void openResultActivity(boolean errorStatus, String orderStatus, String orderId, String paymentId, String statusMessage) {
        PaymentResultActivity.openPaymentResultActivity(appCompatActivity, errorStatus, orderStatus, statusMessage, orderId, paymentId, Config.CALL,RAZOR_PAY_REQUEST_CODE);
    }

    public Observable<BaseResponseModel> abortPaymentStatus(AbortPaymentRequest request) {
        return apiInterface.abortPayment(request);
    }

    public Observable<BaseResponseModel> authorizePayment(AuthorizePaymentRequest request) {
        return apiInterface.authorizePayment(request);
    }

    public Observable<BaseResponseModel> captureCallPayment(CaptureCallPaymentRequest request) {
        return apiInterface.captureCallPayment(request);
    }

    public void startPayment(String customerName, String emailId, String mobileNumber, String orderId, int amount, String currency) {

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

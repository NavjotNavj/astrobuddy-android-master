package in.appnow.astrobuddy.user_authentication_module.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;

import javax.inject.Inject;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.base.BaseActivity;
import in.appnow.astrobuddy.user_authentication_module.activity.dagger.dagger_otp.DaggerOTPComponent;
import in.appnow.astrobuddy.user_authentication_module.activity.dagger.dagger_otp.OTPModule;
import in.appnow.astrobuddy.user_authentication_module.activity.mvp.mvp_otp.OTPPresenter;
import in.appnow.astrobuddy.user_authentication_module.activity.mvp.mvp_otp.OTPVerificationActivityView;

/**
 * Created by Abhishek Thanvi on 09/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class OTPVerificationActivity extends BaseActivity {

    private static final String TAG = OTPVerificationActivity.class.getSimpleName();

    @Inject
    OTPVerificationActivityView otpVerificationActivityView;
    @Inject
    OTPPresenter otpPresenter;
   // private SMSReceiver smsReceiver;
    private String otpReceivedAction;
    private Context context;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerOTPComponent.builder().appComponent(AstroApplication.get(this).component()).oTPModule(new OTPModule(this))
                .build().inject(this);
        setContentView(otpVerificationActivityView);
        if (otpPresenter!=null) {
            otpPresenter.onCreate();
        }
        context = this;
        otpReceivedAction = this.getResources().getString(R.string.otp_received_action);
       // registerSMSReceiver();

    }


    /*private void registerSMSReceiver() {
        if (smsReceiver == null && MarshMallowPermission.checkPermission(context, Manifest.permission.RECEIVE_SMS) == 0) {
            smsReceiver = new SMSReceiver();
            context.registerReceiver(smsReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
        }
    }*/

 /*   private void unregisterSMSReceiver() {
        try {
            if (smsReceiver != null)
                context.unregisterReceiver(smsReceiver);
        } catch (Exception ignored) {
        }
    }*/

    @Override
    public void onResume() {
      //  LocalBroadcastManager.getInstance(context).registerReceiver(onOtpReceivedReceiver, new IntentFilter(otpReceivedAction));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
      //  LocalBroadcastManager.getInstance(context).unregisterReceiver(onOtpReceivedReceiver);
    }

   /* private BroadcastReceiver onOtpReceivedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null && intent.getAction().equalsIgnoreCase(otpReceivedAction)) {
                String otp = intent.getStringExtra("otp");
                otpVerificationActivityView.setOTPValue(otp);
                otpPresenter.otpModel.getRegisterRequestData().setOtp(otp);
                if (AstroApplication.getInstance().isInternetConnected(true)) {
                    otpPresenter.registerUser(otpPresenter.otpModel.getRegisterRequestData());
                }
            }
        }
    };*/


    @Override
    protected void onDestroy() {
        super.onDestroy();
       // unregisterSMSReceiver();
        otpPresenter.onDestroy();
    }

// Error inserting emp. Server Msg: Incorrect date value: '16-1-1900  05:50 PM'
}

package in.appnow.astrobuddy.user_authentication_module.activity.mvp.mvp_otp;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.Timer;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseView;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.helper.MyTimerTask;
import in.appnow.astrobuddy.interfaces.OnTimerChangeListener;
import in.appnow.astrobuddy.utils.ImageUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;

/**
 * Created by Abhishek Thanvi on 09/04/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


@SuppressLint("ViewConstructor")
public class OTPVerificationActivityView extends BaseViewClass implements BaseView {

    private static final String TAG = OTPVerificationActivityView.class.getSimpleName();
    @BindString(R.string.otp_sent_message)
    String otpMessageString;
    @BindString(R.string.receive_otp_label)
    String otpTimerLabel;
    @BindView(R.id.otp_message_label)
    AppCompatTextView otpMessageLabel;
    @BindView(R.id.didnt_receive_otp_label)
    AppCompatTextView didntReceivedOTPLabel;
    @BindView(R.id.otp_value_one)
    AppCompatEditText otp_value_one;
    @BindView(R.id.otp_value_two)
    AppCompatEditText otp_value_two;
    @BindView(R.id.otp_value_three)
    AppCompatEditText otp_value_three;
    @BindView(R.id.otp_value_four)
    AppCompatEditText otp_value_four;
    @BindView(R.id.confirm_otp_button)
    AppCompatButton confirm_otp_button;
    @BindView(R.id.resend_otp)
    AppCompatTextView resend_otp;
    @BindView(R.id.otp_logo)
    ImageView otpLogo;
    @BindString(R.string.unknown_error)
    String unknownErrorString;

    private AppCompatActivity appCompatActivity;

    @BindView(R.id.background_image)
    ImageView backgroundImageView;

    private Timer timer;

    public OTPVerificationActivityView(@NonNull AppCompatActivity appCompatActivity) {
        super(appCompatActivity);
        this.appCompatActivity = appCompatActivity;
        inflate(getContext(), R.layout.activity_verify_otp, this);
        ButterKnife.bind(this);
        ImageUtils.setDrawableImage(appCompatActivity, otpLogo, R.drawable.ic_logo_with_name);
        ImageUtils.setBackgroundImage(appCompatActivity, backgroundImageView);
    }


    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    public Observable<Object> confirmOtpButtonAction() {
        return RxView.clicks(confirm_otp_button);
    }

    public Observable<Object> resendOtpButtonAction() {
        return RxView.clicks(resend_otp);
    }

    public Observable<CharSequence> firstOTPTextWatcher() {
        return RxTextView.textChanges(otp_value_one);
    }

    public Observable<CharSequence> secondOTPTextWatcher() {
        return RxTextView.textChanges(otp_value_two);
    }

    public Observable<CharSequence> thirdOTPTextWatcher() {
        return RxTextView.textChanges(otp_value_three);
    }


    public Observable<CharSequence> fourthOTPTextWatcher() {
        return RxTextView.textChanges(otp_value_four);
    }

    public boolean validateSubmitOTP() {
        if (otp_value_one.getText().toString().trim().isEmpty()
                || otp_value_two.getText().toString().trim().isEmpty()
                || otp_value_three.getText().toString().trim().isEmpty()
                || otp_value_four.getText().toString().trim().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public void setOTPValue(String otp) {
        String[] otpValues = otp.split("(?!^)");
        otp_value_one.setText(otpValues[0]);
        otp_value_two.setText(otpValues[1]);
        otp_value_three.setText(otpValues[2]);
        otp_value_four.setText(otpValues[3]);
    }

    public void updateOtpMessageLabel(String mobileNumber) {
        otpMessageLabel.setText(String.format(otpMessageString, mobileNumber));
    }

    /**
     * start timer
     *
     * @param timeCounter number of seconds
     */
    public synchronized void startTimer(int timeCounter) {
        otp_value_one.setText("");
        otp_value_two.setText("");
        otp_value_three.setText("");
        otp_value_four.setText("");

        resend_otp.setVisibility(View.GONE);
        didntReceivedOTPLabel.setVisibility(View.VISIBLE);

        if (timeCounter < 1)
            return;
        if (timer == null)
            timer = new Timer();
        MyTimerTask skipTimerTask = new MyTimerTask(timeCounter, new OnTimerChangeListener() {
            @Override
            public void updateTimerProgress(int timeCounter) {
                appCompatActivity.runOnUiThread(() -> {
                    didntReceivedOTPLabel.setText(String.format(otpTimerLabel, timeCounter));
                    if (timeCounter == 0) {
                        cancelTimer();
                        didntReceivedOTPLabel.setVisibility(View.GONE);
                        resend_otp.setVisibility(View.VISIBLE);
                    }
                });

            }

            @Override
            public void stopTimer() {
                cancelTimer();
            }
        });
        timer.scheduleAtFixedRate(skipTimerTask, 0, 1000);
    }

    public void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void changeFocus(int position) {
        switch (position) {
            case 1:
                otp_value_two.requestFocus();
                break;
            case 2:
                otp_value_three.requestFocus();
                break;
            case 3:
                otp_value_four.requestFocus();
                break;
        }
    }

    @Override
    public void onUnknownError(String error) {
        ToastUtils.shortToast(error);
    }

    @Override
    public void onTimeout() {
        ToastUtils.shortToast(unknownErrorString);
    }

    @Override
    public void onNetworkError() {
        ToastUtils.shortToast(unknownErrorString);
    }

    @Override
    public boolean isNetworkConnected() {
        return false;
    }

    @Override
    public void onConnectionError() {
        ToastUtils.shortToast(unknownErrorString);
    }


}

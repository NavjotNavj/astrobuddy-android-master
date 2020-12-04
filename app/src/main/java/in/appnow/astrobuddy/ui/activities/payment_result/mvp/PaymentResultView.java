package in.appnow.astrobuddy.ui.activities.payment_result.mvp;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Html;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.fcm.Config;
import in.appnow.astrobuddy.payment.utility.Constants;
import in.appnow.astrobuddy.utils.ImageUtils;
import io.reactivex.Observable;

/**
 * Created by sonu on 17:28, 07/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class PaymentResultView extends BaseViewClass {

    @BindView(R.id.payment_result_done_button)
    Button doneButton;
    @BindView(R.id.payment_result_icon)
    ImageView resultIcon;
    @BindView(R.id.payment_result_label)
    TextView resultLabel;
    @BindView(R.id.payment_result_message_label)
    TextView messageLabel;
    @BindView(R.id.background_image)
    ImageView bgImage;
    @BindView(R.id.call_buddy_view)
    RelativeLayout callBuddyView;


    public PaymentResultView(@NonNull AppCompatActivity appCompatActivity) {
        super(appCompatActivity);
        inflate(appCompatActivity, R.layout.activity_payment_result, this);
        ButterKnife.bind(this);
        ImageUtils.setBackgroundImage(appCompatActivity, bgImage);
    }

    public Observable<Object> observeDoneButton() {
        return RxView.clicks(doneButton);
    }

    public void updateViews(String orderId, String trackingId, boolean errorStatus, String status, String statusMessage) {
        if (errorStatus) {
            updateButtonLabel("Retry");
            resultIcon.setImageResource(R.drawable.ic_cancel_black_24dp);
            ImageUtils.changeImageColor(resultIcon, getContext(), R.color.red_color);
            resultLabel.setText("Oops!!");
        } else {
            switch (status) {
                case Constants.SUCCESS:
                    resultIcon.setImageResource(R.drawable.ic_check_circle_black_24dp);
                    ImageUtils.changeImageColor(resultIcon, getContext(), R.color.green);
                    resultLabel.setText("Congratulations!!");
                    updateButtonLabel("Done");
                    break;
                default:
                    resultIcon.setImageResource(R.drawable.ic_cancel_black_24dp);
                    ImageUtils.changeImageColor(resultIcon, getContext(), R.color.red_color);
                    resultLabel.setText(status + "!!");
                    updateButtonLabel("Retry");
                    break;
            }
        }
        messageLabel.setText(Html.fromHtml(statusMessage));
    }

    public void showHideCallBuddy(String paymentType){
        if (Config.CALL.equals(paymentType)) {
            callBuddyView.setVisibility(VISIBLE);
        }


    }

    private void updateButtonLabel(String label){
        doneButton.setText(label);
    }
}

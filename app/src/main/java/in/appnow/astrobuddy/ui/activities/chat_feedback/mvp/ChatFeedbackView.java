package in.appnow.astrobuddy.ui.activities.chat_feedback.mvp;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.jakewharton.rxbinding2.view.RxView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseView;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.utils.ImageUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;

/**
 * Created by sonu on 17:28, 07/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class ChatFeedbackView extends BaseViewClass implements BaseView {

    @BindView(R.id.chat_feedback_message_label)
    TextView feedbackMessageLabel;
    @BindView(R.id.feedback_rating_bar)
    RatingBar feedbackRating;
    @BindView(R.id.enter_chat_feedback)
    EditText enterChatFeedback;
    @BindView(R.id.chat_feedback_submit_button)
    Button submitButton;
    @BindView(R.id.background_image)
    ImageView bgImage;
    @BindString(R.string.topic_added_message)
    String topicAddedMessage;
    @BindString(R.string.unknown_error)
    String unknownErrorString;

    public ChatFeedbackView(@NonNull AppCompatActivity appCompatActivity) {
        super(appCompatActivity);
        inflate(appCompatActivity, R.layout.activity_chat_feedback, this);
        ButterKnife.bind(this);
        ImageUtils.setBackgroundImage(appCompatActivity, bgImage);
    }

    public Observable<Object> observeSubmitButton() {
        return RxView.clicks(submitButton);
    }

    public void updateMessageLabel(String message) {
        feedbackMessageLabel.setText(message);
    }

    public String getChatFeedback() {
        return enterChatFeedback.getText().toString().trim();
    }

    public float getChatRating() {
        return feedbackRating.getRating();
    }

    public boolean isRatingSelected() {
        if (getChatRating() == 0) {
            ToastUtils.shortToast("Please select rating.");
            return false;
        }
        return true;
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

package in.appnow.astrobuddy.ui.fragments.addcredits.mvp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseView;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.dialog.DialogHelperClass;
import in.appnow.astrobuddy.rest.response.ApplyPromoCodeResponse;
import in.appnow.astrobuddy.rest.response.CheckUserPaymentInfo;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;

@SuppressLint("ViewConstructor")
public class AddCreditsView extends BaseViewClass implements BaseView {

    private static final String TAG = AddCreditsView.class.getSimpleName();

    @BindView(R.id.available_credits_label)
    TextView creditsLabel;
    @BindView(R.id.enter_chat_topics)
    EditText enterChatTopics;
    @BindView(R.id.have_a_promocode_button)
    TextView havePromoCodeButton;
    @BindView(R.id.suggested_credit_points_layout)
    LinearLayout suggestedCreditLayout;
    @BindView(R.id.credit_points_rate_label)
    TextView creditRateLabel;
    @BindView(R.id.make_payment_button)
    Button makePaymentButton;
    @BindView(R.id.applied_promo_code_label)
    TextView applied_code;
    @BindView(R.id.promo_code_applied_message_label)
    TextView promo_applied_message;
    @BindView(R.id.remove_promo_code)
    ImageView removePromoCode;
    @BindView(R.id.promo_code_applied_layout)
    LinearLayout promoCodeLayout;
    @BindView(R.id.add_credit_promo_code_label)
    ImageView promoTextLabel;
    @BindView(R.id.add_credit_note_label)
    TextView creditNoteLabel;
    @BindView(R.id.view_plans_button)
    TextView viewPlansButton;

    @BindString(R.string.available_chat_topics)
    String availableChatTopics;
    @BindString(R.string.credit_rate_chart_label)
    String creditRateChartString;
    @BindString(R.string.make_payment_format)
    String makePaymentString;
    @BindString(R.string.make_payment_format_others)
    String makePaymentOthersString;
    @BindString(R.string.make_payment)
    String makePayment;
    @BindArray(R.array.payment_mode_array)
    String[] paymentModeArray;
    @BindString(R.string.credit_note_string)
    String creditNoteString;
    @BindString(R.string.make_payment_dialog_message)
    String makePaymentDialogMessage;

    @BindString(R.string.unknown_error)
    String unknownErrorString;

    private String currency, priceIndex, price, inrConversionRate;

    private double totalPrice = 0;
    private double totalPriceINR = 0;

    private boolean isPromoApplied;
    private int promoId = 1;//1 for no promo applied
    private String promoCode = "";

    private ApplyPromoCodeResponse applyPromoCodeResponse;


    public AddCreditsView(@NonNull Context context) {
        super(context);
        inflate(getContext(), R.layout.add_credits_fragment, this);
        ButterKnife.bind(this, this);

        //disable copy/paste
        enterChatTopics.setLongClickable(false);

        viewPlansButton.setPaintFlags(viewPlansButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
       /* String promoText = FirebaseRemoteConfig.getInstance().getString("chat_promo_text");
        if (!TextUtils.isEmpty(promoText)) {
            promoTextLabel.setText(promoText);
            promoTextLabel.setBackground(context.getResources().getDrawable(R.drawable.ic_star_shape_sticker));
            promoTextLabel.setVisibility(View.VISIBLE);
        } else {
            promoTextLabel.setVisibility(View.GONE);
        }*/
        // isPromoAvailable();
        enterChatTopics.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
    }

    private void isPromoAvailable() {
        boolean isPromoAvailable = FirebaseRemoteConfig.getInstance().getBoolean("chat_promo_available");
        promoTextLabel.setVisibility(isPromoAvailable ? View.VISIBLE : View.GONE);
    }

    public Observable<Object> observePromoCodeButton() {
        return RxView.clicks(havePromoCodeButton);
    }

    public Observable<Object> observeViewPlansutton() {
        return RxView.clicks(viewPlansButton);
    }

    public Observable<Object> observeMakePaymentButton() {
        return RxView.clicks(makePaymentButton);
    }

    public String[] getPaymentModeArray() {
        return paymentModeArray;
    }

    public Observable<Object> observeRemovePromoCode() {
        return RxView.clicks(removePromoCode);
    }

    public Observable<CharSequence> observeChatTopicsEditText() {
        return RxTextView.textChanges(enterChatTopics);
    }

    public void onPromoCodeApplied(boolean isApplied) {
        this.isPromoApplied = isApplied;
        if (isApplied) {
            // promoTextLabel.setVisibility(View.GONE);
            havePromoCodeButton.setVisibility(GONE);
            promoCodeLayout.setVisibility(GONE);
        } else {
            // isPromoAvailable();
            havePromoCodeButton.setVisibility(GONE);
            promoCodeLayout.setVisibility(GONE);
        }
    }

    public void updatePromoCodeViews(ApplyPromoCodeResponse response, String promoCode, int promoId) {
        this.promoId = promoId;
        this.promoCode = promoCode;
        this.applyPromoCodeResponse = response;
        if (response == null) {
            promo_applied_message.setText("");
            applied_code.setText("");
        } else {
            promo_applied_message.setText(response.getPromoLabel());
            applied_code.setText(promoCode);

            if (!TextUtils.isEmpty(response.getPromoTopicsCount())) {
                //topics credit promo code
                totalPrice = Double.parseDouble(response.getAmount());
            } else {
                //amount discount promo
                if (!TextUtils.isEmpty(response.getPromoAmount())) {
                    totalPrice = Double.parseDouble(response.getAmount()) + Double.parseDouble(response.getPromoAmount());
                    calculatePriceForNonINRCurrency();
                }
            }
            changeButtonText();
        }

    }

    private void calculatePriceForNonINRCurrency() {
        if (!TextUtils.isEmpty(currency) && !currency.equalsIgnoreCase("INR") && !TextUtils.isEmpty(inrConversionRate) && !inrConversionRate.equalsIgnoreCase("0")) {
            totalPriceINR = totalPrice * Double.parseDouble(inrConversionRate);
        }
    }

    public void updateEditTextTopicText(String topic) {
        enterChatTopics.setText("");
    }

    public int getTopicCount() {
        try {
            if (!TextUtils.isEmpty(enterChatTopics.getText().toString().trim()))
                return Integer.parseInt(enterChatTopics.getText().toString().trim());
            else return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public void updateTopicsCount(int topicsCount) {
        creditsLabel.setText(String.format(availableChatTopics, topicsCount));
    }

    public void updateRateChart(CheckUserPaymentInfo rateResponse) {
        this.currency = rateResponse.getCurr();
        this.price = rateResponse.getRate();
        this.priceIndex = rateResponse.getTopicQuan();
        creditRateLabel.setText(String.format(creditRateChartString, currency, Float.parseFloat(price)));

        if (currency.equalsIgnoreCase("GBP")) {
            //this.inrConversionRate = rateResponse.getGbpToInr();
            // creditNoteLabel.setText(String.format(creditNoteString, currency, rateResponse.getGbpToInr()));
            // creditNoteLabel.setVisibility(View.VISIBLE);
        } else if (currency.equalsIgnoreCase("USD")) {
            //this.inrConversionRate = rateResponse.getUsdToInr();
            // creditNoteLabel.setText(String.format(creditNoteString, currency, rateResponse.getUsdToInr()));
            // creditNoteLabel.setVisibility(View.VISIBLE);
        } else {
            creditNoteLabel.setVisibility(View.GONE);
        }
    }

    public void updatePaymentButton(String input) {
        //if promo code is applied remove it
        if (isPromoApplied) {
            onPromoCodeApplied(false);
        }

        int enteredPoints = 0;
        if (!TextUtils.isEmpty(input) && input.length() > 0) {
            enteredPoints = Integer.parseInt(input);
        }
        try {
            totalPrice = (enteredPoints * Float.parseFloat(price)) / Float.parseFloat(priceIndex);
            calculatePriceForNonINRCurrency();
        } catch (Exception e) {
            e.printStackTrace();
        }

        changeButtonText();
    }

    private void changeButtonText() {
        if (totalPrice > 0) {
            if (currency.equalsIgnoreCase("GBP") && totalPriceINR > 0) {
                makePaymentButton.setText(String.format(makePaymentOthersString, currency, totalPrice, totalPriceINR));
            } else if (currency.equalsIgnoreCase("USD") && totalPriceINR > 0) {
                makePaymentButton.setText(String.format(makePaymentOthersString, currency, totalPrice, totalPriceINR));
            } else {
                makePaymentButton.setText(String.format(makePaymentString, currency, totalPrice));
            }
        } else
            makePaymentButton.setText(makePayment);
    }

    public double getTotalAmount() {
        return totalPrice;
    }

    public double getFinalAmountINR() {
        return totalPriceINR;
    }

    public double getAmountINR() {
        String amount = getApplyPromoCodeResponse() != null ? getApplyPromoCodeResponse().getAmount() : String.valueOf(getTotalAmount());
        if (!TextUtils.isEmpty(amount) && !amount.equalsIgnoreCase("0") && !TextUtils.isEmpty(inrConversionRate) && !inrConversionRate.equalsIgnoreCase("0")) {
            return Double.parseDouble(amount) * Double.parseDouble(inrConversionRate);
        }
        return 0;
    }

    public boolean isChatTopicEntered() {
        if (getTopicCount() > 0) {
            return true;
        } else {
            ToastUtils.shortToast("Please enter chat credits to buy.");
            return false;
        }
    }

    public int getPromoId() {
        return promoId;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void showDialogToRemovePromoCode(String message) {
        DialogHelperClass.showMessageOKCancel(getContext(), message, "OK", "CANCEL", (dialogInterface, i) -> {
            updatePromoCodeViews(null, "", 1);
            onPromoCodeApplied(false);
            updatePaymentButton(String.valueOf(getTopicCount()));
        }, (dialogInterface, i) -> {

        });
    }

    public ApplyPromoCodeResponse getApplyPromoCodeResponse() {
        return applyPromoCodeResponse;
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


    public void showPaymentDialog(DialogInterface.OnClickListener okClickListener) {
        DialogHelperClass.showMessageOKCancel(getContext(), String.format(makePaymentDialogMessage, getFinalAmountINR(), currency, getTotalAmount()), "OK", "CANCEL", okClickListener, null);
    }


}

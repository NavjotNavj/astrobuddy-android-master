package in.appnow.astrobuddy.ui.fragments.myaccount.mvp.view;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.app.AstroAppConstants;
import in.appnow.astrobuddy.dialog.DialogHelperClass;
import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.interfaces.APICallback;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.response.BaseResponseModel;
import in.appnow.astrobuddy.rest.response.TransactionHistoryResponse;
import in.appnow.astrobuddy.rest.request.TransactionReportRequest;
import in.appnow.astrobuddy.rest.service.AstroService;
import in.appnow.astrobuddy.utils.AppUtils;
import in.appnow.astrobuddy.utils.DateUtils;
import in.appnow.astrobuddy.utils.KeyboardUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sonu on 13:21, 16/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class TransactionHistoryViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.credit_history_row_description_label)
    TextView descriptionLabel;
    @BindView(R.id.credit_history_row_credit_label)
    TextView creditLabel;
    @BindView(R.id.credit_history_row_id_label)
    TextView transactionIdLabel;
    @BindView(R.id.credit_history_row_date_label)
    TextView dateLabel;
    @BindView(R.id.subscription_tag)
    TextView subscription_tag;
    @BindView(R.id.credit_action_more)
    ImageView actionMore;
    @BindView(R.id.topics_symbol)
    TextView creditSymbol;

    @BindColor(R.color.dark_green)
    int greenColor;
    @BindColor(R.color.red)
    int redColor;
    @BindColor(R.color.gunmetal)
    int defaultColor;
    @BindString(R.string.rs_format)
    String rsFormat;

    private Context context;
    private String userId;
    private APIInterface apiInterface;

    public TransactionHistoryViewHolder(Context context, View itemView, String userId, APIInterface apiInterface) {
        super(itemView);
        this.context = context;
        this.userId = userId;
        this.apiInterface = apiInterface;
        ButterKnife.bind(this, itemView);
    }

    public void bindData(TransactionHistoryResponse.TransactionHistory model) {
        descriptionLabel.setText(model.getComments() + " " + "[ID #" + model.getOrderId() + "]");

        if (model.getTransType().equalsIgnoreCase("db")) {
            creditLabel.setTextColor(redColor);
            creditLabel.setText("-" + model.getTopicsCount());
            if (model.getTransAction().equals(AstroAppConstants.DEDUCT_CHAT)) {
                actionMore.setVisibility(View.VISIBLE);
            } else {
                actionMore.setVisibility(View.INVISIBLE);
            }

        } else if (model.getTransType().equalsIgnoreCase("cr")) {
            creditLabel.setTextColor(greenColor);
            creditLabel.setText("+" + model.getTopicsCount());
            actionMore.setVisibility(View.INVISIBLE);
        } else {
            creditLabel.setTextColor(defaultColor);
            creditLabel.setText(String.valueOf(model.getTopicsCount()));
            actionMore.setVisibility(View.INVISIBLE);

        }

        if (model.getTopicsCount() == 0){
            creditLabel.setVisibility(View.GONE);
            creditSymbol.setVisibility(View.GONE);
        }else {
            creditLabel.setVisibility(View.VISIBLE);
            creditSymbol.setVisibility(View.VISIBLE);
        }

        dateLabel.setText(DateUtils.parseStringDate(model.getTransDate(), DateUtils.SERVER_DATE_FORMAT, DateUtils.TRANSACTION_FORMAT));

        if (model.getCurrency().equalsIgnoreCase("INR")) {
            if (model.getAmount() > 0) {
                transactionIdLabel.setText(String.format(rsFormat, model.getAmount()));
                transactionIdLabel.setVisibility(View.VISIBLE);
            } else {
                transactionIdLabel.setVisibility(View.INVISIBLE);

            }
        } else {
            if (model.getAmount() > 0) {
                transactionIdLabel.setText(AppUtils.getCurrencySymbol(model.getCurrency()) + " " + model.getAmount());
                transactionIdLabel.setVisibility(View.VISIBLE);
            } else {
                transactionIdLabel.setVisibility(View.INVISIBLE);
            }
        }

        if (model.getTransAction().equals(AstroAppConstants.CHG_PLAN) || model.getTransAction().equals(AstroAppConstants.CHG_PLAN_RNW)) {
            subscription_tag.setVisibility(View.VISIBLE);
        } else {
            subscription_tag.setVisibility(View.GONE);
        }


        actionMore.setOnClickListener(view -> showMenu(view, model));
    }

    private void showMenu(View v, TransactionHistoryResponse.TransactionHistory transactionHistory) {
        Context wrapper = new ContextThemeWrapper(context, R.style.DialogTheme);

        PopupMenu popup = new PopupMenu(wrapper, v);
        popup.getMenuInflater().inflate(R.menu.transaction_more_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_report:
                    showConfirmDialog(transactionHistory);
                    break;
            }
            return true;
        });
        popup.show();
    }

    private void showConfirmDialog(TransactionHistoryResponse.TransactionHistory transactionHistory) {
        DialogHelperClass.showMessageOKCancel(context, "Did you face any issue with this transaction?", "Yes", "No", (dialogInterface, i) -> {
            showReportDialog(transactionHistory);
        }, null);
    }

    private void showReportDialog(TransactionHistoryResponse.TransactionHistory transactionHistory) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogTheme);
        builder.setTitle("Report Transaction Issue");
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.message_dialog_layout, null, false);
        EditText inputMessage = view.findViewById(R.id.enter_message);
        builder.setView(view);
        builder.setPositiveButton("Submit", (dialogInterface, i) -> {
            String query = inputMessage.getText().toString().trim();
            if (!TextUtils.isEmpty(query)) {
                KeyboardUtils.hideSoftKeyboard((AppCompatActivity) context);
                TransactionReportRequest reportRequest = new TransactionReportRequest();
                reportRequest.setComments(query);
                reportRequest.setTransId(transactionHistory.getOrderId());
                reportRequest.setUserId(userId);
                submitReport(reportRequest);
            } else {
                ToastUtils.shortToast("Please enter comments.");
            }

        });
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            KeyboardUtils.hideSoftKeyboard((AppCompatActivity) context);
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void submitReport(TransactionReportRequest reportRequest) {

        if (apiInterface == null) {
            ToastUtils.shortToast("Oops!! Some unknown error occurred. Please try again.");
            return;
        }

        AstroService.submitTransactionReport(context, apiInterface, reportRequest, new APICallback() {
            @Override
            public void onResponse(Call<?> call, Response<?> response, int requestCode, @Nullable Object request) {
                if (response.isSuccessful()) {
                    BaseResponseModel responseModel = (BaseResponseModel) response.body();
                    if (responseModel != null) {
                        DialogHelperClass.showMessageOKCancel(context, responseModel.getErrorMessage(), "OK", "", (dialogInterface, i) -> {

                        }, null);
                    } else {
                        ToastUtils.shortToast("Failed to submit report. Please try again.");
                    }
                } else {
                    ToastUtils.shortToast("Failed to submit report. Please try again.");
                }
                ProgressDialogFragment.dismissProgress(((AppCompatActivity) context).getSupportFragmentManager());
            }

            @Override
            public void onFailure(Call<?> call, Throwable t, int requestCode, @Nullable Object request) {
                ToastUtils.shortToast("Failed to submit report. Please try again.");
            }

            @Override
            public void onNoNetwork(int requestCode) {

            }
        }, 1);

    }
}

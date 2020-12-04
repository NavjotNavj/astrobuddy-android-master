package in.appnow.astrobuddy.ui.fragments.upgrade_plan.mvp.view;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jakewharton.rxbinding2.view.RxView;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseView;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.dialog.DialogHelperClass;
import in.appnow.astrobuddy.rest.response.PlanResponse;
import in.appnow.astrobuddy.utils.DateUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;

/**
 * Created by sonu on 18:41, 03/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class UpgradePlanView extends BaseViewClass implements BaseView {
    @BindView(R.id.upgrade_plan_recycler_view)
    RecyclerView upgradePlanRecyclerView;
    @BindView(R.id.plan_details_button)
    TextView planDetailsButton;
    @BindView(R.id.upgrade_plan_progress_bar)
    ProgressBar upgradePlanProgressBar;
    @BindView(R.id.empty_upgrade_plan_label)
    TextView emptyUpgradePlanLabel;
    @BindView(R.id.upgrade_plan_scroll_view)
    NestedScrollView nestedScrollView;

    /* current plan views */
    @BindView(R.id.current_plan_card_view)
    CardView currentPlanCardView;
    @BindView(R.id.row_active_plan_label)
    TextView activePlanLabel;
    @BindView(R.id.row_plan_type_label)
    TextView planTypeLabel;
    @BindView(R.id.row_plan_desc_label)
    TextView topicDescLabel;
    @BindView(R.id.row_plan_topic_count_validity_label)
    TextView topicCountAndValidityLabel;
    @BindView(R.id.row_plan_buy_button)
    TextView buyNowButton;
    @BindView(R.id.show_more_less_button)
    TextView moreLessButton;
    @BindString(R.string.available_topics_format)
    String planChatValidityString;
    @BindString(R.string.more)
    String moreString;
    @BindString(R.string.less)
    String lessString;
    @BindColor(R.color.background_grey)
    int backgroundGrey;

    @BindString(R.string.unknown_error)
    String unknownErrorString;
    @BindString(R.string.make_payment_dialog_message)
    String makePaymentDialogMessage;

    private String currency = "";


    private PlanResponse.Plans currentPlan;

    public final UpgradePlanAdapter adapter = new UpgradePlanAdapter(getContext());

    public UpgradePlanView(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.upgrade_plan_fragment, this);
        ButterKnife.bind(this, this);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        upgradePlanRecyclerView.setLayoutManager(linearLayoutManager);
        upgradePlanRecyclerView.setHasFixedSize(true);
        upgradePlanRecyclerView.setNestedScrollingEnabled(false);
        upgradePlanRecyclerView.setAdapter(adapter);
    }

    private void updateCurrentPlanView() {
        if (currentPlan != null) {
            currentPlanCardView.setCardBackgroundColor(backgroundGrey);
            planTypeLabel.setText(currentPlan.getPlanName());
            topicDescLabel.setText(currentPlan.getPlanDescription());
            buyNowButton.setText("Cancel");
            if (!TextUtils.isEmpty(currentPlan.getCurrentPlanStartDate()) && !currentPlan.getCurrentPlanStartDate().equalsIgnoreCase("null")) {
                topicCountAndValidityLabel.setText("Plan Date : " + DateUtils.parseStringDate(currentPlan.getCurrentPlanStartDate(), DateUtils.SERVER_DATE_FORMAT, DateUtils.SIMPLE_DATE_FORMAT) + " | Validity : " + currentPlan.getValidity() + " Day(s)");
            } else {
                topicCountAndValidityLabel.setText("Validity : " + currentPlan.getValidity() + " Day(s)");
            }
            /* topicCountAndValidityLabel.setText(String.format(planChatValidityString, currentPlan.getTopicCount(), DateUtils.parseStringDate(currentPlan.getStartDate(), DateUtils.SERVER_DATE_FORMAT, DateUtils.TRANSACTION_FORMAT), DateUtils.parseStringDate(currentPlan.getExpiryDate(), DateUtils.SERVER_DATE_FORMAT, DateUtils.TRANSACTION_FORMAT)));*/
            if (currentPlan.getPlanName().equalsIgnoreCase("Astro Basic")) {
                buyNowButton.setVisibility(View.GONE);
            } else {
                buyNowButton.setVisibility(View.VISIBLE);
            }
            currentPlanCardView.setVisibility(View.VISIBLE);
            moreLessButton.setText(moreString);
        } else {
            currentPlanCardView.setVisibility(View.GONE);
        }
    }

    public void showHideCurrentPlanDesc() {
        if (moreLessButton.getText().toString().trim().equals(moreString)) {
            topicDescLabel.setMaxLines(Integer.MAX_VALUE);
            moreLessButton.setText(lessString);
        } else {
            topicDescLabel.setMaxLines(1);
            moreLessButton.setText(moreString);
        }
    }

    public Observable<Object> observePlanDetailsButton() {
        return RxView.clicks(planDetailsButton);
    }

    public Observable<Object> observeCancelCurrentPlanButton() {
        return RxView.clicks(buyNowButton);
    }

    public Observable<Object> observeShowMoreLessButton() {
        return RxView.clicks(moreLessButton);
    }

    public void showHideProgressBar(int visibility) {
        upgradePlanProgressBar.setVisibility(visibility);
        if (visibility == View.VISIBLE) {
            emptyUpgradePlanLabel.setVisibility(View.GONE);
            upgradePlanRecyclerView.setVisibility(View.GONE);
        }
    }

   /* public void updateView(SubscriptionPlanResponse response) {
        if (response.isErrorStatus()) {
            showHideView(true, response.getErrorMsg());
        } else {
            this.currentPlan = response.getCurrentPlan();
            if (response.getSubscriptionPlansList() != null && response.getSubscriptionPlansList().size() > 0) {
                updateCurrentPlanView();
                showHideView(false, "");
                this.currency = response.getCurrency();
                adapter.swapData(response.getSubscriptionPlansList(), currency);
            } else {
                showHideView(true, "No Plans.");
            }
        }
    }
*/

    public void updateView(PlanResponse response) {
        if (response.isErrorStatus()) {
            showHideView(true, response.getErrorMessage());
        } else {
            this.currentPlan = new PlanResponse().new Plans();

            this.currentPlan.setPlanId(response.getCurrentPlanId());
            this.currentPlan.setCurrentPlanStartDate(response.getCurrentPlanStartDate());
            this.currentPlan.setPlanName(response.getCurrentPlanName());
            this.currentPlan.setPlanDescription(response.getCurrentPlanDescription());
            this.currentPlan.setTopics(!TextUtils.isEmpty(response.getCurrentPlanTopics()) ? Integer.parseInt(response.getCurrentPlanTopics()) : 0);
            this.currentPlan.setValidity(response.getCurrentPlanValidity());

            updateCurrentPlanView();


            if (response.getNumberOfPlans() > 0 && response.getPlansList() != null && response.getPlansList().size() > 0) {
                showHideView(false, "");
                this.currency = response.getPlansList().get(0).getCurrency();
                adapter.swapData(response.getPlansList(), currency);
            } else {
                showHideView(true, response.getErrorMessage());
            }
        }
    }

    public String getCurrency() {
        return currency;
    }

    public void showHideView(boolean isEmpty, String message) {
        if (isEmpty) {
            emptyUpgradePlanLabel.setText(message);
            emptyUpgradePlanLabel.setVisibility(View.VISIBLE);
            upgradePlanRecyclerView.setVisibility(View.GONE);
        } else {
            emptyUpgradePlanLabel.setVisibility(View.GONE);
            upgradePlanRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    public PlanResponse.Plans getCurrentPlan() {
        return currentPlan;
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


    public void showPaymentDialog(DialogInterface.OnClickListener okClickListener, Double amountINR, Double amount) {
        DialogHelperClass.showMessageOKCancel(getContext(), String.format(makePaymentDialogMessage, amountINR, currency, amount), "OK", "CANCEL", okClickListener, null);
    }


    public void showCancelCurrentPlanDialog(DialogInterface.OnClickListener okClickListener) {
        DialogHelperClass.showMessageOKCancel(getContext(), "Are you sure you want to cancel the current subscription?", "OK", "CANCEL", okClickListener, null);
    }

    public void showDialogForChangePlan(DialogInterface.OnClickListener okClickListener) {
        DialogHelperClass.showMessageOKCancel(getContext(), "We will be cancelling your existing AstroBuddy Plan and adding you to new plan. Please confirm?", "YES", "NO", okClickListener, null);
    }
}



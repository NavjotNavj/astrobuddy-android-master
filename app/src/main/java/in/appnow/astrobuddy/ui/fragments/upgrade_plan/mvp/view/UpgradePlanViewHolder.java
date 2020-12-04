package in.appnow.astrobuddy.ui.fragments.upgrade_plan.mvp.view;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.rest.response.PlanResponse;
import in.appnow.astrobuddy.rest.response.SubscriptionPlanResponse;
import in.appnow.astrobuddy.utils.AppUtils;
import in.appnow.astrobuddy.utils.LocaleUtils;

/**
 * Created by sonu on 18:57, 03/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class UpgradePlanViewHolder extends RecyclerView.ViewHolder {

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
    @BindString(R.string.plan_chats_validity)
    String planChatValidityString;
    @BindString(R.string.more)
    String moreString;
    @BindString(R.string.less)
    String lessString;

    public UpgradePlanViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void onBindData(PlanResponse.Plans subscriptionPlans, int selectedPosition) {
        planTypeLabel.setText(subscriptionPlans.getPlanName());
        topicDescLabel.setText(subscriptionPlans.getPlanDescription());
        topicCountAndValidityLabel.setText("Chat Topics : " + subscriptionPlans.getTopics() + " | Validity : " + subscriptionPlans.getValidity() + " Day(s)" + " Amount : " + AppUtils.getCurrencySymbol(subscriptionPlans.getCurrency()) + " " + subscriptionPlans.getAmount());
        // topicCountAndValidityLabel.setText(String.format(planChatValidityString, subscriptionPlans.getTopicCount(), subscriptionPlans.getDays(), AppUtils.getCurrencySymbol(currency), subscriptionPlans.getAmount()));
        buyNowButton.setVisibility(View.VISIBLE);

        if (selectedPosition == getAdapterPosition()) {
            if (moreLessButton.getText().toString().trim().equals(moreString)) {
                topicDescLabel.setMaxLines(Integer.MAX_VALUE);
                moreLessButton.setText(lessString);
            } else {
                topicDescLabel.setMaxLines(1);
                moreLessButton.setText(moreString);
            }
        } else {
            topicDescLabel.setMaxLines(1);
            moreLessButton.setText(moreString);
        }

        if (subscriptionPlans.getPlanName().equalsIgnoreCase("Astro Basic")) {
            buyNowButton.setText("Get Now");
        } else {
            buyNowButton.setText("Buy Now");

        }
    }
}

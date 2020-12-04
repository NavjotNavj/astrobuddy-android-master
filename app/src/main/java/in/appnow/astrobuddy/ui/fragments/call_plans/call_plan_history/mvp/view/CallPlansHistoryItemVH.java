package in.appnow.astrobuddy.ui.fragments.call_plans.call_plan_history.mvp.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.custom_views.RatedView;
import in.appnow.astrobuddy.rest.response.CallConversationHistory;
import in.appnow.astrobuddy.utils.ImageUtils;


/**
 * Created by NILESH BHARODIYA on 03-09-2019.
 */
public class CallPlansHistoryItemVH extends RecyclerView.ViewHolder {

    @BindView(R.id.plans_minutes)
    public TextView planMinutes;

    @BindView(R.id.plans_price)
    public TextView planPrice;

    @BindView(R.id.status_badge)
    ImageView statusBadgeDone;

    @BindView(R.id.user_name)
    public TextView userName;

    @BindView(R.id.buy_date)
    public TextView buyDate;

    @BindView(R.id.used_date)
    public TextView usedDate;

    @BindView(R.id.rate_label)
    public TextView rateLabel;

    @BindView(R.id.rated_view)
    RatedView ratedView;

    @BindView(R.id.user_image)
    public CircleImageView userImage;

    @BindView(R.id.txn_id)
    public TextView transactionIdLabel;

    public CallPlansHistoryItemVH(@NonNull View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void bindData(CallConversationHistory conversationHistory) {

        planMinutes.setText(planMinutes.getContext().getResources().getString(R.string.plan_minutes, conversationHistory.getCallMinutes()));
        planPrice.setText(planPrice.getContext().getResources().getString(R.string.plan_price, "Rs", conversationHistory.getAmount()));
        userName.setText(conversationHistory.getName());

        ImageUtils.loadImageUrl(userImage.getContext(), userImage, R.drawable.icon_default_profile, conversationHistory.getImgFile());

        if (conversationHistory.getStatus().equalsIgnoreCase("WAIT")) {
            statusBadgeDone.setImageDrawable(statusBadgeDone.getContext().getResources().getDrawable(R.drawable.ic_pending_black_24dp));

        }else if (conversationHistory.getStatus().equalsIgnoreCase("CLOSE")){
            if (conversationHistory.getFdbkRating() != null && !conversationHistory.getFdbkRating().equalsIgnoreCase("")){
                ratedView.setVisibility(View.VISIBLE);
                ratedView.setRatedStar(Integer.parseInt(conversationHistory.getFdbkRating()));
            } else {
                rateLabel.setVisibility(View.VISIBLE);
            }
            statusBadgeDone.setImageDrawable(statusBadgeDone.getContext().getResources().getDrawable(R.drawable.ic_check_circle_black_24dp));
        }

        if (conversationHistory.getUsedDate() != null && !conversationHistory.getUsedDate().equalsIgnoreCase("")) {
            usedDate.setText(usedDate.getContext().getString(R.string.used_date, conversationHistory.getUsedDate()));
        } else
            usedDate.setText(usedDate.getContext().getString(R.string.used_date, " - "));

        if (conversationHistory.getStartDate() != null && !conversationHistory.getStartDate().equalsIgnoreCase("")) {
            buyDate.setText(buyDate.getContext().getString(R.string.buy_date, conversationHistory.getStartDate()));
        } else
            buyDate.setText(buyDate.getContext().getString(R.string.buy_date, " - "));

        if (conversationHistory.getTransactionId() == null || conversationHistory.getTransactionId().equalsIgnoreCase("")){
            transactionIdLabel.setText("Txn Id : - ");
        }else {
            transactionIdLabel.setText("Txn Id : #" + conversationHistory.getTransactionId());
        }

    }

}

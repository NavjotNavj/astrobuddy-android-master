package in.appnow.astrobuddy.ui.fragments.call_plans.mvp.view;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.custom_views.RatedView;
import in.appnow.astrobuddy.rest.response.CallPlan;
import in.appnow.astrobuddy.utils.ImageUtils;

public class CallPlansItemVH extends RecyclerView.ViewHolder {

    @BindView(R.id.user_image)
    public ImageView userImage;

    @BindView(R.id.user_status)
    public Button userStatus;

    @BindView(R.id.rated_view)
    public RatedView ratingLayout;

    @BindView(R.id.user_name)
    public TextView userName;

    @BindView(R.id.text_experience)
    public TextView textExperience;

    @BindView(R.id.text_language)
    public TextView textLanguage;

    @BindView(R.id.text_duration)
    public TextView textDuration;

    @BindView(R.id.text_fee)
    public TextView textFee;

    @BindView(R.id.text_fee_coin)
    public TextView textFeeCoin;

    @BindView(R.id.button_know_more)
    public Button buttonKnowMore;

    @BindView(R.id.button_buy)
    public Button buttonBuy;

    public CallPlansItemVH(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindData(CallPlan callPlan) {

        ImageUtils.loadImageUrl(userImage.getContext(),
                userImage,
                R.drawable.icon_default_profile,
                callPlan.getImageFile());

        userStatus.setEnabled(callPlan.getAvailable() == 1);
//        buttonBuy.setEnabled(callPlan.getAvailable() == 1);

        if (callPlan.getAvailable() == 1) {
            buttonBuy.setBackgroundResource(R.drawable.theme_button_selector);

        } else {
//            buttonBuy.setBackgroundResource(R.drawable.theme_button_selector_disable);
            buttonBuy.setBackgroundResource(R.drawable.theme_button_selector);
        }

        userName.setText(callPlan.getName());

        textExperience.setText(textExperience.getContext().getResources().getString(
                R.string.text_experience, callPlan.getExperience()));

        textLanguage.setText(textLanguage.getContext().getResources().getString(
                R.string.text_language, callPlan.getLanguage())
                .replace(", ", "\n"));

//        textDuration.setText(textDuration.getContext().getResources().getString(
//                R.string.text_duration, "10 minutes"));

//        textFeeCoin.setText(textFeeCoin.getContext().getResources().getString(
//                R.string.text_fee_coin, callPlan.getPerMinPrice()));

        textFee.setText(textFee.getContext().getResources().getString(
                R.string.text_fee, callPlan.getPerMinPrice()));
        ratingLayout.setRatedStar((int) callPlan.getRating());
    }

}
package in.appnow.astrobuddy.ui.fragments.call_plans.mvp.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseView;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.rest.response.CallPlan;
import in.appnow.astrobuddy.rest.response.CallPlansResponse;
import io.reactivex.Observable;
import it.sephiroth.android.library.tooltip.Tooltip;


/**
 * Created by NILESH BHARODIYA on 28-08-2019.
 */
public class CallPlansView extends BaseViewClass implements BaseView {

    @BindView(R.id.call_plan_recycler)
    RecyclerView callPlanRecyclerView;

    @BindView(R.id.header_image)
    ImageView headerImage;

    @BindView(R.id.empty_call_plan_label)
    TextView emptyCallPlanLabel;

    @BindString(R.string.unknown_error)
    String unknownErrorString;

    private final Context context;

    private Tooltip.TooltipView tooltip;

    private CallPlanAdapter callPlanAdapter = new CallPlanAdapter();

    public CallPlansView(@NonNull Context context) {
        super(context);
        this.context = context;
        inflate(context, R.layout.fragment_call_plan, this);
        ButterKnife.bind(this, this);

        setUpRecyclerView();

        showHideView(true, "");
    }

    private void setUpRecyclerView() {
        callPlanRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        callPlanRecyclerView.setHasFixedSize(true);
        callPlanRecyclerView.setNestedScrollingEnabled(false);
        callPlanRecyclerView.setAdapter(callPlanAdapter);
    }

    public void updateView(CallPlansResponse callPlansResponse) {
        if (callPlansResponse.isErrorStatus()) {
            showHideView(true, callPlansResponse.getErrorMessage());

        } else {
            if (callPlansResponse.getCallPlan() != null && callPlansResponse.getCallPlan().size() > 0) {
                showHideView(false, "");
                callPlanAdapter.updateData(callPlansResponse.getCallPlan());
            }
        }
    }

    public Observable<CallPlan> onKnowMoreClick() {
        return callPlanAdapter.getItemViewClickKnowMore();
    }

    public Observable<CallPlan> onBuyButtonClick() {
        return callPlanAdapter.getItemViewClickBuy();
    }

    public void showHideView(boolean isEmpty, String message) {
        if (isEmpty) {
            emptyCallPlanLabel.setText(message);
            emptyCallPlanLabel.setVisibility(View.VISIBLE);
            callPlanRecyclerView.setVisibility(View.GONE);
            headerImage.setVisibility(View.GONE);
        } else {
            emptyCallPlanLabel.setVisibility(View.GONE);
            callPlanRecyclerView.setVisibility(View.VISIBLE);
            headerImage.setVisibility(View.VISIBLE);
        }
    }
//    private void showCallHistoryToolTip() {
//        tooltip = Tooltip.make(context,
//                new Tooltip.Builder(101)
//                        .anchor(userHomeTopicsLabel, Tooltip.Gravity.BOTTOM)
//                        .closePolicy(new Tooltip.ClosePolicy()
//                                .insidePolicy(true, false)
//                                .outsidePolicy(false, false), 5000)
//                        .activateDelay(800)
//                        .withStyleId(R.style.ToolTipLayoutCustomLightStyle)
//                        .showDelay(300)
//                        .text("Call History")
//                        .maxWidth(500)
//                        .withArrow(true)
//                        .withOverlay(true).build());
//        tooltip.show();
//
//        //.typeface(mYourCustomFont)
//        //.floatingAnimation(Tooltip.AnimationBuilder.DEFAULT)
//
//    }

    @Override
    public void onUnknownError(String error) {
        showHideView(true, error);
    }

    @Override
    public void onTimeout() {
        showHideView(true, unknownErrorString);
    }

    @Override
    public void onNetworkError() {
        showHideView(true, unknownErrorString);
    }

    @Override
    public boolean isNetworkConnected() {
        return false;
    }

    @Override
    public void onConnectionError() {
        showHideView(true, unknownErrorString);
    }

}

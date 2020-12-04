package in.appnow.astrobuddy.ui.fragments.call_plans.call_plan_history.mvp.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseView;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.rest.response.CallConversationHistory;
import in.appnow.astrobuddy.rest.response.CallHistoryResponse;
import io.reactivex.Observable;

/**
 * Created by NILESH BHARODIYA on 03-09-2019.
 */
public class CallPlansHistoryView extends BaseViewClass implements BaseView {

    @BindView(R.id.call_plan_history_recycler)
    RecyclerView callPlanHistoryRecyclerView;

    @BindView(R.id.empty_call_plan_history_label)
    TextView emptyCallPlanHistoryLabel;

    @BindString(R.string.unknown_error)
    String unknownErrorString;

    private CallPlansHistoryAdapter callPlansHistoryAdapter = new CallPlansHistoryAdapter();

    public CallPlansHistoryView(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.fragment_call_plan_history, this);
        ButterKnife.bind(this, this);

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        callPlanHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        callPlanHistoryRecyclerView.setHasFixedSize(true);
        callPlanHistoryRecyclerView.setNestedScrollingEnabled(false);
        callPlanHistoryRecyclerView.setAdapter(callPlansHistoryAdapter);
    }

    public void updateView(CallHistoryResponse callHistoryResponse) {
        if (callHistoryResponse.isErrorStatus()) {
            showHideView(true, callHistoryResponse.getErrorMessage());

        } else {
            if (callHistoryResponse.getCallCount() > 0 && callHistoryResponse.getCallConversationHistory() != null && callHistoryResponse.getCallConversationHistory().size() > 0) {
                showHideView(false, "");
                callPlansHistoryAdapter.updateData(callHistoryResponse.getCallConversationHistory());
            }
        }
    }

    public void showHideView(boolean isEmpty, String message) {
        if (isEmpty) {
            emptyCallPlanHistoryLabel.setText(message);
            emptyCallPlanHistoryLabel.setVisibility(View.VISIBLE);
            callPlanHistoryRecyclerView.setVisibility(View.GONE);
        } else {
            emptyCallPlanHistoryLabel.setVisibility(View.GONE);
            callPlanHistoryRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    public Observable<CallConversationHistory> onRateNowButtonClick() {
        return callPlansHistoryAdapter.getItemViewClickRateNow();
    }

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

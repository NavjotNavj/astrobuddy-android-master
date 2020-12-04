package in.appnow.astrobuddy.ui.fragments.myaccount.mvp.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jakewharton.rxbinding2.view.RxView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseView;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.response.MyAccountResponse;
import in.appnow.astrobuddy.rest.response.TransactionHistoryResponse;
import in.appnow.astrobuddy.utils.ImageUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import in.appnow.astrobuddy.utils.VectorUtils;
import io.reactivex.Observable;

@SuppressLint("ViewConstructor")
public class MyAccountView extends BaseViewClass implements BaseView {

    private static final String TAG = MyAccountView.class.getSimpleName();

    @BindView(R.id.total_chat_topics_label)
    TextView chatTopicsLabel;
    @BindView(R.id.add_chat_topics_button)
    TextView addChatTopics;
    @BindView(R.id.current_plan_label)
    TextView currentPlanLabel;
    @BindView(R.id.upgrade_plan_button)
    TextView upgradePlanButton;
    @BindView(R.id.transaction_history_recycler_view)
    RecyclerView transactionHistoryRecyclerView;
    @BindView(R.id.transaction_history_progress_bar)
    ProgressBar transactionProgressBar;
    @BindView(R.id.empty_transaction_label)
    TextView emptyTransactionLabel;
    @BindView(R.id.background_image)
    ImageView backgroundImageView;
    @BindString(R.string.waiting_dash)
    String dashString;
    @BindString(R.string.no_transaction_history)
    String noTransactionHistoryString;
    private int topicsCount = 0;

    @BindString(R.string.unknown_error)
    String unknownErrorString;

    private final TransactionHistoryAdapter adapter = new TransactionHistoryAdapter();

    public MyAccountView(@NonNull Context context) {
        super(context);
        inflate(getContext(), R.layout.fragment_my_account, this);
        ButterKnife.bind(this, this);
        ImageUtils.setBackgroundImage(context, backgroundImageView);
        VectorUtils.setVectorCompoundDrawable(addChatTopics, context, R.drawable.ic_add_box_black_24dp, 0, 0, 0, R.color.gunmetal);
        VectorUtils.setVectorCompoundDrawable(upgradePlanButton, context, R.drawable.ic_add_box_black_24dp, 0, 0, 0, R.color.gunmetal);
        initRecyclerView(context);
    }

    private void initRecyclerView(@NonNull Context context) {
        transactionHistoryRecyclerView.setHasFixedSize(true);
        transactionHistoryRecyclerView.setNestedScrollingEnabled(false);
        transactionHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        transactionHistoryRecyclerView.setAdapter(adapter);
    }

    public Observable<Object> observeAddChatTopicsButton() {
        return RxView.clicks(addChatTopics);
    }

    public Observable<Object> observeUpgradePlanButton() {
        return RxView.clicks(upgradePlanButton);
    }

    public void updateMyAccount(MyAccountResponse data) {
        if (!data.isErrorStatus() && data.getAccountDetails() != null) {
            chatTopicsLabel.setText(String.valueOf(data.getAccountDetails().getUserTopics()));
            this.topicsCount = data.getAccountDetails().getUserTopics();
            //Current plan exist
//            if (data.getPlanHistory() != null) {
//                currentPlanLabel.setText(data.getPlanHistory().getPlanName());
//            }
            //No Plan Exist
           // else {
                currentPlanLabel.setText(dashString);
           // }
        } else {
            this.topicsCount = 0;
            ToastUtils.shortToast(data.getErrorMessage());
            chatTopicsLabel.setText(dashString);
            currentPlanLabel.setText(dashString);

        }
    }

    public int getTopicsCount() {
        return topicsCount;
    }


    public void showHideProgressBar(int visibility) {
        transactionProgressBar.setVisibility(visibility);
        if (visibility == VISIBLE) {
            emptyTransactionLabel.setVisibility(View.GONE);
            transactionHistoryRecyclerView.setVisibility(View.GONE);
        }
    }

    public void setTransactionHistory(TransactionHistoryResponse response, String userId, APIInterface apiInterface) {
        adapter.setUserId(userId);
        adapter.setApiInterface(apiInterface);
        if (response.isErrorStatus()) {
            showHideView(true, response.getErrorMessage());
        } else if (response.getTransactionHistoryList() != null && response.getTransactionHistoryList().size() > 0) {
            showHideView(false, "");
            adapter.swapData(response.getTransactionHistoryList());
        } else {
            showHideView(true, noTransactionHistoryString);
        }
    }

    public void showHideView(boolean isEmpty, String message) {
        if (isEmpty) {
            emptyTransactionLabel.setText(message);
            emptyTransactionLabel.setVisibility(View.VISIBLE);
            transactionHistoryRecyclerView.setVisibility(View.GONE);
        } else {
            emptyTransactionLabel.setVisibility(View.GONE);
            transactionHistoryRecyclerView.setVisibility(View.VISIBLE);
        }
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

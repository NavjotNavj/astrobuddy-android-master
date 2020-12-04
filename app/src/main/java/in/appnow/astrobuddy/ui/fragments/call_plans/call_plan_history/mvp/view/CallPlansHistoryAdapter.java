package in.appnow.astrobuddy.ui.fragments.call_plans.call_plan_history.mvp.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.rest.response.CallConversationHistory;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by NILESH BHARODIYA on 03-09-2019.
 */
public class CallPlansHistoryAdapter extends RecyclerView.Adapter<CallPlansHistoryItemVH> {

    private final List<CallConversationHistory> callConversationHistories = new ArrayList<>(0);
    private PublishSubject<CallConversationHistory> itemViewClickRateNow = PublishSubject.create();

    @NonNull
    @Override
    public CallPlansHistoryItemVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.call_plan_history_item, parent, false);

        return new CallPlansHistoryItemVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallPlansHistoryItemVH holder, int position) {
        CallConversationHistory callConversationHistory = callConversationHistories.get(position);

        RxView.clicks(holder.rateLabel)
                .map(o -> callConversationHistories.get(position))
                .subscribe(itemViewClickRateNow);

        holder.bindData(callConversationHistory);
    }

    public Observable<CallConversationHistory> getItemViewClickRateNow() {
        return itemViewClickRateNow;
    }

    public void updateData(List<CallConversationHistory> conversationHistories) {
        this.callConversationHistories.clear();

        this.callConversationHistories.addAll(conversationHistories);

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return callConversationHistories.size();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        itemViewClickRateNow.onComplete();
    }
}

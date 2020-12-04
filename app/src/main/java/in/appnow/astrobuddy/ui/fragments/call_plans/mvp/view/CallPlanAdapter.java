package in.appnow.astrobuddy.ui.fragments.call_plans.mvp.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.rest.response.CallPlan;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;


/**
 * Created by NILESH BHARODIYA on 28-08-2019.
 */
public class CallPlanAdapter extends RecyclerView.Adapter<CallPlansItemVH> {

    private final List<CallPlan> callPlanArrayList = new ArrayList<>(0);
    private PublishSubject<CallPlan> itemViewClickKnowMore = PublishSubject.create();
    private PublishSubject<CallPlan> itemViewClickBuy = PublishSubject.create();

    @NonNull
    @Override
    public CallPlansItemVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.call_plan_item, parent, false);

        return new CallPlansItemVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallPlansItemVH holder, int position) {
        CallPlan callPlan = callPlanArrayList.get(position);
        holder.bindData(callPlan);

        Function<Object, CallPlan> objectCallPlansFunction = model -> callPlanArrayList.get(holder.getAdapterPosition());

        RxView.clicks(holder.buttonBuy)
                .map(objectCallPlansFunction)
                .subscribe(itemViewClickBuy);

        RxView.clicks(holder.buttonKnowMore)
                .map(objectCallPlansFunction)
                .subscribe(itemViewClickKnowMore);
    }

    public void updateData(List<CallPlan> callPlansList) {
        this.callPlanArrayList.clear();

        this.callPlanArrayList.addAll(callPlansList);

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return callPlanArrayList.size();
    }


    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        itemViewClickKnowMore.onComplete();
        itemViewClickBuy.onComplete();
    }

    public Observable<CallPlan> getItemViewClickKnowMore() {
        return itemViewClickKnowMore;
    }

    public Observable<CallPlan> getItemViewClickBuy() {
        return itemViewClickBuy;
    }

}

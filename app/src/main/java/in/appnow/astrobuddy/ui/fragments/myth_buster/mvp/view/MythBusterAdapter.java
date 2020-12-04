package in.appnow.astrobuddy.ui.fragments.myth_buster.mvp.view;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.rest.response.MythBuster;
import in.appnow.astrobuddy.rest.response.MythBusterResponse;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by sonu on 13:20, 16/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class MythBusterAdapter extends RecyclerView.Adapter<MythBusterViewHolder> {
    private final List<MythBuster> mythBusterArrayList = new ArrayList<>(0);
    private PublishSubject<MythBuster> itemViewClickSubject = PublishSubject.create();

    private Context context;

    public MythBusterAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MythBusterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myth_buster_row_layout, parent, false);
        MythBusterViewHolder viewHolder = new MythBusterViewHolder(view);
        RxView.clicks(viewHolder.itemView)
                .map(model -> mythBusterArrayList.get(viewHolder.getAdapterPosition()))
                .subscribe(itemViewClickSubject);
        return viewHolder;
    }

    public Observable<MythBuster> getItemViewClickSubject() {
        return itemViewClickSubject;
    }

    @Override
    public void onBindViewHolder(@NonNull MythBusterViewHolder holder, int position) {
        MythBuster mythBuster = mythBusterArrayList.get(position);
        holder.bindData(context, mythBuster);
    }

    @Override
    public int getItemCount() {
        return mythBusterArrayList.size();
    }

    public void swapData(List<MythBuster> mythBusterArrayList,boolean isMythVideo) {
        this.mythBusterArrayList.clear();

        if (isMythVideo){
            for (int i =0; i < mythBusterArrayList.size(); i++){
                if (mythBusterArrayList.get(i).getMythType().equalsIgnoreCase("VIDEO")){
                    this.mythBusterArrayList.add(mythBusterArrayList.get(i));
                }
            }
        }else {
            for (int i =0; i < mythBusterArrayList.size(); i++){
                if (mythBusterArrayList.get(i).getMythType().equalsIgnoreCase("ARTICLE")){
                    this.mythBusterArrayList.add(mythBusterArrayList.get(i));
                }
            }
        }

//        if (mythBusterArrayList != null && !mythBusterArrayList.isEmpty()) {
//            this.mythBusterArrayList.addAll(mythBusterArrayList);
//        }
        notifyDataSetChanged();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        itemViewClickSubject.onComplete(); //here we avoid memory leaks
    }
}

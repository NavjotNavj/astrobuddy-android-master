package in.appnow.astrobuddy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.models.HomeModel;
import in.appnow.astrobuddy.utils.ImageUtils;

/**
 * Created by Abhishek Thanvi on 19/03/19.
 * Copyright © 2019 Abhishek Thanvi. All rights reserved.
 */

public class HomeItemsRecyclerAdapter extends RecyclerView.Adapter<HomeItemsRecyclerAdapter.HomeItemVH> {

    private HomeModel homeModel;
    private Context context;

    public HomeItemsRecyclerAdapter(Context context,HomeModel homeModel) {
        this.homeModel = homeModel;
        this.context=context;
    }

    @NonNull
    @Override
    public HomeItemVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home_item, parent, false);
        return new HomeItemVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeItemVH holder, int position) {
        ImageUtils.setDrawableImage(context,holder.itemImage,homeModel.getHomeIcons()[position]);
        holder.itemTitle.setText(homeModel.getHomeTitles().get(position));
    }

    @Override
    public int getItemCount() {
        return homeModel.getHomeTitles().size();
    }

   public static class HomeItemVH extends RecyclerView.ViewHolder {

        @BindView(R.id.icon)
       public AppCompatImageView itemImage;

        @BindView(R.id.title)
        AppCompatTextView itemTitle;

        public HomeItemVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

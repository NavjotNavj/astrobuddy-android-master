package in.appnow.astrobuddy.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.models.NavigationModel;
import in.appnow.astrobuddy.utils.ImageUtils;

/**
 * Created by Abhishek Thanvi on 22/03/19.
 * Copyright © 2019 Abhishek Thanvi. All rights reserved.
 */

public class MoreSettingsRecyclerAdapter extends RecyclerView.Adapter<MoreSettingsRecyclerAdapter.MoreVH> {

    private List<NavigationModel> navigationModels;
    private Context context;
    private final PreferenceManger preferenceManger;

    public MoreSettingsRecyclerAdapter(Context context, List<NavigationModel> navigationModels, PreferenceManger preferenceManger) {
        this.navigationModels = navigationModels;
        this.context = context;
        this.preferenceManger = preferenceManger;
    }

    @NonNull
    @Override
    public MoreVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_more_settings, parent, false);
        return new MoreVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoreVH holder, int position) {
        holder.bindData(navigationModels.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return navigationModels.size();
    }

    class MoreVH extends RecyclerView.ViewHolder {

        @BindView(R.id.navigation_row_image_view)
        ImageView navigation_row_image_view;
        @BindView(R.id.navigation_row_label)
        TextView navigation_row_label;
        @BindView(R.id.navigation_row_switch)
        SwitchCompat switchCompat;
        @BindView(R.id.divider_view)
        View divider_view;

        public MoreVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(NavigationModel model) {
            navigation_row_label.setText(model.getLabel());
            if (model.getIcon() != 0) {
                navigation_row_image_view.setImageResource(model.getIcon());
                ImageUtils.changeImageColor(navigation_row_image_view, context, R.color.white);
                navigation_row_image_view.setVisibility(View.VISIBLE);
            } else {
                navigation_row_image_view.setVisibility(View.GONE);
            }

            if (model.isSwitch()) {
                switchCompat.setVisibility(View.VISIBLE);
                switchCompat.setChecked(preferenceManger.getBooleanValue(model.getLabel(), true));
                switchCompat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        preferenceManger.putBoolean(model.getLabel(), switchCompat.isChecked());
                    }
                });
            } else {
                switchCompat.setVisibility(View.GONE);
            }

        }
    }
}

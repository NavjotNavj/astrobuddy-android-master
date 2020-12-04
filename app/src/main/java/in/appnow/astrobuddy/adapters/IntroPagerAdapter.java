package in.appnow.astrobuddy.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.ui.activities.intro.IntroModel;
import in.appnow.astrobuddy.utils.ImageUtils;

/**
 * Created by sonu on 18:11, 23/05/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class IntroPagerAdapter extends PagerAdapter {
    private final List<IntroModel> introModelList = new ArrayList<>(0);

    private final Context context;
    private LayoutInflater layoutInflater;


    public IntroPagerAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return introModelList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup view, int position) {
        View imageLayout = layoutInflater.inflate(R.layout.banner_image_layout, view, false);
        IntroModel model = introModelList.get(position);
        ImageView imageView = imageLayout
                .findViewById(R.id.banner_image_view);
        TextView introTitleLabel = imageLayout.findViewById(R.id.intro_title_label);
        TextView introDescLabel = imageLayout.findViewById(R.id.intro_desc_label);

        ImageUtils.setDrawableImage(context, imageView, model.getIntroImage());
        introTitleLabel.setText(model.getIntroTitle());
        introDescLabel.setText(model.getIntroDesc());

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    public void swapData(List<IntroModel> upIntroList) {
        introModelList.clear();
        if (upIntroList != null && !upIntroList.isEmpty()) {
            introModelList.addAll(upIntroList);
        }
        notifyDataSetChanged();
    }
}

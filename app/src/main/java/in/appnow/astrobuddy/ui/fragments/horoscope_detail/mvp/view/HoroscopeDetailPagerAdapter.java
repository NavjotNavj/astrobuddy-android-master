package in.appnow.astrobuddy.ui.fragments.horoscope_detail.mvp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

import in.appnow.astrobuddy.R;

/**
 * Created by sonu on 11:27, 25/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class HoroscopeDetailPagerAdapter extends PagerAdapter {

    private static final String TAG = HoroscopeDetailPagerAdapter.class.getSimpleName();
    private Context mContext;
    private List<String> horoscopeTypes;
    private OnPagerTextClickListener onPagerTextClickListener;
    private LayoutInflater inflater;

    public HoroscopeDetailPagerAdapter(Context context, List<String> horoscopeTypes) {
        this.mContext = context;
        this.horoscopeTypes = horoscopeTypes;
        this.inflater = LayoutInflater.from(context);
    }

    public void setOnPagerTextClickListener(OnPagerTextClickListener onPagerTextClickListener) {
        this.onPagerTextClickListener = onPagerTextClickListener;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup collection, int position) {
        View view = inflater.inflate(R.layout.adapter_horoscope_detail_types, collection, false);
        AppCompatTextView textView = view.findViewById(R.id.horoscope_type);
        textView.setText(horoscopeTypes.get(position).toUpperCase());

        textView.setOnClickListener(view1 -> {
            if (onPagerTextClickListener != null) {
                onPagerTextClickListener.onClick(position);
            }
        });

        collection.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup collection, int position, @NonNull Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return horoscopeTypes.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, Object object) {
        return view == object;
    }

    public interface OnPagerTextClickListener {
        public void onClick(int position);
    }
}

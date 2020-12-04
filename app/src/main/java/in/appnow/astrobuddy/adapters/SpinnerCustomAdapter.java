package in.appnow.astrobuddy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import in.appnow.astrobuddy.R;

/**
 * Created by sonu on 15:57, 04/06/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class SpinnerCustomAdapter extends BaseAdapter {

    private List<String> stringList;
    private Context context;
    private LayoutInflater layoutInflater;

    public SpinnerCustomAdapter(List<String> stringList, Context context) {
        this.stringList = stringList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return stringList.size();
    }

    @Override
    public Object getItem(int i) {
        return stringList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.spinner_custom_label, viewGroup, false);
        TextView label = view.findViewById(R.id.spinner_custom_label);
        label.setText(stringList.get(i));
        return view;
    }
}

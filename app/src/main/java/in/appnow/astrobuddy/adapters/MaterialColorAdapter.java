package in.appnow.astrobuddy.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

import com.lukedeighton.wheelview.adapter.WheelArrayAdapter;

import java.util.List;
import java.util.Map;

import in.appnow.astrobuddy.utils.TextDrawable;

/**
 * Created by sonu on 12:17, 16/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class MaterialColorAdapter extends WheelArrayAdapter<Map.Entry<String, Integer>> {
    private Context context;
    private int selectedPosition;
    private float fontSize, selectedCenterX, centerX, selectedCenterY, centerY;

    public MaterialColorAdapter(List<Map.Entry<String, Integer>> items, Context context, int selectedPosition, float fontSize, float selectedCenterX, float centerX, float selectedCenterY, float centerY) {
        super(items);
        this.context = context;
        this.selectedPosition = selectedPosition;
        this.fontSize = fontSize;
        this.selectedCenterX = selectedCenterX;
        this.centerX = centerX;
        this.selectedCenterY = selectedCenterY;
        this.centerY = centerY;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    @Override
    public Drawable getDrawable(int position) {
        Drawable[] drawable;
        if (selectedPosition == position) {
            drawable = new Drawable[]{
                    createOvalDrawable(getItem(position).getValue()),
                    new TextDrawable(context, getItem(position).getKey(), true, fontSize, centerX, centerY, selectedCenterX, selectedCenterY)};
        } else {
            drawable = new Drawable[]{
                    createOvalDrawable(getItem(position).getValue()),
                    new TextDrawable(context, getItem(position).getKey(), false, fontSize, centerX, centerY, selectedCenterX, selectedCenterY)};
        }

        return new LayerDrawable(drawable);
    }

    private Drawable createOvalDrawable(int drawable) {
        return context.getResources().getDrawable(drawable);
    }

}

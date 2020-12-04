package in.appnow.astrobuddy.utils;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;

import in.appnow.astrobuddy.R;

public class TextDrawable extends Drawable {

    private final String text;
    private final Paint paint;
    private final boolean isSelected;

    private float fontSize, selectedCenterX, centerX, selectedCenterY, centerY;

    public TextDrawable(Context context, String text, boolean isSelected,float fontSize, float centerX, float centerY,float selectedCenterX,float selectedCenterY) {

        this.text = text;
        this.isSelected = isSelected;
        this.fontSize =fontSize;
        this.centerX=centerX;
        this.centerY =centerY;
        this.selectedCenterX =selectedCenterX;
        this.selectedCenterY=selectedCenterY;

        this.paint = new Paint();
        if (isSelected) paint.setColor(context.getResources().getColor(R.color.colorPrimary));
        else paint.setColor(Color.WHITE);
        paint.setTextSize(fontSize);
        paint.setAntiAlias(true);
        //paint.setFakeBoldText(true);
        //paint.setShadowLayer(12f, 0, 0, Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.LEFT);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect bounds = getBounds();
        /*      canvas.drawText(text, bounds.centerX() - 15f *//*just a lazy attempt to centre the text*//* * text.length(), bounds.centerY() + 15f, paint);*/
        if (isSelected) {
            canvas.drawText(text, bounds.centerX() - selectedCenterX  /*just a lazy attempt to centre the text*/ * text.length(), bounds.centerY() + selectedCenterY, paint);
        } else {
            canvas.drawText(text, bounds.centerX() - centerX  /*just a lazy attempt to centre the text*/ * text.length(), bounds.centerY() + centerY, paint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public String getText() {
        return text;
    }
}
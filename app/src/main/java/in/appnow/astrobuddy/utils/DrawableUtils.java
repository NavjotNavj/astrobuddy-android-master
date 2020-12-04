package in.appnow.astrobuddy.utils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

import androidx.core.graphics.drawable.DrawableCompat;

public class DrawableUtils {

    public static Drawable changeDrawableColor(Context context, int image, int color) {
        Drawable drawable = context.getResources().getDrawable(image);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, context.getResources().getColor(color));
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
        return drawable;
    }
}

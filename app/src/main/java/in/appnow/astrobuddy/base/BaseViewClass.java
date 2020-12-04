package in.appnow.astrobuddy.base;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by sonu on 13:06, 06/08/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class BaseViewClass extends FrameLayout {

    public BaseViewClass(@NonNull Context context) {
        super(context);
    }


    /**
     * If you just want to protect your app from tap jacking due to another app drawing over your app you can add setFilterTouchesWhenObscured to your views via XML or programmatically.


     There is a View#onFilterTouchEventForSecurity() method you can override to detect if the motion event has the FLAG_WINDOW_IS_OBSCURED. This will let you know if something is drawn on top of your view.

     * @param event
     * @return
     */
    @Override
    public boolean onFilterTouchEventForSecurity(MotionEvent event) {
        if ((event.getFlags() & MotionEvent.FLAG_WINDOW_IS_OBSCURED) == MotionEvent.FLAG_WINDOW_IS_OBSCURED) {
            // show error message
            return false;
        }
        return super.onFilterTouchEventForSecurity(event);
    }

}

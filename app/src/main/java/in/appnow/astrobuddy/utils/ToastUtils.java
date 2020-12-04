package in.appnow.astrobuddy.utils;

import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.IntDef;
import androidx.annotation.StringRes;

import in.appnow.astrobuddy.app.AstroApplication;

/**
 * Created by Abhishek Thanvi on 28/03/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class ToastUtils {

    public static void shortToast(@StringRes int text) {
        shortToast(AstroApplication.getInstance().getString(text));
    }

    public static void shortToast(String text) {
        if (TextUtils.isEmpty(text))
            return;
        show(text, Toast.LENGTH_SHORT);
    }

    public static void longToast(@StringRes int text) {
        longToast(AstroApplication.getInstance().getString(text));
    }

    public static void longToast(String text) {
        if (TextUtils.isEmpty(text))
            return;
        show(text, Toast.LENGTH_LONG);
    }

    private static Toast makeToast(String text, @ToastLength int length) {
        return Toast.makeText(AstroApplication.getInstance(), text, length);
    }

    private static void show(String text, @ToastLength int length) {
        makeToast(text, length).show();
    }

    @IntDef({Toast.LENGTH_LONG, Toast.LENGTH_SHORT})
    private @interface ToastLength {

    }
}
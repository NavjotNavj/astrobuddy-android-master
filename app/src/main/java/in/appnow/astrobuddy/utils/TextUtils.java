package in.appnow.astrobuddy.utils;

import android.text.InputFilter;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by sonu on 16:07, 25/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class TextUtils {

    public static boolean isNumberExist(String value) {
        Pattern numberPat = Pattern.compile("\\d+");
        Matcher matcher = numberPat.matcher(value);
        return matcher.find();
    }

    public static void setTextMaxLength(EditText editText, int maxLength) {
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
    }

}

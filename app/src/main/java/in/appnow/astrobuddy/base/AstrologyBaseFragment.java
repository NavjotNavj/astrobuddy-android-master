package in.appnow.astrobuddy.base;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.horoscope_chart.APITask;
import in.appnow.astrobuddy.utils.ImageUtils;
import in.appnow.astrobuddy.utils.Logger;

/**
 * Created by sonu on 13:03, 17/04/19
 * Copyright (c) 2019 . All rights reserved.
 */
public abstract class AstrologyBaseFragment extends BaseFragment {

    private String currentLang = APITask.ENGLISH_LANG;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.astrology_lang_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_language_menu);
        View actionView = menuItem.getActionView();
        TextView englishLang = actionView.findViewById(R.id.language_english_button);
        TextView hindiLang = actionView.findViewById(R.id.language_hindi_button);

        if (currentLang.equalsIgnoreCase(APITask.ENGLISH_LANG)) {
            englishLangSelection(englishLang, hindiLang);
        } else if (currentLang.equalsIgnoreCase(APITask.HINDI_LANG)) {
            hindiLangSelection(englishLang, hindiLang);
        }

        englishLang.setOnClickListener(v -> {
            if(!currentLang.equalsIgnoreCase(APITask.ENGLISH_LANG)) {
                englishLangSelection(englishLang, hindiLang);
                this.currentLang = APITask.ENGLISH_LANG;
                onLanguageSelected(currentLang);
            }
        });
        hindiLang.setOnClickListener(v -> {
            if(!currentLang.equalsIgnoreCase(APITask.HINDI_LANG)) {
                hindiLangSelection(englishLang, hindiLang);
                this.currentLang = APITask.HINDI_LANG;
                onLanguageSelected(currentLang);
            }
        });

    }

    private void hindiLangSelection(TextView englishLang, TextView hindiLang) {
        try {
            englishLang.setBackground(ImageUtils.changeShapeColor(getContext(), R.drawable.left_curve_bg, R.color.white));
            hindiLang.setBackground(ImageUtils.changeShapeColor(getContext(), R.drawable.right_curve_bg, R.color.blue));

            if (getContext() != null) {
                englishLang.setTextColor(getContext().getResources().getColor(R.color.gunmetal));
                hindiLang.setTextColor(getContext().getResources().getColor(R.color.white));
            }
        } catch (Exception ignored) {

        }
    }

    private void englishLangSelection(TextView englishLang, TextView hindiLang) {
        try {
            englishLang.setBackground(ImageUtils.changeShapeColor(getContext(), R.drawable.left_curve_bg, R.color.blue));
            hindiLang.setBackground(ImageUtils.changeShapeColor(getContext(), R.drawable.right_curve_bg, R.color.white));

            if (getContext() != null) {
                englishLang.setTextColor(getContext().getResources().getColor(R.color.white));
                hindiLang.setTextColor(getContext().getResources().getColor(R.color.gunmetal));
            }
        } catch (Exception ignored) {

        }
    }

    /* @Override
     public void onPrepareOptionsMenu(Menu menu) {
         super.onPrepareOptionsMenu(menu);
         MenuItem menuItem = menu.findItem(R.id.action_language_menu);
         View actionView = menuItem.getActionView();
         TextView englishLang = actionView.findViewById(R.id.language_english_button);
         TextView hindiLang = actionView.findViewById(R.id.language_hindi_button);
     }
 */
    public abstract void onLanguageSelected(String language);

    public void invalidateOptionMenu(String language) {
        if (!TextUtils.isEmpty(language))
            this.currentLang = language;
        if (getActivity() != null) {
            getActivity().invalidateOptionsMenu();
        }
    }

}

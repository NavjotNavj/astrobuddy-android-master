package in.appnow.astrobuddy.ui.common_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.base.BaseActivity;
import in.appnow.astrobuddy.ui.WebViewFragment;
import in.appnow.astrobuddy.ui.common_activity.dagger.CommonActivityComponent;
import in.appnow.astrobuddy.ui.common_activity.dagger.DaggerCommonActivityComponent;
import in.appnow.astrobuddy.ui.fragments.HelpFragment;
import in.appnow.astrobuddy.ui.fragments.call_plans.CallPlansFragment;
import in.appnow.astrobuddy.utils.FragmentUtils;

/**
 * Created by sonu on 15:26, 19/07/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class CommonActivity extends BaseActivity {

    private static final String ARG_TITLE = "title";
    private static final String ARG_FRAGMENT_TAG = "frg_tag";
    private static final String ARG_URL = "url";
    @BindView(R.id.common_toolbar)
    Toolbar toolbar;

    private CommonActivityComponent component;

    public static void openCommonActivity(Context context, String title, String fragmentToReplace) {
        Intent intent = new Intent(context, CommonActivity.class);
        intent.putExtra(ARG_TITLE, title);
        intent.putExtra(ARG_FRAGMENT_TAG, fragmentToReplace);
        context.startActivity(intent);
    }

    public static void openCommonActivity(Context context, String title, String url, String fragmentToReplace) {
        Intent intent = new Intent(context, CommonActivity.class);
        intent.putExtra(ARG_TITLE, title);
        intent.putExtra(ARG_FRAGMENT_TAG, fragmentToReplace);
        intent.putExtra(ARG_URL, url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component = DaggerCommonActivityComponent.builder()
                .appComponent(AstroApplication.get(this).component())
                .build();
        component.inject(this);

        setContentView(R.layout.activity_common);
        ButterKnife.bind(this);
        setUpToolbar();
        loadFragment();
    }

    public CommonActivityComponent getComponent() {
        return component;
    }

    private void setUpToolbar() {
        toolbar.setNavigationOnClickListener(view ->finish());
        toolbar.setTitle(getIntent().getStringExtra(ARG_TITLE));
    }

    private void loadFragment() {
        if (getIntent() != null && getIntent().hasExtra(ARG_FRAGMENT_TAG)) {
            String fragmentTag = getIntent().getStringExtra(ARG_FRAGMENT_TAG);
            if (fragmentTag == null)
                return;
            if (fragmentTag.equalsIgnoreCase(FragmentUtils.CALL_PLAN_FRAGMENT)) {
                //FragmentUtils.replaceFragmentCommon(getSupportFragmentManager(), R.id.common_frame_container, WebViewFragment.newInstance("https://www.astrobuddy.guru/schedual-call.php", "", false), fragmentTag, false);
                FragmentUtils.replaceFragmentCommon(getSupportFragmentManager(), R.id.common_frame_container, CallPlansFragment.newInstance(), FragmentUtils.CALL_PLAN_FRAGMENT, false);

            } else if (fragmentTag.equalsIgnoreCase(FragmentUtils.WEB_VIEW_FRAGMENT)) {
                FragmentUtils.replaceFragmentCommon(getSupportFragmentManager(), R.id.common_frame_container, WebViewFragment.newInstance("https://www.astrobuddy.guru/privacy-policy.php", "", false), fragmentTag, false);

            } else if (fragmentTag.equalsIgnoreCase(FragmentUtils.ACCOUNT_HELP_FRAGMENT)) {
                FragmentUtils.replaceFragmentCommon(getSupportFragmentManager(), R.id.common_frame_container, HelpFragment.newInstance(), FragmentUtils.ACCOUNT_HELP_FRAGMENT, false);
            } else {
                String url = getIntent().getStringExtra(ARG_URL);
                FragmentUtils.replaceFragmentCommon(getSupportFragmentManager(), R.id.common_frame_container, WebViewFragment.newInstance(url, getIntent().getStringExtra(ARG_TITLE), false), fragmentTag, false);
            }
        }
    }
}

package in.appnow.astrobuddy.ui.fragments.more.mvp;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.BuildConfig;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.adapters.MoreSettingsRecyclerAdapter;
import in.appnow.astrobuddy.base.BaseView;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.dialog.DialogHelperClass;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.helper.RecyclerItemClickListener;
import in.appnow.astrobuddy.models.NavigationModel;
import in.appnow.astrobuddy.ui.fragments.contact_us.ContactUsFragment;
import in.appnow.astrobuddy.ui.fragments.myprofile.MyProfileFragment;
import in.appnow.astrobuddy.utils.AppUtils;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.ToastUtils;

/**
 * Created by Abhishek Thanvi on 22/03/19.
 * Copyright © 2019 Abhishek Thanvi. All rights reserved.
 */

public class MoreSettingsFragmentView extends BaseViewClass implements BaseView {


    @BindView(R.id.background_image)
    ImageView backgroundImage;
    @BindView(R.id.more_list_rv)
    RecyclerView moreItemLV;
    @BindView(R.id.app_version_label)
    AppCompatTextView appVersionLabel;

    @BindString(R.string.referral_email_subject)
    String emailSubject;
    @BindString(R.string.referral_share_new_message)
    String shareMessage;

    private MoreSettingsRecyclerAdapter adapter;
    public FragmentManager fragmentManager;
    private Context context;

    private LogoutListener logoutListener;

    public MoreSettingsFragmentView(@NonNull Context context) {
        super(context);
        this.context = context;
        inflate(getContext(), R.layout.fragment_more_settings, this);
        ButterKnife.bind(this, this);

        appVersionLabel.setText("Version \n" + BuildConfig.VERSION_NAME);

        moreItemLV.addOnItemTouchListener(
                new RecyclerItemClickListener(context, moreItemLV, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        doReplaceFragment(position);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                })
        );

    }


    public void setUpMoreList(List<NavigationModel> navigationModelList, PreferenceManger preferenceManger) {
        moreItemLV.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        adapter = new MoreSettingsRecyclerAdapter(context, navigationModelList, preferenceManger);
        moreItemLV.setAdapter(adapter);
    }

    @Override
    public void onUnknownError(String error) {

    }

    @Override
    public void onTimeout() {

    }

    @Override
    public void onNetworkError() {

    }

    @Override
    public boolean isNetworkConnected() {
        return false;
    }

    @Override
    public void onConnectionError() {

    }

    public void setLogoutListener(LogoutListener logoutListener) {
        this.logoutListener = logoutListener;
    }

    public void doReplaceFragment(int position) {
        switch (position) {
            case 0:
                replaceFragment(MyProfileFragment.newInstance(), FragmentUtils.MY_PROFILE_FRAGMENT);
                break;
            case 1:
                replaceFragment(ContactUsFragment.newInstance(), FragmentUtils.CONTACT_US_FRAGMENT);
                break;
            case 2:
                AppUtils.shareData(context, "more", emailSubject, shareMessage);
                break;
            case 4:
                DialogHelperClass.showMessageOKCancel(getContext(), "Are you sure you want to Logout?", "Logout", "Cancel", (dialogInterface, i) -> {
                    if (logoutListener != null) {
                        logoutListener.onDoLogout();
                    } else {
                        ToastUtils.shortToast("Oops!! Unknown error occurred.");
                    }
                    //doLogout();
                }, (dialogInterface, i) -> {

                });
                break;
        }
    }


    public void replaceFragment(Fragment fragment, String fragmentTag) {
        FragmentUtils.onChangeFragment(fragmentManager, R.id.container_view, fragment, fragmentTag);
    }

    public interface LogoutListener {
        void onDoLogout();
    }
}

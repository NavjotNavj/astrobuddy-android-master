package in.appnow.astrobuddy.ui.fragments.myprofile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseFragment;
import in.appnow.astrobuddy.ui.activities.home.HomeActivity;
import in.appnow.astrobuddy.ui.fragments.myprofile.mvp.MyProfileModel;
import in.appnow.astrobuddy.ui.fragments.myprofile.mvp.MyProfilePresenter;
import in.appnow.astrobuddy.ui.fragments.myprofile.mvp.MyProfileView;
import in.appnow.astrobuddy.utils.ToastUtils;

/**
 * Created by Abhishek Thanvi on 29/03/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class MyProfileFragment extends BaseFragment implements MyProfilePresenter.OnImageUploadListener {
    private static final String TAG = MyProfileFragment.class.getSimpleName();
    @Inject
    MyProfileView view;
    @Inject
    MyProfilePresenter presenter;

    private MyProfilePresenter.OnImageUploadListener onImageUploadListener;


    public static MyProfileFragment newInstance() {
        Bundle args = new Bundle();
        MyProfileFragment fragment = new MyProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            updateToolbarTitle(context.getResources().getString(R.string.action_profile));
            showHideToolbar(true);
            showHideBackButton(true);
            onImageUploadListener = (MyProfilePresenter.OnImageUploadListener) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((HomeActivity) getActivity()).getComponent().inject(this);
        presenter.onCreate();
        presenter.setOnImageUploadListener(this);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MyProfileModel.GALLERY_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri imageUri = data.getData();
                    presenter.onImagePick(imageUri);
                    //Logger.DebugLog(TAG, "Image URI : " + imageUri);
                } else {
                    ToastUtils.longToast("Failed to pick image from gallery.");
                }
                break;
            case MyProfileModel.CAMERA_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    presenter.onImageCapture();
                } else {
                    ToastUtils.longToast("Failed to capture image.");
                }
                break;
            default:

                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }

    @Override
    public void onImageUploaded() {
        if (onImageUploadListener != null) {
            onImageUploadListener.onImageUploaded();
        }
    }


}

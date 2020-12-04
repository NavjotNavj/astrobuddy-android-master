package in.appnow.astrobuddy.ui.fragments.myprofile.mvp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;

import in.appnow.astrobuddy.BuildConfig;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.dialog.DialogHelperClass;
import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.response.BaseResponseModel;
import in.appnow.astrobuddy.rest.request.UpdateEmailRequest;
import in.appnow.astrobuddy.rest.response.UpdateEmailResponse;
import in.appnow.astrobuddy.rest.request.UpdateImageRequest;
import in.appnow.astrobuddy.rest.response.UpdateImageResponse;
import in.appnow.astrobuddy.rest.request.UpdateMaritalStatusRequest;
import in.appnow.astrobuddy.ui.fragments.changePassword.ChangePasswordFragment;
import in.appnow.astrobuddy.utils.DeviceUtils;
import in.appnow.astrobuddy.utils.FileUtils;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.Logger;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;

/**
 * Created by sonu on 16:18, 16/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class MyProfileModel {
    private final AppCompatActivity appCompatActivity;
    private final APIInterface apiInterface;

    public static final int GALLERY_REQUEST_CODE = 332;
    public static final int CAMERA_REQUEST_CODE = 333;
    private Uri cameraFileURI;
    private String imagePath = "";


    public MyProfileModel(AppCompatActivity appCompatActivity, APIInterface apiInterface) {
        this.appCompatActivity = appCompatActivity;
        this.apiInterface = apiInterface;
    }

    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    public void showProgressBar() {
        ProgressDialogFragment.showProgress(appCompatActivity.getSupportFragmentManager());
    }

    public void hideProgressBar() {
        ProgressDialogFragment.dismissProgress(appCompatActivity.getSupportFragmentManager());

    }

    public void onPermissionGranted() {
        DialogHelperClass.showListDialog(appCompatActivity, appCompatActivity.getResources().getString(R.string.choose), appCompatActivity.getResources().getStringArray(R.array.pick_picture_array), (dialogInterface, i) -> {
            switch (i) {
                case 0:
                    //Gallery
                    selectImageFromGallery();
                    break;
                case 1:
                    //Camera
                    captureImageFormCamera();
                    break;
            }
        });
    }

    public void onPermissionDenied() {
        ToastUtils.shortToast("You cannot change your profile picture without allowing this permissions.");
    }

    public void onPermissionDeniedPermanently() {
        ToastUtils.shortToast("Please go to Setting and allow permissions.");

    }

    private void selectImageFromGallery() {
        Intent in = new Intent(Intent.ACTION_PICK);
        in.setType("image/*");
        appCompatActivity.startActivityForResult(in, GALLERY_REQUEST_CODE);
    }

    private void captureImageFormCamera() {
        if (!DeviceUtils.isDeviceSupportCamera(appCompatActivity)) {
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File createImageFile = FileUtils.createImageFile(appCompatActivity);
        cameraFileURI = FileProvider.getUriForFile(appCompatActivity, BuildConfig.FILES_AUTHORITY, createImageFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraFileURI);
        imagePath = createImageFile.getPath();
        Logger.ErrorLog("Image captured path: ", imagePath);
        for (ResolveInfo resolveInfo : appCompatActivity.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)) {
            appCompatActivity.grantUriPermission(resolveInfo.activityInfo.packageName, cameraFileURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        if (intent.resolveActivity(appCompatActivity.getPackageManager()) != null) {
            appCompatActivity.startActivityForResult(intent, CAMERA_REQUEST_CODE);
        } else {
            ToastUtils.longToast("No app to capture image.");
        }
    }

    public Uri getCameraFileURI() {
        return cameraFileURI;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Observable<UpdateEmailResponse> updateEmail(UpdateEmailRequest updateEmailRequest) {
        return apiInterface.updateEmail(updateEmailRequest);
    }

    public Observable<UpdateImageResponse> updateProfileImage(UpdateImageRequest updateImageRequest) {
        return apiInterface.updateUserProfileImage(updateImageRequest);
    }

    public void replaceChangePasswordFragment() {
        FragmentUtils.onChangeFragment(appCompatActivity.getSupportFragmentManager(), R.id.container_view, ChangePasswordFragment.newInstance(), FragmentUtils.CHANGE_PASSWORD_FRAGMENT);
    }

    public void showDialogToUpdateMaritalStatus(String[] maritalStatusArray, int checkedItem, DialogInterface.OnClickListener selectListener, DialogInterface.OnClickListener onClickListener) {
        DialogHelperClass.showSingleChoiceListDialog(appCompatActivity, "Change Marital Status", maritalStatusArray, checkedItem, "Update", "Cancel", selectListener, onClickListener);
    }

    public Observable<BaseResponseModel> updateMaritalStatus(UpdateMaritalStatusRequest request) {
        return apiInterface.updateMaritalStatus(request);
    }
}

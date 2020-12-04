package in.appnow.astrobuddy.ui.fragments.myprofile.mvp;

import android.Manifest;
import android.net.Uri;
import android.text.TextUtils;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.base.BasePresenter;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.dialog.UnAuthoriseUserDialog;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.rest.CallbackWrapper;
import in.appnow.astrobuddy.rest.request.UpdateEmailRequest;
import in.appnow.astrobuddy.rest.request.UpdateImageRequest;
import in.appnow.astrobuddy.rest.request.UpdateMaritalStatusRequest;
import in.appnow.astrobuddy.rest.response.BaseResponseModel;
import in.appnow.astrobuddy.rest.response.LoginResponseModel;
import in.appnow.astrobuddy.rest.response.UpdateEmailResponse;
import in.appnow.astrobuddy.rest.response.UpdateImageResponse;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.ImageUtils;
import in.appnow.astrobuddy.utils.Logger;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by sonu on 16:19, 16/04/18
 * Copyright (c) 2018 . All rights reserved.
 */
public class MyProfilePresenter implements BasePresenter {
    private static final String TAG = MyProfilePresenter.class.getSimpleName();
    private final MyProfileView view;
    private final MyProfileModel model;

    private final CompositeDisposable disposable = new CompositeDisposable();
    private RxPermissions rxPermissions;
    private final PreferenceManger preferenceManger;
    private ABDatabase abDatabase;

    private String selectedMaritalStatus;

    private OnImageUploadListener onImageUploadListener;

    public MyProfilePresenter(MyProfileView view, MyProfileModel model, PreferenceManger preferenceManger, ABDatabase abDatabase) {
        this.view = view;
        this.model = model;
        this.preferenceManger = preferenceManger;
        this.abDatabase = abDatabase;
    }

    @Override
    public void onCreate() {
        this.rxPermissions = new RxPermissions(model.getAppCompatActivity());
        disposable.add(observeProfileImageSelect());
        disposable.add(observeChangePassword());
        disposable.add(observeEditEmail());
        disposable.add(observeEditMaritalStatus());
        view.setUpUserInfo(preferenceManger);
    }

    public void setOnImageUploadListener(OnImageUploadListener onImageUploadListener) {
        this.onImageUploadListener = onImageUploadListener;
    }

    private Disposable observeChangePassword() {
        return view.observeChangePasswordButton()
                .subscribe(__ -> model.replaceChangePasswordFragment());
    }

    private Disposable observeEditMaritalStatus() {
        return view.observeEditMaritalStatus()
                .subscribe(__ -> {
                    model.showDialogToUpdateMaritalStatus(view.getMaritalStatusArray(), view.getCheckedItem(), (dialogInterface, i) -> {
                        selectedMaritalStatus = view.getMaritalStatusArray()[i];
                    }, (dialogInterface, i) -> {
                        if (!TextUtils.isEmpty(selectedMaritalStatus)) {
                            if (!selectedMaritalStatus.equalsIgnoreCase(view.getCurrentMaritalStatus())) {
                                if (AstroApplication.getInstance().isInternetConnected(true)) {
                                    disposable.add(updateMaritalStatus(selectedMaritalStatus));
                                    selectedMaritalStatus = "";
                                }
                            }
                        }
                    });
                });
    }

    private Disposable updateMaritalStatus(String maritalStatus) {
        ProgressDialogFragment.showProgress(model.getAppCompatActivity().getSupportFragmentManager());
        UpdateMaritalStatusRequest updateMaritalStatusRequest = new UpdateMaritalStatusRequest();
        updateMaritalStatusRequest.setMaritalStatus(maritalStatus);
        updateMaritalStatusRequest.setUserId(preferenceManger.getUserDetails().getUserProfile().getUserId());
        return model.updateMaritalStatus(updateMaritalStatusRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> ProgressDialogFragment.dismissProgress(model.getAppCompatActivity().getSupportFragmentManager()))
                .subscribeWith(new CallbackWrapper<BaseResponseModel>(view, this) {
                    @Override
                    protected void onSuccess(BaseResponseModel data) {
                        if (data != null) {
                            ToastUtils.shortToast(data.getErrorMessage());
                            if (!data.isErrorStatus()) {
                                view.updateMaritalStatus(maritalStatus);
                                LoginResponseModel loginResponseModel = preferenceManger.getUserDetails();
                                loginResponseModel.getUserProfile().setMaritalStatus(maritalStatus);
                                preferenceManger.putUserDetails(loginResponseModel);
                            }
                        } else {
                            ToastUtils.shortToast("Failed to update status. Please try again.");
                        }
                    }
                });

    }


    private Disposable observeProfileImageSelect() {
        return view.observeEditImageButtonClick()
                .compose(rxPermissions.ensureEachCombined(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                .subscribe(permission -> { // will emit 1 Permission object
                            if (permission.granted) {
                                // All permissions are granted !
                                model.onPermissionGranted();
                            } else if (permission.shouldShowRequestPermissionRationale) {
                                // At least one denied permission without ask never again
                                model.onPermissionDenied();

                            } else {
                                // At least one denied permission with ask never again
                                // Need to go to the settings
                                model.onPermissionDeniedPermanently();
                            }
                        }
                );
    }

    private Disposable observeEditEmail() {
        return view.observeEditEmail()
                .doOnNext(__ -> ProgressDialogFragment.showProgress(model.getAppCompatActivity().getSupportFragmentManager()))
                .map(isEnabled -> view.isEmailEnabled() && view.isEmailValid() && AstroApplication.getInstance().isInternetConnected(true))
                .observeOn(Schedulers.io())
                .switchMap(isEnabled -> {
                    if (isEnabled) {
                        UpdateEmailRequest updateEmailRequest = new UpdateEmailRequest();
                        updateEmailRequest.setEmailId(view.getEmailId());
                        updateEmailRequest.setUserId(preferenceManger.getUserDetails().getUserProfile().getUserId());
                        return model.updateEmail(updateEmailRequest);
                    } else {
                        return Observable.just(new UpdateEmailResponse());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(__ -> ProgressDialogFragment.dismissProgress(model.getAppCompatActivity().getSupportFragmentManager()))
                .subscribeWith(new CallbackWrapper<UpdateEmailResponse>(view, this) {
                    @Override
                    protected void onSuccess(UpdateEmailResponse data) {
                        if (data != null) {
                            ToastUtils.shortToast(data.getErrorMessage());
                            if (!data.isErrorStatus() && data.getEmailUpdated()) {
                                view.enableDisableEmailEditText(false);
                                LoginResponseModel loginResponseModel = preferenceManger.getUserDetails();
                                loginResponseModel.getUserProfile().setEmail(view.getEmailId());
                                preferenceManger.putUserDetails(loginResponseModel);
                            }
                        }
                    }
                });
    }


    public void onImagePick(Uri imageUri) {
        view.displayImage(imageUri);
        //Logger.DebugLog(TAG, "image " + imageUri + "\n" + ImageUtils.getRealPathFromUri(model.getAppCompatActivity(), imageUri));
        //updateProfile(ImageUtils.getRealPathFromUri(model.getAppCompatActivity(), imageUri));
        updateProfileImage(ImageUtils.getRealPathFromUri(model.getAppCompatActivity(), imageUri));

    }

    public void onImageCapture() {
        view.displayImage(model.getCameraFileURI());
        if (!TextUtils.isEmpty(model.getCameraFileURI().getPath())) {
            File imageFile = new File(model.getCameraFileURI().getPath());
            Logger.DebugLog(TAG, "image " + model.getCameraFileURI() + "\n" + imageFile.getAbsolutePath());
            updateProfileImage(model.getImagePath());
        } else {
            ToastUtils.longToast("Failed to upload image. Please try again.");
        }
    }

    private void updateProfileImage(String imagePath) {
        if (TextUtils.isEmpty(imagePath)) {
            ToastUtils.longToast("Failed to upload image. Please try again.");
            return;
        }
        if (AstroApplication.getInstance().isInternetConnected(true)) {
            try {
                String base64Image = ImageUtils.convertBitmapIntoBase64(ImageUtils.convertImagePathToBitmap(imagePath, 400, 400));
                disposable.add(updateProfile(base64Image));
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.longToast("Failed to upload image. Please try again.");
            }
        }

    }

    private Disposable updateProfile(String base64Image) {
        UpdateImageRequest updateImageRequest = new UpdateImageRequest();
        updateImageRequest.setUserId(preferenceManger.getUserDetails().getUserProfile().getUserId());
        updateImageRequest.setProfileImage(base64Image);
        return model.updateProfileImage(updateImageRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(error -> Logger.ErrorLog(TAG, "error : " + error.getLocalizedMessage()))
                .subscribeWith(new CallbackWrapper<UpdateImageResponse>(view, this) {
                    @Override
                    protected void onSuccess(UpdateImageResponse data) {
                        if (data != null) {
                            if (data.isErrorStatus()) {
                                ToastUtils.shortToast(data.getErrorMessage());
                            } else if (data.isImageUpdated()) {
                                ToastUtils.shortToast(data.getErrorMessage());
                                LoginResponseModel loginResponseModel = preferenceManger.getUserDetails();
                                loginResponseModel.getUserProfile().setProfileImage(data.getProfileImage());
                                preferenceManger.putUserDetails(loginResponseModel);
                                if (onImageUploadListener != null) {
                                    onImageUploadListener.onImageUploaded();
                                }
                            }
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        disposable.clear();
    }

    @Override
    public void unAuthorizeUserAccess(String message) {
        UnAuthoriseUserDialog.getInstance().showLogOutDialog(
                model.getAppCompatActivity(),
                message,
                preferenceManger,
                abDatabase,
                new UnAuthoriseUserDialog.OnLogoutListener() {
                    @Override
                    public void OnClickLogOut() {
                        model.showProgressBar();
                    }

                    @Override
                    public void OnLogOut() {
                        model.hideProgressBar();
                        FragmentUtils.onLogoutSuccess(model.getAppCompatActivity());
                    }
                });
    }

    public interface OnImageUploadListener {
        public void onImageUploaded();
    }
}

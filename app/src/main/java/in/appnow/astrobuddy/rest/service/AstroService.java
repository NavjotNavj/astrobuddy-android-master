package in.appnow.astrobuddy.rest.service;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import in.appnow.astrobuddy.BuildConfig;
import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.dialog.ProgressDialogFragment;
import in.appnow.astrobuddy.interfaces.APICallback;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.request.AppDownloadRequest;
import in.appnow.astrobuddy.rest.request.PostUserStatsRequest;
import in.appnow.astrobuddy.rest.request.TrackNotificationClickRequest;
import in.appnow.astrobuddy.rest.request.TransactionReportRequest;
import in.appnow.astrobuddy.rest.response.BaseResponseModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Abhishek Thanvi on 28/03/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class AstroService {


    public static Call<ResponseBody> getRSA(Context context, APIInterface apiInterface, String orderId, final APICallback apiCallback, final int requestCode) {
        if (!AstroApplication.getInstance().isInternetConnected(true)) {
            return null;
        }
        ProgressDialogFragment.showProgress(((AppCompatActivity) context).getSupportFragmentManager());
        Call<ResponseBody> call = apiInterface.getRSA(BuildConfig.ACCESS_CODE, orderId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                apiCallback.onResponse(call, response, requestCode, null);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                apiCallback.onFailure(call, t, requestCode, null);
                ProgressDialogFragment.dismissProgress(((AppCompatActivity) context).getSupportFragmentManager());
            }
        });
        return null;
    }

    public static Call<BaseResponseModel> submitTransactionReport(Context context, APIInterface apiInterface, TransactionReportRequest reportRequest, final APICallback apiCallback, final int requestCode) {
        if (!AstroApplication.getInstance().isInternetConnected(true)) {
            return null;
        }
        ProgressDialogFragment.showProgress(((AppCompatActivity) context).getSupportFragmentManager());
        Call<BaseResponseModel> call = apiInterface.transactionReport(reportRequest);
        call.enqueue(new Callback<BaseResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponseModel> call, @NonNull Response<BaseResponseModel> response) {
                apiCallback.onResponse(call, response, requestCode, null);
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponseModel> call, @NonNull Throwable t) {
                apiCallback.onFailure(call, t, requestCode, null);
                ProgressDialogFragment.dismissProgress(((AppCompatActivity) context).getSupportFragmentManager());
            }
        });
        return null;
    }

    public static Call<BaseResponseModel> sendAppDownload(APIInterface apiInterface, AppDownloadRequest request, final APICallback apiCallback, final int requestCode) {
        if (!AstroApplication.getInstance().isInternetConnected(false)) {
            return null;
        }
        Call<BaseResponseModel> call = apiInterface.appDownload(request);
        call.enqueue(new Callback<BaseResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponseModel> call, @NonNull Response<BaseResponseModel> response) {
                apiCallback.onResponse(call, response, requestCode, null);
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponseModel> call, @NonNull Throwable t) {
                apiCallback.onFailure(call, t, requestCode, null);
            }
        });
        return null;
    }

    public static Call<BaseResponseModel> trackNotificationClick(APIInterface apiInterface, TrackNotificationClickRequest request, final APICallback apiCallback, final int requestCode) {
        if (!AstroApplication.getInstance().isInternetConnected(false)) {
            return null;
        }
        Call<BaseResponseModel> call = apiInterface.trackNotificationClick(request);
        call.enqueue(new Callback<BaseResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponseModel> call, @NonNull Response<BaseResponseModel> response) {
                apiCallback.onResponse(call, response, requestCode, null);
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponseModel> call, @NonNull Throwable t) {
                apiCallback.onFailure(call, t, requestCode, null);
            }
        });
        return null;
    }

    public static void postUserStats(APIInterface apiInterface, PostUserStatsRequest request, final APICallback apiCallback, final int requestCode) {
        if (!AstroApplication.getInstance().isInternetConnected(false)) {
            apiCallback.onNoNetwork(requestCode);
        }
        Call<BaseResponseModel> call = apiInterface.postUserStats(request);
        call.enqueue(new Callback<BaseResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponseModel> call, @NonNull Response<BaseResponseModel> response) {
                apiCallback.onResponse(call, response, requestCode, null);
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponseModel> call, @NonNull Throwable t) {
                apiCallback.onFailure(call, t, requestCode, null);
            }
        });
    }


}

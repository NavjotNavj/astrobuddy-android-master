package in.appnow.astrobuddy.services;

import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.Nullable;
import android.text.TextUtils;

import javax.inject.Inject;

import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.dagger.component.DaggerMyServiceComponent;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.interfaces.APICallback;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.rest.request.AppDownloadRequest;
import in.appnow.astrobuddy.rest.request.TrackNotificationClickRequest;
import in.appnow.astrobuddy.rest.response.BaseResponseModel;
import in.appnow.astrobuddy.rest.service.AstroService;
import in.appnow.astrobuddy.utils.DeviceUtils;
import in.appnow.astrobuddy.utils.Logger;
import retrofit2.Call;
import retrofit2.Response;

public class MyIntentService extends IntentService implements APICallback {

    private static final String TAG = MyIntentService.class.getSimpleName();

    public static final String KEY = "key";
    public static final String APP_DOWNLOAD = "app_download";
    private static final int APP_DOWNLOAD_REQUEST_CODE = 1;
    public static final String NOTIFICATION_ID = "notification_id";

    @Inject
    APIInterface apiInterface;
    @Inject
    ABDatabase abDatabase;
    @Inject
    PreferenceManger preferenceManger;

    public MyIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerMyServiceComponent.builder()
                .appComponent(AstroApplication.get(this).component())
                .build()
                .inject(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null)
            return;
        String key = intent.getAction();
        if (TextUtils.isEmpty(key))
            return;
        switch (key) {
            case APP_DOWNLOAD:
                String randomUUID = "";
                if (!TextUtils.isEmpty(preferenceManger.getStringValue(PreferenceManger.UUID))) {
                    randomUUID = preferenceManger.getStringValue(PreferenceManger.UUID);
                } else {
                    randomUUID = DeviceUtils.getRandomUUID();
                    preferenceManger.putString(PreferenceManger.UUID, randomUUID);
                }
                AppDownloadRequest request = DeviceUtils.getDeviceDataForAppDownload(this);
                request.setFcmToken(preferenceManger.getStringValue(PreferenceManger.FCM_TOKEN));
                request.setRandomUUID(randomUUID);
                AstroService.sendAppDownload(apiInterface, request, this, APP_DOWNLOAD_REQUEST_CODE);
                break;
        }
    }

    @Override
    public void onResponse(Call<?> call, Response<?> response, int requestCode, @Nullable Object request) {
        switch (requestCode) {
            case APP_DOWNLOAD_REQUEST_CODE:
                if (response.isSuccessful()) {
                    BaseResponseModel responseModel = (BaseResponseModel) response.body();
                    if (responseModel != null && !responseModel.isErrorStatus()) {
                        preferenceManger.putBoolean(PreferenceManger.APP_DOWNLOAD_API, true);
                    }
                }
                break;
        }
    }

    @Override
    public void onFailure(Call<?> call, Throwable t, int requestCode, @Nullable Object request) {
        switch (requestCode) {
            case APP_DOWNLOAD_REQUEST_CODE:
                Logger.ErrorLog(TAG, "Failed to send app download : " + t.getLocalizedMessage());
                break;
        }
    }

    @Override
    public void onNoNetwork(int requestCode) {
        switch (requestCode) {
            case APP_DOWNLOAD_REQUEST_CODE:
                break;
        }
    }
}

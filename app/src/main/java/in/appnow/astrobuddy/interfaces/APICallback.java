package in.appnow.astrobuddy.interfaces;

import androidx.annotation.Nullable;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Abhishek Thanvi on 28/03/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public interface APICallback {

    public void onResponse(Call<?> call, Response<?> response, int requestCode, @Nullable Object request);

    public void onFailure(Call<?> call, Throwable t, int requestCode, @Nullable Object request);

    public void onNoNetwork(int requestCode);
}
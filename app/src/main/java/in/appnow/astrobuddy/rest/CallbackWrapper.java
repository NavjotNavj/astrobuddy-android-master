package in.appnow.astrobuddy.rest;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.SocketTimeoutException;

import in.appnow.astrobuddy.base.BasePresenter;
import in.appnow.astrobuddy.base.BaseView;
import in.appnow.astrobuddy.rest.response.BaseResponseModel;
import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * Created by sonu on 11:50, 07/06/18
 * Copyright (c) 2018 . All rights reserved.
 */
public abstract class CallbackWrapper<T extends BaseResponseModel> extends DisposableObserver<T> {
    //BaseView is just a reference of a View in MVP
    private WeakReference<BaseView> weakReference;
    private WeakReference<BasePresenter> presenterWeakReference;

    public CallbackWrapper(BaseView view) {
        this.weakReference = new WeakReference<>(view);
    }

    public CallbackWrapper(BaseView view, BasePresenter presenter) {
        this.weakReference = new WeakReference<>(view);
        this.presenterWeakReference = new WeakReference<>(presenter);

    }

    protected abstract void onSuccess(T t);

    @Override
    public void onNext(T t) {
        //You can return StatusCodes of different cases from your API and handle it here.
        // I usually include these cases on BaseResponse and inherit it from every Response

        if (t.isLogoutUser() && presenterWeakReference != null) {
            presenterWeakReference.get().unAuthorizeUserAccess(t.getErrorMessage());

        } else {
            onSuccess(t);
        }
    }

    @Override
    public void onError(Throwable e) {
        BaseView view = weakReference.get();
        if (e instanceof HttpException) {
            ResponseBody responseBody = ((HttpException) e).response().errorBody();
            view.onUnknownError(getErrorMessage(responseBody));
        } else if (e instanceof SocketTimeoutException) {
            view.onTimeout();
        } else if (e instanceof IOException) {
            view.onNetworkError();
        } else {
            view.onUnknownError(e.getMessage());
        }
    }

    @Override
    public void onComplete() {

    }

    private String getErrorMessage(ResponseBody responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.string());
            return jsonObject.getString("message");
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}

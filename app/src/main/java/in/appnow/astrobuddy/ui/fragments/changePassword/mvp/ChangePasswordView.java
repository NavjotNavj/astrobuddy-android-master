package in.appnow.astrobuddy.ui.fragments.changePassword.mvp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;

import com.jakewharton.rxbinding2.view.RxView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseView;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.interfaces.OnRemoveFragmentListener;
import in.appnow.astrobuddy.utils.ImageUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.Observable;

@SuppressLint("ViewConstructor")
public class ChangePasswordView extends BaseViewClass implements BaseView {

    private static final String TAG = ChangePasswordView.class.getSimpleName();

    @BindView(R.id.enter_old_password)
    AppCompatEditText enterOldPassword;
    @BindView(R.id.enter_new_password)
    AppCompatEditText enterNewPassword;
    @BindView(R.id.enter_confirm_new_password)
    AppCompatEditText enterConfirmNewPassword;
    @BindView(R.id.change_password_submit_button)
    Button submitButton;
    @BindView(R.id.background_image)
    ImageView backgroundImage;

    @BindString(R.string.unknown_error)
    String unknownErrorString;

    private OnRemoveFragmentListener onRemoveFragmentListener;
    private Context context;

    public ChangePasswordView(@NonNull Context context) {
        super(context);
        this.context = context;
        inflate(getContext(), R.layout.change_password_frgament, this);
        ButterKnife.bind(this, this);
        ImageUtils.setBackgroundImage(context, backgroundImage);
    }

    public Observable<Object> observeChangeButton() {
        return RxView.clicks(submitButton);
    }


    public void setOnRemoveFragmentListener(OnRemoveFragmentListener onRemoveFragmentListener) {
        this.onRemoveFragmentListener = onRemoveFragmentListener;
    }

    public boolean validateData() {
        if (TextUtils.isEmpty(getOldPassword())) {
            ToastUtils.shortToast("Please enter old PIN.");
            return false;
        } else if (getOldPassword().length() != 4) {
            ToastUtils.shortToast("Old PIN length should be 4");
            return false;

        } else if (TextUtils.isEmpty(getNewPassword())) {
            ToastUtils.shortToast("Please enter new PIN.");
            return false;

        } else if (getNewPassword().length() != 4) {
            ToastUtils.shortToast("New PIN length should be 4");
            return false;

        } else if (TextUtils.isEmpty(getConfirmNewPassword())) {
            ToastUtils.shortToast("Please confirm new PIN again.");
            return false;

        } else if (!getConfirmNewPassword().equals(getNewPassword())) {
            ToastUtils.shortToast("New PINs did not matched. Please retry.");
            return false;
        } else {
            return true;

        }
    }

    public String getOldPassword() {
        return enterOldPassword.getText().toString().trim();
    }

    public String getNewPassword() {
        return enterNewPassword.getText().toString().trim();
    }

    public String getConfirmNewPassword() {
        return enterConfirmNewPassword.getText().toString().trim();
    }

    public void passwordChangeSuccessfully() {
        enterOldPassword.setText("");
        enterNewPassword.setText("");
        enterConfirmNewPassword.setText("");

        if (onRemoveFragmentListener != null) {
            onRemoveFragmentListener.removeFragment();
        }
    }


    @Override
    public void onUnknownError(String error) {
        ToastUtils.shortToast(error);
    }

    @Override
    public void onTimeout() {
        ToastUtils.shortToast(unknownErrorString);
    }

    @Override
    public void onNetworkError() {
        ToastUtils.shortToast(unknownErrorString);

    }

    @Override
    public boolean isNetworkConnected() {
        return false;
    }

    @Override
    public void onConnectionError() {
        ToastUtils.shortToast(unknownErrorString);

    }


}

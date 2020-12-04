package in.appnow.astrobuddy.ui.fragments.referral.mvp.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseView;
import in.appnow.astrobuddy.base.BaseViewClass;
import in.appnow.astrobuddy.ui.fragments.referral.ReferralPojo;
import in.appnow.astrobuddy.utils.AppUtils;
import in.appnow.astrobuddy.utils.ImageUtils;
import in.appnow.astrobuddy.utils.ToastUtils;
import io.reactivex.disposables.Disposable;

@SuppressLint("ViewConstructor")
public class ReferralView extends BaseViewClass implements BaseView {

    private static final String TAG = ReferralView.class.getSimpleName();

    @BindView(R.id.referral_recycler_view)
    RecyclerView referralRecyclerView;
    @BindView(R.id.background_image)
    ImageView backgroundImage;
    @BindView(R.id.lottieAnimationView)
    LottieAnimationView lottieAnimationView;
    @BindView(R.id.referral_code_label)
    TextView referralCodeLabel;
    @BindView(R.id.referral_message_label)
    TextView referral_message_label;

    @BindString(R.string.referral_email_subject)
    String emailSubject;
    @BindString(R.string.referral_share_message)
    String shareMessage;
    @BindString(R.string.waiting_dash)
    String dashString;

    @BindString(R.string.unknown_error)
    String unknownErrorString;

    private String referralCode;

    private Context context;

    private final ReferralAdapter adapter = new ReferralAdapter(getContext());

    public ReferralView(@NonNull Context context) {
        super(context);
        this.context = context;
        inflate(getContext(), R.layout.referral_fragment, this);
        ButterKnife.bind(this, this);
        ImageUtils.setBackgroundImage(context, backgroundImage);
        referralRecyclerView.setHasFixedSize(true);
        referralRecyclerView.setNestedScrollingEnabled(false);
        referralRecyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        referralRecyclerView.setAdapter(adapter);
        referral_message_label.setText(FirebaseRemoteConfig.getInstance().getString("referral_message"));

    }

    public void setReferralData(List<ReferralPojo> referralPojoList) {
        adapter.swapData(referralPojoList);
    }

    public Disposable onRecyclerClickEvent() {
        return adapter.getItemViewClickSubject().subscribe(object -> {
            if (TextUtils.isEmpty(referralCode)) {
                ToastUtils.shortToast("No referral code to share.");
                return;
            }

            if (object.getPackageName().equalsIgnoreCase("com.message")) {
                AppUtils.sendMessage(context, shareMessage);
            } else if (object.getPackageName().equalsIgnoreCase("More")) {
                AppUtils.shareData(context, object.getPackageName(), emailSubject, shareMessage);
            } else {
                AppUtils.shareData(context, object.getPackageName(), emailSubject, shareMessage);

            }
        });
    }

    public void startStopLottieAnimation(boolean isPlay) {
        //LottieTask<LottieComposition> lottieCompositionLottieTask = LottieCompositionFactory.fromUrl(context, titleUrl);
        if (isPlay)
            lottieAnimationView.playAnimation();
        else
            lottieAnimationView.pauseAnimation();
    }

    public void updateReferral(String referralCode) {
        if (!TextUtils.isEmpty(referralCode)) {
            this.referralCode = referralCode;
            shareMessage = String.format(shareMessage, referralCode);
        } else {
            referralCode = dashString;
        }
        referralCodeLabel.setText(referralCode);
    }

    @Override
    public void onUnknownError(String error) {
        updateReferral("");
    }

    @Override
    public void onTimeout() {
        updateReferral("");
    }

    @Override
    public void onNetworkError() {
        updateReferral("");
    }

    @Override
    public boolean isNetworkConnected() {
        return false;
    }

    @Override
    public void onConnectionError() {
        updateReferral("");
    }


}

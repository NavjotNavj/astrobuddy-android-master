package in.appnow.astrobuddy.ui.fragments.call_plans.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import de.hdodenhof.circleimageview.CircleImageView;
import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.custom_views.RatedView;
import in.appnow.astrobuddy.rest.response.CallPlan;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.ImageUtils;


public class KnowMoreDialogFragment extends DialogFragment {
    public static final String TAG = KnowMoreDialogFragment.class.getSimpleName();

    private CallPlan callPlan;

    // Note: only the system can call this constructor by reflection.
    public KnowMoreDialogFragment() {
    }

    public static KnowMoreDialogFragment getInstance(CallPlan callPlan) {
        KnowMoreDialogFragment fragment = new KnowMoreDialogFragment();
        fragment.callPlan = callPlan;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.know_more_dialog, null, false);

        CircleImageView userImage = view.findViewById(R.id.user_image);
        ImageView cancelImage = view.findViewById(R.id.button_cancel);
        TextView userName = view.findViewById(R.id.user_name);
        TextView textDescription = view.findViewById(R.id.user_desc);
        TextView textExperience = view.findViewById(R.id.user_experience);
        TextView textLanguage = view.findViewById(R.id.user_language);
        RatedView ratedView = view.findViewById(R.id.rated_view);

        ImageUtils.loadImageUrl(getContext(), userImage, R.drawable.icon_default_profile, callPlan.getImageFile());

        userName.setText(callPlan.getName());

        textDescription.setText(callPlan.getDesc());

        ratedView.setRatedStar((int) callPlan.getRating());

        textExperience.setText(getContext().getResources().
                getString(R.string.text_experience, callPlan.getExperience()));

        textLanguage.setText(getContext().getResources().
                getString(R.string.text_language, callPlan.getLanguage())
                .replace(", ", "\n"));

        cancelImage.setOnClickListener(v -> dismissDialog(((AppCompatActivity) getContext()).getSupportFragmentManager()));

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public static void showDialog(FragmentManager fragmentManager, CallPlan callPlan) {
        if (fragmentManager == null) return;

        KnowMoreDialogFragment dialogFragment = (KnowMoreDialogFragment)
                fragmentManager.findFragmentByTag(FragmentUtils.KNOW_MORE_DIALOG_FRAGMENT);

        if (dialogFragment == null) {
            fragmentManager.beginTransaction().add(
                    KnowMoreDialogFragment.getInstance(callPlan),
                    FragmentUtils.KNOW_MORE_DIALOG_FRAGMENT).commitAllowingStateLoss();
        }
    }

    public static void dismissDialog(FragmentManager fragmentManager) {
        if (fragmentManager == null) return;

        KnowMoreDialogFragment progressDialogFragment = (KnowMoreDialogFragment)
                fragmentManager.findFragmentByTag(FragmentUtils.KNOW_MORE_DIALOG_FRAGMENT);

        if (progressDialogFragment != null) {
            fragmentManager.beginTransaction()
                    .remove(progressDialogFragment)
                    .commitAllowingStateLoss();
        }
    }
}

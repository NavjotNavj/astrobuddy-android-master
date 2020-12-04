package in.appnow.astrobuddy.ui.fragments.call_plans.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.custom_views.SegmentedView;
import in.appnow.astrobuddy.rest.response.CallPlan;
import in.appnow.astrobuddy.ui.fragments.call_plans.OnCallPlanFlowLaunch;
import in.appnow.astrobuddy.utils.FragmentUtils;
import in.appnow.astrobuddy.utils.KeyboardUtils;
import in.appnow.astrobuddy.utils.ToastUtils;

public class BuyPlanDialogFragment extends DialogFragment {
    public static final String TAG = BuyPlanDialogFragment.class.getSimpleName();

    private CallPlan callPlan;
    private EditText mobileNumberET;
    private ImageView editNumberButton;

    private OnCallPlanFlowLaunch onCallPlanFlowLaunch;

    // Note: only the system can call this constructor by reflection.
    public BuyPlanDialogFragment(OnCallPlanFlowLaunch onCallPlanFlowLaunch) {
        this.onCallPlanFlowLaunch = onCallPlanFlowLaunch;
    }

    public static BuyPlanDialogFragment getInstance(CallPlan callPlan,
                                                    OnCallPlanFlowLaunch onCallPlanFlowLaunch) {
        BuyPlanDialogFragment fragment = new BuyPlanDialogFragment(onCallPlanFlowLaunch);
        fragment.callPlan = callPlan;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.buy_call_plan_dialog, null, false);

        ImageView cancelImage = view.findViewById(R.id.button_cancel);
        TextView userName = view.findViewById(R.id.user_name);
        TextView textDescription = view.findViewById(R.id.plan_desc);
        TextView unavailableLable = view.findViewById(R.id.unavailable_label);
        mobileNumberET = view.findViewById(R.id.et_number);
        TextView countryCodeLabel = view.findViewById(R.id.country_code_label);
        editNumberButton = view.findViewById(R.id.number_edit_button);
        Button buyButton = view.findViewById(R.id.buy_button);
        SegmentedView segmentedView = view.findViewById(R.id.segment_view);

        userName.setText(callPlan.getName());

        mobileNumberET.setText(callPlan.getMobileNumber());

        countryCodeLabel.setText(!callPlan.getCountryCode().contains("+") ?
                String.format("+%s", callPlan.getCountryCode()) :
                callPlan.getCountryCode());

        //unavailableLable.setVisibility(callPlan.getAvailable() == 1 ? View.GONE : View.VISIBLE);

        try {

            String[] keys = new String[callPlan.getCallPlanTypes().size()];
            String[] values = new String[callPlan.getCallPlanTypes().size()];

            for (int i = 0; i < callPlan.getCallPlanTypes().size(); i++) {
                keys[i] = callPlan.getCallPlanTypes().get(i).getDuration() + " Min";
                values[i] = String.valueOf(callPlan.getCallPlanTypes().get(i).getPrice());
            }

            segmentedView.setKeysWithValue(keys, values);

            segmentedView.setOnSelectionChangedListener((id, selectedItem, selectedValue) -> {

                        String desc = String.format(
                                getResources().getString(R.string.text_choose_plan_desc),
                                selectedItem, selectedValue);

                        textDescription.setText(desc);
                    }
            );

            segmentedView.setSelection(0);

        } catch (Exception e) {
            e.printStackTrace();
        }

        buyButton.setOnClickListener(v -> {

            if (!validateNumber(mobileNumberET.getText().toString())) {
                return;
            }

            callPlan.setMobileNumber(mobileNumberET.getText().toString());
            if (onCallPlanFlowLaunch != null) {
                onCallPlanFlowLaunch.OnCallPlanSelected(
                        segmentedView.getCurrentSelectedKey(),
                        segmentedView.getCurrentSelectedValue(),
                        callPlan);
            }

            KeyboardUtils.hideSoftKeyboard(getActivity());
            dismissDialog(((AppCompatActivity) getContext()).getSupportFragmentManager());
        });

        editNumberButton.setOnClickListener(v -> enableDisableEditText(!mobileNumberET.isEnabled()));

        cancelImage.setOnClickListener(v -> {
            KeyboardUtils.hideSoftKeyboard(getActivity());
            dismissDialog(((AppCompatActivity) getContext()).getSupportFragmentManager());
        });

        return view;
    }

    private boolean validateNumber(String number) {
        if (TextUtils.isEmpty(number)) {
            ToastUtils.shortToast("Please enter mobile number..");
            return false;

        } else if (number.length() != 10) {
            ToastUtils.shortToast("Please enter valid mobile number.");
            return false;
        }

        return true;
    }

    public void enableDisableEditText(boolean enable) {
        mobileNumberET.setEnabled(enable);
        if (enable) {
            editNumberButton.setImageResource(R.drawable.ic_done_black_24dp);
        } else {
            editNumberButton.setImageResource(R.drawable.ic_edit_black_24dp);
        }
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

    public static void showDialog(FragmentManager fragmentManager, CallPlan callPlan,
                                  OnCallPlanFlowLaunch onCallPlanFlowLaunch) {
        if (fragmentManager == null) return;

        BuyPlanDialogFragment dialogFragment = (BuyPlanDialogFragment)
                fragmentManager.findFragmentByTag(FragmentUtils.BUY_PLAN_DIALOG_FRAGMENT);

        if (dialogFragment == null) {
            fragmentManager.beginTransaction().add(
                    BuyPlanDialogFragment.getInstance(callPlan, onCallPlanFlowLaunch),
                    FragmentUtils.BUY_PLAN_DIALOG_FRAGMENT).commitAllowingStateLoss();
        }
    }

    public static void dismissDialog(FragmentManager fragmentManager) {
        if (fragmentManager == null) return;

        BuyPlanDialogFragment progressDialogFragment = (BuyPlanDialogFragment)
                fragmentManager.findFragmentByTag(FragmentUtils.BUY_PLAN_DIALOG_FRAGMENT);

        if (progressDialogFragment != null) {
            fragmentManager.beginTransaction()
                    .remove(progressDialogFragment)
                    .commitAllowingStateLoss();
        }
    }
}

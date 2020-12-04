package in.appnow.astrobuddy.ui.fragments.call_plans.call_plan_history;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import javax.inject.Inject;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.base.BaseFragment;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.ui.activities.home.HomeActivity;
import in.appnow.astrobuddy.ui.fragments.call_plans.call_plan_history.mvp.CallPlansHistoryModel;
import in.appnow.astrobuddy.ui.fragments.call_plans.call_plan_history.mvp.CallPlansHistoryPresenter;
import in.appnow.astrobuddy.ui.fragments.call_plans.call_plan_history.mvp.view.CallPlansHistoryView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by NILESH BHARODIYA on 03-09-2019.
 */
public class CallPlansHistoryFragment extends BaseFragment {

    public static final int REQUEST_CODE = 123;

    private CallPlansHistoryView view;
    private CallPlansHistoryPresenter presenter;
    private CallPlansHistoryModel model;

    @Inject
    APIInterface apiInterface;

    @Inject
    PreferenceManger preferenceManger;

    @Inject
    ABDatabase abDatabase;

    public CallPlansHistoryFragment() {
    }

    public static CallPlansHistoryFragment newInstance() {
        return new CallPlansHistoryFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            updateToolbarTitle(context.getResources().getString(R.string.action_call_plan_history));
            showHideToolbar(true);
            showHideBackButton(true);
            hideBottomBar(false);
        } catch (Exception ignored) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ((HomeActivity) getActivity()).getComponent().inject(this);
        view = new CallPlansHistoryView(getActivity());
        model = new CallPlansHistoryModel((AppCompatActivity) getContext(), apiInterface);
        presenter = new CallPlansHistoryPresenter(view, model, preferenceManger, abDatabase);

        presenter.onCreate();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                forceBackPress();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }
}

package in.appnow.astrobuddy.ui.fragments.call_plans;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import javax.inject.Inject;

import in.appnow.astrobuddy.R;
import in.appnow.astrobuddy.app.AstroApplication;
import in.appnow.astrobuddy.base.BaseFragment;
import in.appnow.astrobuddy.dao.ABDatabase;
import in.appnow.astrobuddy.helper.PreferenceManger;
import in.appnow.astrobuddy.interfaces.OnPaymentListener;
import in.appnow.astrobuddy.rest.APIInterface;
import in.appnow.astrobuddy.ui.activities.home.HomeActivity;
import in.appnow.astrobuddy.ui.fragments.call_plans.mvp.CallPlansModel;
import in.appnow.astrobuddy.ui.fragments.call_plans.mvp.CallPlansPresenter;
import in.appnow.astrobuddy.ui.fragments.call_plans.mvp.view.CallPlansView;
import in.appnow.astrobuddy.utils.Logger;
import it.sephiroth.android.library.tooltip.Tooltip;

import static android.app.Activity.RESULT_OK;


/**
 * Created by NILESH BHARODIYA on 28-08-2019.
 */
public class CallPlansFragment extends BaseFragment implements OnPaymentListener {
    public static final String TAG = "CallPlansFragment";

    private CallPlansView view;
    private CallPlansPresenter presenter;
    private CallPlansModel model;
    private Tooltip.TooltipView tooltip;
    private MenuItem callHistoryItem;
    Context context;


    @Inject
    APIInterface apiInterface;

    @Inject
    ABDatabase abDatabase;

    @Inject
    PreferenceManger preferenceManger;

    public CallPlansFragment() {
    }

    public static CallPlansFragment newInstance() {
        return new CallPlansFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

        try {
            updateToolbarTitle(context.getResources().getString(R.string.action_astro_buddy));
            showHideToolbar(true);
            showHideBackButton(true);
            hideBottomBar(false);
        } catch (Exception ignored) {

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.call_plan_history_menu, menu);

//        callHistoryItem = menu.findItem(R.id.action_call_plan_history);
//        callHistoryItem.setActionView(R.layout.custom_menu_item);
//        callHistoryItem.getActionView().setPadding(0,0,0, (int) getResources().getDimension(R.dimen._10sdp));
//        ImageView history = callHistoryItem.getActionView().findViewById(R.id.history_image_view);
//        history.setOnClickListener(view -> {
//            if (AstroApplication.getInstance().isInternetConnected(true)) {
//                presenter.replaceHistoryFragment();
//            }
//        });
//        showCallHistoryToolTip();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_call_plan_history:
                if (AstroApplication.getInstance().isInternetConnected(true)) {
                    presenter.replaceHistoryFragment();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        Toolbar scToolbar = ((HomeActivity) getActivity()).getToolbar();

        scToolbar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                scToolbar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                View menuItem = scToolbar.findViewById(R.id.action_call_plan_history);
                showCallHistoryToolTip(menuItem);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ((HomeActivity) getActivity()).getComponent().inject(this);
        view = new CallPlansView(getActivity());
        model = new CallPlansModel((AppCompatActivity) getContext(), apiInterface);
        presenter = new CallPlansPresenter(view, model, preferenceManger, abDatabase);

        presenter.onCreate();
        //checkIfBackPromptIsShown();
       // showCallHistoryToolTip();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }

    private void checkIfBackPromptIsShown() {
        if (presenter != null) {
            PreferenceManger preferenceManger = presenter.getPreferenceManger();
            if (preferenceManger != null) {
                if (preferenceManger.getBooleanValue(PreferenceManger.LIVE_CALL_HINT) && !preferenceManger.getBooleanValue(PreferenceManger.LIVE_CALL_BACK_PRESS_HINT)) {
                    showToolbarBackPrompt(R.drawable.ic_menu);
                    preferenceManger.putBoolean(PreferenceManger.LIVE_CALL_BACK_PRESS_HINT, true);
                }
            }
        }
    }


        private void showCallHistoryToolTip(View menu) {
        tooltip = Tooltip.make(context,
                new Tooltip.Builder(109)
                        .anchor(menu, Tooltip.Gravity.BOTTOM)
                        .closePolicy(new Tooltip.ClosePolicy()
                                .insidePolicy(true, false)
                                .outsidePolicy(false, false), 5000)
                        .activateDelay(800)
                        .withStyleId(R.style.ToolTipLayoutCustomLightStyle)
                        .showDelay(300)
                        .text("Call History")
                        .maxWidth(500)
                        .withArrow(true)
                        .withOverlay(true).build());
        tooltip.show();

        //.typeface(mYourCustomFont)
        //.floatingAnimation(Tooltip.AnimationBuilder.DEFAULT)

    }

    // Payment

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CallPlansModel.RAZOR_PAY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                forceBackPress();
            }
        }
    }

    @Override
    public void onPaymentCompleted(String paymentId) {
        Logger.DebugLog(TAG, "Payment Id : " + paymentId);
        if (presenter != null)
            presenter.onPaymentDone(1, paymentId, "success");
    }

    @Override
    public void onPaymentFailed(int code, String response) {
        Logger.ErrorLog(TAG, "Payment Failed : " + response);
        if (presenter != null)
            presenter.onPaymentDone(code, "", response);
    }
}

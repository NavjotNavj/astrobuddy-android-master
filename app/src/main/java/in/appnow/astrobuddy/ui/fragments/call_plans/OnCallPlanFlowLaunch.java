package in.appnow.astrobuddy.ui.fragments.call_plans;

import in.appnow.astrobuddy.rest.response.CallPlan;

/**
 * Created by NILESH BHARODIYA on 03-09-2019.
 */
public interface OnCallPlanFlowLaunch {

    void OnCallPlanSelected(String callMinutes, String price, CallPlan callPlan);

}

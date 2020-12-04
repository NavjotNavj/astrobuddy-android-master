package in.appnow.astrobuddy.rest.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by NILESH BHARODIYA on 28-08-2019.
 */
public class CallPlansResponse extends BaseResponseModel {

    @SerializedName("call_plans")
    private List<CallPlan> callPlan;

    public List<CallPlan> getCallPlan() {
        return callPlan;
    }

    @Override
    public String toString() {
        return "CallPlansResponse{" +
                "callPlan=" + callPlan +
                '}';
    }
}

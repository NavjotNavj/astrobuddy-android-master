package in.appnow.astrobuddy.rest.response;

import com.google.gson.annotations.SerializedName;

public class UserPaymentCheckResponse extends BaseResponseModel{

	@SerializedName("response")
	private CheckUserPaymentInfo checkUserPaymentInfo;

	public void setCheckUserPaymentInfo(CheckUserPaymentInfo checkUserPaymentInfo){
		this.checkUserPaymentInfo = checkUserPaymentInfo;
	}

	public CheckUserPaymentInfo getCheckUserPaymentInfo(){
		return checkUserPaymentInfo;
	}


}
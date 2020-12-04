package in.appnow.astrobuddy.rest.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import in.appnow.astrobuddy.rest.response.BaseResponseModel;

public class OTPResponseModel extends BaseResponseModel implements Serializable{


	@SerializedName("user_match")
	private boolean userMatch;

	@SerializedName("sms_sent_status")
	private String smsSentStatus;


	@SerializedName("otp_db_insert")
	private boolean otpDbInsert;


	public boolean isUserMatch() {
		return userMatch;
	}

	public void setUserMatch(boolean userMatch) {
		this.userMatch = userMatch;
	}

	public void setSmsSentStatus(String smsSentStatus){
		this.smsSentStatus = smsSentStatus;
	}

	public String getSmsSentStatus(){
		return smsSentStatus;
	}


	public void setOtpDbInsert(boolean otpDbInsert){
		this.otpDbInsert = otpDbInsert;
	}

	public boolean isOtpDbInsert(){
		return otpDbInsert;
	}

	@Override
 	public String toString(){
		return 
			"OTPResponseModel{" + 
			",sms_sent_status = '" + smsSentStatus + '\'' +
			",otp_db_insert = '" + otpDbInsert + '\'' +
			"}";
		}
}
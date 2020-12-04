package in.appnow.astrobuddy.rest.response;

import com.google.gson.annotations.SerializedName;

import in.appnow.astrobuddy.rest.response.BaseResponseModel;

public class ForgotPasswordResponse extends BaseResponseModel{

	@SerializedName("sms_sent_status")
	private String smsSentStatus;

	@SerializedName("email_sent")
	private String eMailSent;

	@SerializedName("user_exists")
	private boolean userExist;

	public void setSmsSentStatus(String smsSentStatus){
		this.smsSentStatus = smsSentStatus;
	}

	public String getSmsSentStatus(){
		return smsSentStatus;
	}

	public String geteMailSent() {
		return eMailSent;
	}

	public void setUserExist(boolean userExsist){
		this.userExist = userExsist;
	}

	public boolean isUserExist(){
		return userExist;
	}

	@Override
	public String toString() {
		return "ForgotPasswordResponse{" +
				"smsSentStatus='" + smsSentStatus + '\'' +
				", eMailSent='" + eMailSent + '\'' +
				", userExist=" + userExist +
				'}';
	}
}
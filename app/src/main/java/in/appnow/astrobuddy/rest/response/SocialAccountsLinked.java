package in.appnow.astrobuddy.rest.response;

import com.google.gson.annotations.SerializedName;

public class SocialAccountsLinked {

	@SerializedName("social_id")
	private String socialId;

	@SerializedName("social_media_type_id")
	private String socialMediaTypeId;

	@SerializedName("user_id")
	private String userId;

	public void setSocialId(String socialId){
		this.socialId = socialId;
	}

	public String getSocialId(){
		return socialId;
	}

	public void setSocialMediaTypeId(String socialMediaTypeId){
		this.socialMediaTypeId = socialMediaTypeId;
	}

	public String getSocialMediaTypeId(){
		return socialMediaTypeId;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	@Override
 	public String toString(){
		return 
			"SocialAccountsLinked{" +
			"social_id = '" + socialId + '\'' + 
			",social_media_type_id = '" + socialMediaTypeId + '\'' + 
			",user_id = '" + userId + '\'' + 
			"}";
		}
}
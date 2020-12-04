package in.appnow.astrobuddy.rest.response;

import com.google.gson.annotations.SerializedName;

public class CallConversationHistory{

	@SerializedName("end_date")
	private String endDate;

	@SerializedName("purchased_plan_id")
	private String purchasedPlanId;

	@SerializedName("fdbk_rating")
	private String fdbkRating;

	@SerializedName("amount")
	private String amount;

	@SerializedName("comments")
	private String comments;

	@SerializedName("call_minutes")
	private String callMinutes;

	@SerializedName("name")
	private String name;

	@SerializedName("img_file")
	private String imgFile;

	@SerializedName("description")
	private String description;

	@SerializedName("start_date")
	private String startDate;

	@SerializedName("used_date")
	private String usedDate;

	@SerializedName("status")
	private String status;

	@SerializedName("trans_id")
	private String transactionId;

	public void setEndDate(String endDate){
		this.endDate = endDate;
	}

	public String getEndDate(){
		return endDate;
	}

	public void setFdbkRating(String fdbkRating){
		this.fdbkRating = fdbkRating;
	}

	public String getFdbkRating(){
		return fdbkRating;
	}

	public void setAmount(String amount){
		this.amount = amount;
	}

	public String getAmount(){
		return amount;
	}

	public void setComments(String comments){
		this.comments = comments;
	}

	public String getComments(){
		return comments;
	}

	public void setCallMinutes(String callMinutes){
		this.callMinutes = callMinutes;
	}

	public String getCallMinutes(){
		return callMinutes;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setImgFile(String imgFile){
		this.imgFile = imgFile;
	}

	public String getImgFile(){
		return imgFile;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setStartDate(String startDate){
		this.startDate = startDate;
	}

	public String getStartDate(){
		return startDate;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public String getUsedDate() {
		return usedDate;
	}

	public void setPurchasedPlanId(String purchasedPlanId) {
		this.purchasedPlanId = purchasedPlanId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public void setUsedDate(String usedDate) {
		this.usedDate = usedDate;
	}

	public String getPurchasedPlanId() {
		return purchasedPlanId;
	}

	@Override
 	public String toString(){
		return 
			"CallConversationHistoryItem{" + 
			"end_date = '" + endDate + '\'' + 
			",fdbk_rating = '" + fdbkRating + '\'' + 
			",amount = '" + amount + '\'' + 
			",comments = '" + comments + '\'' + 
			",call_minutes = '" + callMinutes + '\'' + 
			",name = '" + name + '\'' + 
			",img_file = '" + imgFile + '\'' + 
			",description = '" + description + '\'' + 
			",start_date = '" + startDate + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}
package in.appnow.astrobuddy.rest.response;

import com.google.gson.annotations.SerializedName;

public class CheckUserPaymentInfo{

	@SerializedName("hostname")
	private String hostname;

	@SerializedName("clientName")
	private String clientName;

	@SerializedName("rate")
	private String rate;

	@SerializedName("topic_quan")
	private String topicQuan;

	@SerializedName("name")
	private String name;

	@SerializedName("clientStatus")
	private int clientStatus;

	@SerializedName("dynamic_price")
	private String dynamicPrice;

	@SerializedName("curr")
	private String curr;

	@SerializedName("locale")
	private String locale;

	@SerializedName("type")
	private String type;

	@SerializedName("email")
	private String email;

	public void setHostname(String hostname){
		this.hostname = hostname;
	}

	public String getHostname(){
		return hostname;
	}

	public void setClientName(String clientName){
		this.clientName = clientName;
	}

	public String getClientName(){
		return clientName;
	}


	public String getRate(){
		return rate;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setClientStatus(int clientStatus){
		this.clientStatus = clientStatus;
	}

	public int getClientStatus(){
		return clientStatus;
	}



	public void setCurr(String curr){
		this.curr = curr;
	}

	public String getCurr(){
		return curr;
	}

	public void setLocale(String locale){
		this.locale = locale;
	}

	public String getLocale(){
		return locale;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getTopicQuan() {
		return topicQuan;
	}

	public void setTopicQuan(String topicQuan) {
		this.topicQuan = topicQuan;
	}

	public String getDynamicPrice() {
		return dynamicPrice;
	}

	public void setDynamicPrice(String dynamicPrice) {
		this.dynamicPrice = dynamicPrice;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	@Override
 	public String toString(){
		return 
			"CheckUserPaymentInfo{" + 
			"hostname = '" + hostname + '\'' + 
			",clientName = '" + clientName + '\'' + 
			",rate = '" + rate + '\'' + 
			",topic_quan = '" + topicQuan + '\'' + 
			",name = '" + name + '\'' + 
			",clientStatus = '" + clientStatus + '\'' + 
			",dynamic_price = '" + dynamicPrice + '\'' + 
			",curr = '" + curr + '\'' + 
			",locale = '" + locale + '\'' + 
			",type = '" + type + '\'' + 
			",email = '" + email + '\'' + 
			"}";
		}
}
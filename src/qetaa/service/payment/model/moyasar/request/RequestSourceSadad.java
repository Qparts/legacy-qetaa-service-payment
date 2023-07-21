package qetaa.service.payment.model.moyasar.request;

public class RequestSourceSadad {

	private String type;//sadad
	private String username;//sadad username
	private String successUrl;//successurl
	private String failUrl;//fail url
	public String getType() {
		return type;
	}
	public String getUsername() {
		return username;
	}
	public String getSuccessUrl() {
		return successUrl;
	}
	public String getFailUrl() {
		return failUrl;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}
	public void setFailUrl(String failUrl) {
		this.failUrl = failUrl;
	}
	
	
}

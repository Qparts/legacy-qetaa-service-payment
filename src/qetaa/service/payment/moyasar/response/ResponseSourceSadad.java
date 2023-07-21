package qetaa.service.payment.moyasar.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseSourceSadad {
	
	
	private String type;//sadad
	private String username;//sadad username
	@JsonProperty("error_code")
	private String errorCode;//error code from sadad if failure happen.
	private String message;//message??
	@JsonProperty("transaction_id")
	private String transactionId;//Sadad Transaction ID.
	@JsonProperty("transaction_url")
	private String transactionUrl;//URL given from sadad to continue the payment.
	public String getType() {
		return type;
	}
	public String getUsername() {
		return username;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public String getMessage() {
		return message;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public String getTransactionUrl() {
		return transactionUrl;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public void setTransactionUrl(String transactionUrl) {
		this.transactionUrl = transactionUrl;
	}
}

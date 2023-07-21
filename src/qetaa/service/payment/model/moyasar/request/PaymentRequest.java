package qetaa.service.payment.model.moyasar.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;

@JsonSubTypes({
    @JsonSubTypes.Type(value = PaymentRequestSadad.class, name = "source"),
    @JsonSubTypes.Type(value = PaymentRequestCC.class, name = "source")
})
public class PaymentRequest { 
	
	@JsonProperty("amount")
	private int amount;
	@JsonProperty("currency")
	private String currency;
	@JsonProperty("description")
	private String description;
	@JsonProperty("callback_url")
	private String callbackUrl;

	public int getAmount() {
		return amount;
	}

	public String getCurrency() {
		return currency;
	}

	public String getDescription() {
		return description;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}


	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

}

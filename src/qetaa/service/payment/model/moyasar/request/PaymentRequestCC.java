package qetaa.service.payment.model.moyasar.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import qetaa.service.payment.model.moyasar.request.PaymentRequest;

public class PaymentRequestCC extends PaymentRequest{
	
	@JsonProperty("source")
	private RequestSourceCC source;
	
	public PaymentRequestCC(){
		super();
	}
	
	public RequestSourceCC getSource() {
		return source;
	}

	public void setSource(RequestSourceCC source) {
		this.source = source;
	}
	
	
	
}

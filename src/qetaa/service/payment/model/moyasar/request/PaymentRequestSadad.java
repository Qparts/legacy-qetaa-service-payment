package qetaa.service.payment.model.moyasar.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import qetaa.service.payment.model.moyasar.request.PaymentRequest;

public class PaymentRequestSadad extends PaymentRequest{
	
	public PaymentRequestSadad(){
		super();
	}
	
	@JsonProperty("source")
	private RequestSourceSadad source;

	public RequestSourceSadad getSource() {
		return source;
	}

	public void setSource(RequestSourceSadad source) {
		this.source = source;
	}

}

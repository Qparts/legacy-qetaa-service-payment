package qetaa.service.payment.moyasar.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentResponseSadad extends PaymentResponse{
	
	public PaymentResponseSadad(){
		super();
	}
	
	@JsonProperty("source")
	private ResponseSourceSadad source;

	public ResponseSourceSadad getSource() {
		return source;
	}

	public void setSource(ResponseSourceSadad source) {
		this.source = source;
	}

}

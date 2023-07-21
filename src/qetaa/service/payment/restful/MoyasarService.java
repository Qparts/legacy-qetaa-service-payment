package qetaa.service.payment.restful;

import java.io.IOException;
import java.util.Formatter;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.security.Base64Encoder;

import qetaa.service.payment.dao.DAO;
import qetaa.service.payment.filters.LoggingFilter;
import qetaa.service.payment.filters.SecuredCustomer;
import qetaa.service.payment.helpers.AppConstants;
import qetaa.service.payment.helpers.Helper;
import qetaa.service.payment.model.LocalPayment;
import qetaa.service.payment.model.moyasar.request.PaymentRequest;
import qetaa.service.payment.model.moyasar.request.PaymentRequestCC;
import qetaa.service.payment.model.moyasar.request.PaymentRequestSadad;
import qetaa.service.payment.model.moyasar.request.RequestSourceCC;
import qetaa.service.payment.model.moyasar.request.RequestSourceSadad;
import qetaa.service.payment.moyasar.response.PaymentResponseCC;
import qetaa.service.payment.moyasar.response.PaymentResponseSadad;

@Path("/moyasar")
public class MoyasarService {

	@EJB
	private DAO dao;
	 

	@SecuredCustomer
	@POST
	@Path("/payment-request")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response startCreditCardPayment(LocalPayment payment) {
		try {
			PaymentRequest preq = null;
			if (payment.getType() == 'S') {
				PaymentRequestSadad sdd = new PaymentRequestSadad();
				sdd.setSource(initSadad(payment));
				preq = sdd;
			} else if (payment.getType() == 'C') {
				PaymentRequestCC cc = new PaymentRequestCC();
				cc.setSource(initCC(payment));
				preq = cc;
			}
			preq.setAmount(Helper.paymentIntegerFormat(payment.getAmount()));
			preq.setCallbackUrl(payment.getCallback());
			preq.setCurrency("SAR");
			preq.setDescription("Parts Order # " + payment.getCartId());
			Response r = this.postSecuredRequest(AppConstants.MOYASAR_API_URL, preq);
			if (r.getStatus() == 200 || r.getStatus() == 201) {
				if (payment.getType() == 'C') {
					// read cc object
					PaymentResponseCC ccr = r.readEntity(PaymentResponseCC.class); 
					return Response.status(200).entity(ccr).build();
				} else if (payment.getType() == 'S') {
					PaymentResponseSadad sadad = r.readEntity(PaymentResponseSadad.class);
					return Response.status(200).entity(sadad).build();
					// read sadad object
				} else {
					return Response.status(406).build();// error from server
				}

			} else {
				r.bufferEntity();
				return Response.status(406).build();// error from server
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(500).build();
		}
	}

	@GET
	@Path("/test-payment-sadad")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void startSadadPaymentTest() {
		PaymentRequest preq = null;
		PaymentRequestSadad cc = new PaymentRequestSadad();
		cc.setSource(new RequestSourceSadad());
		cc.getSource().setSuccessUrl("https://www.qetaa.com/");
		cc.getSource().setFailUrl("https://www.qetaa.com/");
		cc.getSource().setType("sadad");
		cc.getSource().setUsername("sadad35");
		preq = cc;
		preq.setAmount(Helper.paymentIntegerFormat(300.0));
		preq.setCallbackUrl("https://www.qetaa.com");
		preq.setCurrency("SAR");
		preq.setDescription("desc");
		this.postSecuredRequest(AppConstants.MOYASAR_API_URL, preq);
		// returns null if account is not valid

	}

	private RequestSourceSadad initSadad(LocalPayment lp) {
		RequestSourceSadad sadad = new RequestSourceSadad();
		sadad.setFailUrl("www.qetaa.com/parts_order");
		sadad.setSuccessUrl("www.qetaa.com/parts_order");
		sadad.setType("sadad");
		sadad.setUsername(lp.getName());
		return sadad;
	}

	private RequestSourceCC initCC(LocalPayment lp) { 
		RequestSourceCC cc = new RequestSourceCC();
		cc.setCvc(this.getCVC(lp.getCvc()));
		cc.setMonth(lp.getMonth());
		cc.setYear(lp.getYear());
		cc.setType("creditcard");
		cc.setName(lp.getName());
		cc.setNumber(lp.getNumber());
		return cc;
	}
	
	private String getCVC(Integer number) {
		StringBuilder sb = new StringBuilder();
		Formatter f = new Formatter(sb);
		f.format("%03d", number);
		f.close();
		return sb.toString();
	}

	private <T> Response postSecuredRequest(String link, T t) {
		Builder b = ClientBuilder.newClient().target(link).register(LoggingFilter.class).request();
		b.header(HttpHeaders.AUTHORIZATION, "Basic " + getSecurityHeader());
		Response r = b.post(Entity.entity(t, "application/json"));
		return r;
	}

	private String getSecurityHeader() {
		try {
			return Base64Encoder.encode(AppConstants.MOYASAR_LIVE_PUBLISHABLE_KEY);
		} catch (IOException e) {
			return "";
		}
	}

}

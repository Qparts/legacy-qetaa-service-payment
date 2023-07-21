package qetaa.service.payment.restful;

import java.util.Map;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import qetaa.service.payment.dao.DAO;
import qetaa.service.payment.helpers.AppConstants;
import qetaa.service.payment.model.customer.contract.LoyaltyPointsCreation;

@Stateless
public class AsyncService {

	@EJB
	private DAO dao;

	
	@Asynchronous
	public void confirmWireTransfer(String authHeader, Map<String,Object> map) {
		Response r = putSecuredRequest(AppConstants.PUT_CONFIRM_WIRE_TRANSFER, map, authHeader);
		System.out.println(r.getStatus());
	}

	@Asynchronous
	public void createLoyaltyPoints(String authHeader, long cartId, long customerId, double amount){
		LoyaltyPointsCreation lpc = new LoyaltyPointsCreation();
		lpc.setCartId(cartId);
		lpc.setCustomerId(customerId); 
		lpc.setAmount(amount);
		Response r = this.postSecuredRequest(AppConstants.POST_LOYALTY_POINTS, lpc, authHeader);
		System.out.println(r.getStatus());
	}
	
	public <T> Response postSecuredRequest(String link, T t, String authHeader) {
		Builder b = ClientBuilder.newClient().target(link).request();
		b.header(HttpHeaders.AUTHORIZATION, authHeader);
		Response r = b.post(Entity.entity(t, "application/json"));// not secured
		return r;
	}
	
	public <T> Response putSecuredRequest(String link, T t, String authHeader) {
		Builder b = ClientBuilder.newClient().target(link).request();
		b.header(HttpHeaders.AUTHORIZATION, authHeader);
		Response r = b.put(Entity.entity(t, "application/json"));// not secured
		return r;
	}
	
	
}

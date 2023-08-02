package qetaa.service.payment.restful;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import qetaa.service.payment.model.WalletItem;
import qetaa.service.payment.model.contract.Courier;
import qetaa.service.payment.model.customer.contract.LoyaltyPointsCreation;
import qetaa.service.payment.model.shipment.Shipment;
import qetaa.service.payment.model.shipment.ShipmentItem;

@Stateless
public class AsyncService {

	@EJB
	private DAO dao;

	
	@Asynchronous
	public void confirmWireTransfer(String authHeader, Map<String,Object> map) {
		Response r = putSecuredRequest(AppConstants.PUT_CONFIRM_WIRE_TRANSFER, map, authHeader);
	}
	
	@Asynchronous
	public void refundCart(String authHeader, Long cartId) {
		Response r = putSecuredRequest(AppConstants.PUT_REFUND_CART, cartId, authHeader);
	}

	@Asynchronous
	public void createLoyaltyPoints(String authHeader, long cartId, long customerId, double amount){
		LoyaltyPointsCreation lpc = new LoyaltyPointsCreation();
		lpc.setCartId(cartId);
		lpc.setCustomerId(customerId); 
		lpc.setAmount(amount);
		Response r = this.postSecuredRequest(AppConstants.POST_LOYALTY_POINTS, lpc, authHeader);
	}
	
	


	private Courier getCourier(int id, String authHeader) {
		Response r = getSecuredRequest(AppConstants.getCourier(id), authHeader);
		Courier c = new Courier();
		if(r.getStatus() == 200) {
			c = r.readEntity(Courier.class);
		}
		return c;
	}
	

	private String getCartNumbers(List<ShipmentItem> items) {
		String t = "";
		Set<Long> set = new HashSet<Long>();
		for(ShipmentItem shi : items) {
			WalletItem wi = dao.find(WalletItem.class, shi.getWalletItemId());
			set.add(wi.getCartId());
		}
		for(Long lon : set) {
			t = lon + "/";
		}
		if(set.size() == 1) {
			t.replace("/", "");
		}
		return t;
	}
	
	@Asynchronous
	public void sendShipmentSms(Shipment shipment, String authHeader) {
		Courier c = getCourier(shipment.getCourierId(), authHeader);
		String text = "تم شحن القطع الى عنوانك للطلب رقم ";
		text += getCartNumbers(shipment.getShipmentItems());
		if(shipment.isTrackable()) {
			text += " رقم التتبع ";
			text += shipment.getTrackReference().replace(" ", "");
			text += " على الناقل ";
			text += c.getName();
			text += " , رابط التتبع: ";
			text += c.getTrackLink() + " ";
		}
		text += " شكرا لكم, نتمنى أن نكون عند حسن ظنكم";
		this.sendSms(shipment.getCustomerId(), shipment.getId(), text, "shipment", authHeader);
	}
	@Asynchronous
	public void sendSms(long customerId, long cartId, String text, String purpose, String authHeader) {
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("customerId", String.valueOf(customerId));
			map.put("text", text);
			map.put("cartId", String.valueOf(cartId));
			map.put("purpose", purpose);
			this.postSecuredRequest(AppConstants.SEND_SMS_TO_CUSTOMER, map, authHeader);
		} catch (Exception ex) {
		}
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
	

	public Response getSecuredRequest(String link, String authHeader) {
		Builder b = ClientBuilder.newClient().target(link).request();
		b.header(HttpHeaders.AUTHORIZATION, authHeader);
		Response r = b.get();
		return r;
	}
	
}

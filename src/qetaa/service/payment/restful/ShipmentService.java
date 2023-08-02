package qetaa.service.payment.restful;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import qetaa.service.payment.dao.DAO;
import qetaa.service.payment.filters.SecuredUser;
import qetaa.service.payment.helpers.Helper;
import qetaa.service.payment.model.Wallet;
import qetaa.service.payment.model.WalletItem;
import qetaa.service.payment.model.shipment.Shipment;
import qetaa.service.payment.model.shipment.ShipmentItem;

@Path("/shipment/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ShipmentService {

	@EJB
	private DAO dao;
	
	@EJB
	private AsyncService async;
	
	
	@POST
	@Path("new-shipment")
	@SecuredUser
	public Response createEmptyWallet(Map<String,Object> map) {
		try {
			Long customerId = ((Number) map.get("customerId")).longValue();
			Long addressId = ((Number) map.get("addressId")).longValue();
			Shipment s = new Shipment();
			s.setCustomerId(customerId);
			s.setCreatedBy(0);
			s.setCourierId(0);
			s.setAddressId(addressId);
			s.setTrackable(false);
			s.setShipmentFees(0);
			s.setStatus('W');//Waiting for update
			dao.persist(s);
			return Response.status(200).entity(s.getId()).build();
		}catch(Exception ex) {
			ex.printStackTrace();
			return Response.status(500).build();
		}
	}
	
	@SecuredUser
	@PUT
	@Path("shipment")
	public Response createShipment(Shipment shipment, @HeaderParam("Authorization") String authHeader) {
		try {
			shipment.setCreated(new Date());
			shipment.setStatus('S');//Shipped
			dao.update(shipment);
			createShipmentItems(shipment);
			updateWallet(shipment);
			async.sendShipmentSms(shipment, authHeader);
			return Response.status(201).build();
		}catch(Exception ex) {
			return Response.status(500).build();
		}
	}
	
	private void createShipmentItems(Shipment shipment) {
		for(ShipmentItem item :  shipment.getShipmentItems()) {
			item.setShipped(new Date());
			item.setShipmentId(shipment.getId());
			item.setStatus('S');//shipped
			dao.persist(item);
		}
	}
	
	private void updateWallet(Shipment shipment) {
		Set<Long> set = new HashSet<Long>();
		for(ShipmentItem item :  shipment.getShipmentItems()) {
			WalletItem wi = dao.find(WalletItem.class, item.getWalletItemId());
			set.add(wi.getWalletId());
			wi.setStatus('H');
			dao.update(wi);
		}
		
		for(Long walletId : set) {
			List<WalletItem> wis = dao.getThreeConditions(WalletItem.class, "walletId", "status", "itemType", walletId, 'S', 'P');
			if(wis.isEmpty()) {
				Wallet w = dao.find(Wallet.class, walletId);
				w.setStatus('H');
				List<WalletItem> ditems = dao.getThreeConditions(WalletItem.class, "walletId", "status", "itemType", walletId, 'S', 'D');
				for(WalletItem wi : ditems) {
					wi.setStatus('H');
					dao.update(wi);
				}
				dao.update(w);
			}
		}
	}
	
	@SecuredUser
	@GET
	@Path("/shipments/year/{param}/month/{param2}/courier/{param3}/cart/{param4}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWalletsReport(@PathParam(value = "param") int year, 
			@PathParam(value = "param2") int month,
			@PathParam(value = "param3") int courierId,
			@PathParam(value = "param4") long cartId) {
		try {
			Date from = Helper.getFromDate(month, year);
			Date to = Helper.getToDate(month, year);
			

			List<Shipment> shipments= new ArrayList<>();
			String jpql = "select b from Shipment b where b.created between :value0 and :value1";

			if (courierId == 0 && cartId == 0) {
				jpql = jpql + " order by b.created asc";
				shipments = dao.getJPQLParams(Shipment.class, jpql, from, to);
			} else if(courierId > 0 && cartId == 0){
				jpql = jpql + " and b.courierId = :value2";
				jpql = jpql + " order by b.created asc";
				shipments = dao.getJPQLParams(Shipment.class, jpql, from, to, courierId);
			}else if(courierId == 0 && cartId > 0) {
				jpql = jpql + " and b.id in (select c.shipmentId from ShipmentItem c where c.walletItem.cartId = :value2)";
				jpql = jpql + " order by b.created asc";
				shipments = dao.getJPQLParams(Shipment.class, jpql, from, to, cartId);
			}
			else if (courierId > 0 && cartId > 0) {
				jpql = jpql + " and b.id in (select c.shipmentId from ShipmentItem c where c.walletItem.cartId = :value2)";
				jpql = jpql + " and b.courierId = :value3 ";
				jpql = jpql + " order by b.created asc";
				shipments = dao.getJPQLParams(Shipment.class, jpql, from, to, cartId, courierId);
			}
			
			for(Shipment sh : shipments) {
				List<ShipmentItem> items = dao.getCondition(ShipmentItem.class, "shipmentId", sh.getId());
				sh.setShipmentItems(items);
			}
			
			return Response.status(200).entity(shipments).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(500).build();
		}
	}
	
	public Response getSecuredRequest(String link, String authHeader) {
		Builder b = ClientBuilder.newClient().target(link).request();
		b.header(HttpHeaders.AUTHORIZATION, authHeader);
		Response r = b.get();
		return r;
	}
	
	public <T> Response postSecuredRequest(String link, T t, String authHeader) {
		Builder b = ClientBuilder.newClient().target(link).request();
		b.header(HttpHeaders.AUTHORIZATION, authHeader);
		Response r = b.post(Entity.entity(t, "application/json"));// not secured
		return r;
	}

	
}

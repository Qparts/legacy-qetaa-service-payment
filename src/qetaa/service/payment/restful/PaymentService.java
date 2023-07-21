package qetaa.service.payment.restful;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import qetaa.service.payment.dao.DAO;
import qetaa.service.payment.filters.Secured;
import qetaa.service.payment.filters.SecuredCustomer;
import qetaa.service.payment.filters.SecuredUser;
import qetaa.service.payment.filters.SecuredVendor;
import qetaa.service.payment.helpers.AppConstants;
import qetaa.service.payment.model.Bank;
import qetaa.service.payment.model.PartsPayment;
import qetaa.service.payment.model.PurchaseOrder;
import qetaa.service.payment.model.Wallet;
import qetaa.service.payment.model.contract.CompletedCart;
import qetaa.service.payment.model.contract.PurchaseOrderContract;

@Path("/")
public class PaymentService {
	@EJB
	private DAO dao;

	@EJB
	private AsyncService async;

	@SecuredVendor
	@GET
	@Path("/test1")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void app2() {

	}
	
	@SecuredUser
	@GET
	@Path("/parts-payment/cart/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPartsPayment(@PathParam(value="param") long cartId) {
		try {
			PartsPayment pp = dao.findCondition(PartsPayment.class, "cartId", cartId);
			return Response.status(200).entity(pp).build();
		}catch(Exception ex) {
			return Response.status(500).build();
		}
	}
	
	@SecuredUser
	@GET
	@Path("wallets/awaiting")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAwaitingWallets() {
		try {
			String jpql = "select b from Wallet b where b.status =:value0 order by b.created asc";
			List<Wallet> wallets = dao.getJPQLParams(Wallet.class, jpql, 'A');
			return Response.status(200).entity(wallets).build();
		}catch(Exception ex) {
			return Response.status(500).build();
		}
	}
	
	@SecuredUser
	@GET
	@Path("/wallets/year/{param}/month/{param2}/payment-type/{param3}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWalletsReport(@PathParam(value = "param") int year, 
			@PathParam(value = "param2") int month,
			@PathParam(value = "param3") String paymentType) {
		try {
			Date from = new Date();
			Date to = new Date();
			if (month == 12) {
				Calendar cFrom = new GregorianCalendar();
				cFrom.set(year, 0, 1, 0, 0, 0);
				cFrom.set(Calendar.MILLISECOND, 0);
				from.setTime(cFrom.getTimeInMillis());

				Calendar cTo = new GregorianCalendar();
				cTo.set(year, 11, 31, 0, 0, 0);
				cTo.set(Calendar.HOUR_OF_DAY, 23);
				cTo.set(Calendar.MINUTE, 59);
				cTo.set(Calendar.SECOND, 59);
				cTo.set(Calendar.MILLISECOND, cTo.getActualMaximum(Calendar.MILLISECOND));
				to.setTime(cTo.getTimeInMillis());
			} else {
				Calendar cFrom = new GregorianCalendar();
				cFrom.set(year, month, 1, 0, 0, 0);
				cFrom.set(Calendar.MILLISECOND, 0);
				from.setTime(cFrom.getTimeInMillis());

				Calendar cTo = new GregorianCalendar();
				cTo.set(year, month, 1, 0, 0, 0);
				cTo.set(Calendar.MILLISECOND, 0);
				cTo.set(Calendar.DAY_OF_MONTH, cTo.getActualMaximum(Calendar.DAY_OF_MONTH));
				cTo.set(Calendar.HOUR_OF_DAY, 23);
				cTo.set(Calendar.MINUTE, 59);
				cTo.set(Calendar.SECOND, 59);
				cTo.set(Calendar.MILLISECOND, cTo.getActualMaximum(Calendar.MILLISECOND));
				to.setTime(cTo.getTimeInMillis());
			}

			List<Wallet> wallets = new ArrayList<>();
			String jpql = "select b from Wallet b where b.created between :value0 and :value1 order by b.created asc";

			if (paymentType.equals("all")) {
				wallets= dao.getJPQLParams(Wallet.class, jpql, from, to);
			} else {
				jpql = jpql + " and b.paymentType = :value2";
				wallets = dao.getJPQLParams(Wallet.class, jpql, from, to, paymentType);
			}
			return Response.status(200).entity(wallets).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(500).build();
		}
	}
	

	@SecuredUser
	@GET
	@Path("/payment-report/year/{param}/month/{param2}/payment-type/{param3}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPaymentReport(@HeaderParam("Authorization") String authHeader,
			@PathParam(value = "param") int year, @PathParam(value = "param2") int month,
			@PathParam(value = "param3") String paymentType) {
		try {
			Date from = new Date();
			Date to = new Date();
			if (month == 12) {
				Calendar cFrom = new GregorianCalendar();
				cFrom.set(year, 0, 1, 0, 0, 0);
				cFrom.set(Calendar.MILLISECOND, 0);
				from.setTime(cFrom.getTimeInMillis());

				Calendar cTo = new GregorianCalendar();
				cTo.set(year, 11, 31, 0, 0, 0);
				cTo.set(Calendar.HOUR_OF_DAY, 23);
				cTo.set(Calendar.MINUTE, 59);
				cTo.set(Calendar.SECOND, 59);
				cTo.set(Calendar.MILLISECOND, cTo.getActualMaximum(Calendar.MILLISECOND));
				to.setTime(cTo.getTimeInMillis());
			} else {
				Calendar cFrom = new GregorianCalendar();
				cFrom.set(year, month, 1, 0, 0, 0);
				cFrom.set(Calendar.MILLISECOND, 0);
				from.setTime(cFrom.getTimeInMillis());

				Calendar cTo = new GregorianCalendar();
				cTo.set(year, month, 1, 0, 0, 0);
				cTo.set(Calendar.MILLISECOND, 0);
				cTo.set(Calendar.DAY_OF_MONTH, cTo.getActualMaximum(Calendar.DAY_OF_MONTH));
				cTo.set(Calendar.HOUR_OF_DAY, 23);
				cTo.set(Calendar.MINUTE, 59);
				cTo.set(Calendar.SECOND, 59);
				cTo.set(Calendar.MILLISECOND, cTo.getActualMaximum(Calendar.MILLISECOND));
				to.setTime(cTo.getTimeInMillis());
			}

			List<PartsPayment> payments = new ArrayList<>();
			String jpql = "select b from PartsPayment b where b.created between :value0 and :value1 order by b.created asc";

			if (paymentType.equals("all")) {
				payments = dao.getJPQLParams(PartsPayment.class, jpql, from, to);
			} else {
				jpql = jpql + " and b.paymentType = :value2";
				payments = dao.getJPQLParams(PartsPayment.class, jpql, from, to, paymentType);
			}

			List<CompletedCart> completedCarts = new ArrayList<>();
			for (PartsPayment partsPayment : payments) {
				Response r = this.getSecuredRequest(AppConstants.getCompletedCarts(partsPayment.getCartId()),
						authHeader);
				if (r.getStatus() == 200) {
					CompletedCart completed = r.readEntity(CompletedCart.class);
					completed.setPayment(partsPayment);
					completedCarts.add(completed);
				}
			}

			return Response.status(200).entity(completedCarts).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(500).build();
		}
	}

	@SecuredCustomer
	@POST
	@Path("/save-successful-payment/customer")//from credit card
	@Consumes(MediaType.APPLICATION_JSON)
	public Response processPaymentCustomer(@HeaderParam("Authorization") String authHeader, PartsPayment payment) {
		try {
			PartsPayment test = dao.findCondition(PartsPayment.class, "cartId", payment.getCartId());
			if (test == null) {
				Double fees = (payment.getAmount() * 0.024) + 100;
				payment.setCreditFees(fees);
				payment = dao.persistAndReturn(payment);
				async.createLoyaltyPoints(authHeader, payment.getCartId(), payment.getCustomerId(), payment.getAmount());
				return Response.status(200).entity(payment.getId()).build();
			} else {
				return Response.status(409).build();
			}
		} catch (Exception ex) {
			return Response.status(500).build();
		}
	}

	@SecuredCustomer
	@POST
	@Path("/fund-wallet/credit-card")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response fundWalletCreditCard(Map<String,Object> map) {
		try {
			Double amount = ((Number) map.get("amount")).doubleValue();
			Long cartId = ((Number) map.get("cartId")).longValue();
			Long customerId = ((Number) map.get("customerId")).longValue();
			String customerName = (String) map.get("customerName");
			String ccCompany = (String) map.get("ccCompany");
			Double creditFees = ((Number) map.get("creditFees")).doubleValue();
			String gateway = (String) map.get("gateway");
			String transId = (String) map.get("transactionId");

			//for credit card
			Wallet wallet = new Wallet();
			wallet.setCcCompany(ccCompany);
			wallet.setCreditFees(creditFees);
			wallet.setGateway(gateway);
			wallet.setTransactionId(transId);
			wallet.setAmount(amount);
			wallet.setCartId(cartId);
			wallet.setCurrency("SAR");
			wallet.setCustomerId(customerId);
			wallet.setCustomerName(customerName);
			wallet.setPaymentType("creditcard");
			wallet.setWalletType('P');
			wallet.setCreated(new Date());
			wallet.setStatus('A');//awaiting sales
			dao.persist(wallet);
			return Response.status(201).build();
		}
		catch(Exception ex) {
			return Response.status(500).build();
		}
	}
	
	@SecuredUser
	@POST
	@Path("/fund-wallet/wire-transfer")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response fundWalletWireTransfer(@HeaderParam("Authorization") String authHeader, Map<String,Object> map) {
		try {
			Double amount = ((Number) map.get("amount")).doubleValue();
			Integer bankId = (Integer) map.get("bankId");
			Integer confirmedBy = (Integer) map.get("confirmedBy");
			Long cartId = ((Number) map.get("cartId")).longValue();
			Long customerId = ((Number) map.get("customerId")).longValue();
			String customerName = (String) map.get("customerName");

			Wallet wallet = new Wallet();
			wallet.setAmount(amount);
			wallet.setBankConfirmedBy(confirmedBy);
			wallet.setCartId(cartId);
			wallet.setCurrency("SAR");
			wallet.setCustomerId(customerId);
			wallet.setCustomerName(customerName);
			wallet.setPaymentType("wiretransfer");//wiretransfer
			wallet.setWalletType('P');
			wallet.setCreated(new Date());
			wallet.setStatus('A');//awaiting sales
			Bank bank = dao.find(Bank.class, bankId);
			wallet.setBank(bank);
			dao.persist(wallet);
			
			async.confirmWireTransfer(authHeader, map);
			return Response.status(201).build();
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return Response.status(500).build();
		}
	}
	
	@SecuredUser
	@POST
	@Path("/save-successful-payment/user")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response processPaymentUser(@HeaderParam("Authorization") String authHeader, PartsPayment payment) {
		try {
			dao.persist(payment);
			// update purchase order
			List<PurchaseOrder> pos = dao.getCondition(PurchaseOrder.class, "cartId", payment.getCartId());
			for (PurchaseOrder po : pos) {
				po.setStatus('W');// waiting for approval
				po.setCreated(new Date());
				dao.update(po);
			}
			this.async.createLoyaltyPoints(authHeader, payment.getCartId(), payment.getCustomerId(), payment.getAmount());
			
			return Response.status(200).build();
		} catch (Exception ex) {
			return Response.status(500).build();
		}
	}
	
	@SecuredUser
	@DELETE
	@Path("purchase-order/cart/{cartId}")
	public Response deletePurchaseOrder(@PathParam(value="cartId") long cartId) {
		try {
			String sql = "delete from pay_purchase_order where cart_id = " + cartId;
			dao.updateNative(sql);
			return Response.status(201).build();
		}catch(Exception ex) {
			return Response.status(500).build();
		}
	}

	@Secured
	@POST
	@Path("purchase-order")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createPurchaseOrder(List<PurchaseOrderContract> contracts) {
		try {
			for (PurchaseOrderContract contract : contracts) {
				PartsPayment pp = dao.findCondition(PartsPayment.class, "cartId", contract.getCartId());
				PurchaseOrder po = new PurchaseOrder();
				po.setAmount(contract.getAmount());
				po.setCartId(contract.getCartId());
				po.setCreated(new Date());
				po.setType('T');
				po.setVendorId(contract.getVendorId());
				if (pp != null) {
					po.setStatus('W');// WAITING for approval
				} else {
					po.setStatus('T');// waiting confirmation from transfer
				}
				// check if payable exists
				PurchaseOrder test = dao.findTwoConditions(PurchaseOrder.class, "cartId", "vendorId", po.getCartId(),
						po.getVendorId());
				if (test == null) {
					dao.persist(po);
				}
			}

			return Response.status(201).build();

		} catch (Exception ex) {
			return Response.status(500).build();
		}
	}
	
	
	//will be retired
	@SecuredVendor
	@GET
	@Path("uninvoiced-purchase-orders/vendor/{vendorId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUninvoicedPurchaseOrders(@PathParam(value="vendorId") int vendorId) {
		try {
			List<PurchaseOrder> pos = dao.getTwoConditionsOrdered(PurchaseOrder.class, "vendorId", "status", vendorId, 'W', "id", "asc");
			return Response.status(200).entity(pos).build();
		} catch (Exception ex) {
			return Response.status(500).build();
		}
	}

	@SecuredVendor
	@GET
	@Path("outstanding-purchase-orders/vendor/{vendorId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOutstandingPayments(@PathParam(value = "vendorId") int vendorId) {
		try {
			List<PurchaseOrder> pos = dao.getTwoConditionsOrdered(PurchaseOrder.class, "vendorId", "status", vendorId, 'O', "id", "asc");
			return Response.status(200).entity(pos).build();
		} catch (Exception ex) {
			return Response.status(500).build();
		}
	}

	@Secured
	@GET
	@Path("awaiting-purchase-orders")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAwaitingPayments() {
		try {
			String jpql = "select b from PurchaseOrder b where b.status = :value0 order by b.id";
			List<PurchaseOrder> pos = dao.getJPQLParams(PurchaseOrder.class, jpql, 'W');
			return Response.status(200).entity(pos).build();
		} catch (Exception ex) {
			return Response.status(500).build();
		}
	}

	@SecuredUser
	@PUT
	@Path("confirm-puchase-order")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response confirmPurchaseOrder(PurchaseOrder po) {
		try {
			po.setStatus('O');// outstanding
			dao.update(po);
			return Response.status(201).build();
		} catch (Exception ex) {
			return Response.status(500).build();
		}
	}

	public Response getSecuredRequest(String link, String authHeader) {
		Builder b = ClientBuilder.newClient().target(link).request();
		b.header(HttpHeaders.AUTHORIZATION, authHeader);
		Response r = b.get();
		return r;
	}
}

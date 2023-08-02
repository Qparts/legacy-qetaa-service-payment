package qetaa.service.payment.restful;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import qetaa.service.payment.helpers.Helper;
import qetaa.service.payment.model.Bank;
import qetaa.service.payment.model.PartsPayment;
import qetaa.service.payment.model.PurchaseOrder;
import qetaa.service.payment.model.Wallet;
import qetaa.service.payment.model.WalletItem;
import qetaa.service.payment.model.WalletItemVendor;
import qetaa.service.payment.model.WalletQuotation;
import qetaa.service.payment.model.WalletQuotationItem;
import qetaa.service.payment.model.contract.CompletedCart;
import qetaa.service.payment.model.contract.PurchaseOrderContract;
import qetaa.service.payment.model.contract.WalletWire;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PaymentService {
	@EJB
	private DAO dao;

	@EJB
	private AsyncService async;

	@Secured
	@SecuredVendor
	@GET
	@Path("/test1")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void app2() {

	}

	@SecuredUser
	@GET
	@Path("wallet-items/sold/customer/{customerId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWalletSoldItems(@PathParam(value = "customerId") long customerId) {
		try {
			String hql = "select b from WalletItem b where b.status = :value0 and b.itemType = :value1 and b.walletId in ("
					+ "select c from Wallet c where c.customerId = :value2)";
			List<WalletItem> walletItems = dao.getJPQLParams(WalletItem.class, hql, 'S', 'P', customerId);
			return Response.status(200).entity(walletItems).build();
		} catch (Exception ex) {
			return Response.status(500).build();
		}
	}

	@SecuredUser
	@GET
	@Path("/parts-payment/cart/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPartsPayment(@PathParam(value = "param") long cartId) {
		try {
			PartsPayment pp = dao.findCondition(PartsPayment.class, "cartId", cartId);
			return Response.status(200).entity(pp).build();
		} catch (Exception ex) {
			return Response.status(500).build();
		}
	}

	@SecuredUser
	@GET
	@Path("wallet/{param}/purchased")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPurchasedWallet(@PathParam(value = "param") long id) {
		try {
			Wallet w = dao.findTwoConditions(Wallet.class, "status", "id", 'P', id);// purchased wallet
			if (w == null) {
				return Response.status(404).build();
			}
			List<WalletItem> wis = dao.getCondition(WalletItem.class, "walletId", w.getId());
			w.setWalletItems(wis);
			return Response.status(200).entity(w).build();
		} catch (Exception ex) {
			return Response.status(500).build();
		}
	}
	
	@SecuredUser
	@PUT
	@Path("wallet-item")
	public Response updateWalletItem(WalletItem walletItem) {
		try {
			dao.update(walletItem);
			return Response.status(201).build();
		}catch(Exception ex) {
			return Response.status(500).build();
		}
	}

	@SecuredUser
	@POST
	@Path("wallet-item-vendors")
	public Response createWalletItemVendors(Set<WalletItemVendor> wivs) {
		try {
			for (WalletItemVendor wiv : wivs) {
				if (wiv.getCost() != null) {
					WalletItemVendor wivcheck = dao.findTwoConditions(WalletItemVendor.class, "vendorId",
							"walletItemId", wiv.getVendorId(), wiv.getWalletItemId());
					if (wivcheck == null) {
						wiv.setDate(new Date());
						dao.persist(wiv);
					} else {
						if (!wivcheck.getCost().equals(wiv.getCost())) {
							wivcheck.setCost(wiv.getCost());
							wivcheck.setDate(new Date());
							dao.update(wivcheck);
						}
					}
				}
			}
			return Response.status(201).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(500).build();
		}
	}

	@SecuredUser
	@GET
	@Path("wallet/{param}/awaiting")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAwaitingWallet(@PathParam(value = "param") long id) {
		try {
			Wallet w = dao.findTwoConditions(Wallet.class, "status", "id", 'A', id);
			if (w == null) {
				return Response.status(404).build();
			}
			List<WalletItem> wis = dao.getCondition(WalletItem.class, "walletId", w.getId());
			prepareWalletItemsVendors(wis);
			prepareWalletQuotations(w);
			w.setWalletItems(wis);
			return Response.status(200).entity(w).build();
		} catch (Exception ex) {
			return Response.status(500).build();
		}
	}
	
	private void prepareWalletQuotations(Wallet wallet) {
		List<WalletQuotation> wqs = dao.getCondition(WalletQuotation.class, "walletId", wallet.getId());
		for(WalletQuotation wq : wqs) {
			List<WalletQuotationItem> wqItems = dao.getCondition(WalletQuotationItem.class, "walletQuotationId", wq.getId());
			wq.setWalletQuotationItems(wqItems);
		}
		wallet.setWalletQuotations(wqs);
	}

	private void prepareWalletItemsVendors(List<WalletItem> wis) {
		for (WalletItem wi : wis) {
			wi.setWalletItemVendors(new ArrayList<>());
			List<WalletItemVendor> list = dao.getCondition(WalletItemVendor.class, "walletItemId", wi.getId());
			wi.setWalletItemVendors(list);
		}
	}
	
	@SecuredUser
	@GET
	@Path("wallets-notification/process")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAwaitingWalletsNotification() {
		try {
			String jpql = "select count(b) from Wallet b where b.status in (:value0 , :value1 , :value2)";
			Long l = dao.findJPQLParams(Long.class, jpql, 'A', 'P' , 'S');
			if(l == null) { 
				l = 0L;
			}
			return Response.status(200).entity(l).build();
		}catch(Exception ex) {
			return Response.status(500).build();
		}
	}

	@SecuredUser
	@GET
	@Path("wallets/process")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAwaitingWallets() {
		try {
			String jpql = "select b from Wallet b where b.status =:value0 or b.status = :value1 or b.status = :value2 order by b.created asc";
			List<Wallet> wallets = dao.getJPQLParams(Wallet.class, jpql, 'A', 'P', 'S');// awaiting or purchased or sold
			for (Wallet w : wallets) {
				List<WalletItem> wis = dao.getCondition(WalletItem.class, "walletId", w.getId());
				w.setWalletItems(wis);
			}
			return Response.status(200).entity(wallets).build();
		} catch (Exception ex) {
			return Response.status(500).build();
		}
	}

	@SecuredUser
	@GET
	@Path("/wallets/year/{param}/month/{param2}/payment-type/{param3}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWalletsReport(@PathParam(value = "param") int year, @PathParam(value = "param2") int month,
			@PathParam(value = "param3") String paymentType) {
		try {
			Date from = Helper.getFromDate(month, year);
			Date to = Helper.getToDate(month, year);

			List<Wallet> wallets = new ArrayList<>();
			String jpql = "select b from Wallet b where b.created between :value0 and :value1";

			if (paymentType.equals("all")) {
				jpql = jpql + " order by b.created asc";
				wallets = dao.getJPQLParams(Wallet.class, jpql, from, to);
			} else {
				jpql = jpql + " and b.paymentType = :value2 order by b.created asc";
				wallets = dao.getJPQLParams(Wallet.class, jpql, from, to, paymentType);
			}

			for (Wallet wallet : wallets) {
				List<WalletItem> items = dao.getCondition(WalletItem.class, "walletId", wallet.getId());
				wallet.setWalletItems(items);
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
			Date from = Helper.getFromDate(month, year);
			Date to = Helper.getToDate(month, year);
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
	@Path("/save-successful-payment/customer") // from credit card
	@Consumes(MediaType.APPLICATION_JSON)
	public Response processPaymentCustomer(@HeaderParam("Authorization") String authHeader, PartsPayment payment) {
		try {
			PartsPayment test = dao.findCondition(PartsPayment.class, "cartId", payment.getCartId());
			if (test == null) {
				Double fees = (payment.getAmount() * 0.024) + 100;
				payment.setCreditFees(fees);
				payment = dao.persistAndReturn(payment);
				async.createLoyaltyPoints(authHeader, payment.getCartId(), payment.getCustomerId(),
						payment.getAmount());
				return Response.status(200).entity(payment.getId()).build();
			} else {
				return Response.status(409).build();
			}
		} catch (Exception ex) {
			return Response.status(500).build();
		}
	}

	@SecuredUser
	@PUT
	@Path("/fund-wallet/credit-sales")
	public Response fundWalletCreditSales(Wallet wallet) {
		try {
			wallet.setCurrency("SAR");
			wallet.setPaymentType("creditsales");
			wallet.setWalletType('P');
			wallet.setCreated(new Date());
			wallet.setStatus('A');// awaiting sales
			dao.update(wallet);
			for (WalletItem wi : wallet.getWalletItems()) {
				wi.setWalletId(wallet.getId());
				dao.persist(wi);
			}
			// delete wallets that are 0;
			String check = "select b from Wallet b where b.cartId = :value0 and b.created =:value1 and b.status = :value2";
			List<Wallet> wallets = dao.getJPQLParams(Wallet.class, check, wallet.getCartId(), null, 'A');// awaiting
			for (Wallet w : wallets) {
				Long id = w.getId();
				dao.delete(w);
				dao.updateNative("DELETE FROM PAY_WALLET WHERE ID = " + id);
			}
			return Response.status(201).build();
		} catch (Exception e) {
			return Response.status(500).build();
		}
	}

	@Secured
	@PUT
	@Path("/fund-wallet/cash-on-delivery")
	public Response fundWalletCashOnDeluvery(Wallet wallet) {
		try {
			wallet.setCurrency("SAR");
			wallet.setPaymentType("cashondelivery");
			wallet.setWalletType('P');
			wallet.setCreated(new Date());
			wallet.setStatus('A');// awaiting sales
			dao.update(wallet);
			for (WalletItem wi : wallet.getWalletItems()) {
				wi.setWalletId(wallet.getId());
				dao.persist(wi);
			}
			// delete wallets that are 0;
			String check = "select b from Wallet b where b.cartId = :value0 and b.created =:value1 and b.status = :value2";
			List<Wallet> wallets = dao.getJPQLParams(Wallet.class, check, wallet.getCartId(), null, 'A');// awaiting
			for (Wallet w : wallets) {
				Long id = w.getId();
				dao.delete(w);
				dao.updateNative("DELETE FROM PAY_WALLET WHERE ID = " + id);
			}
			return Response.status(201).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(500).build();
		}
	}

	@SecuredCustomer
	@PUT
	@Path("/fund-wallet/credit-card")
	public Response fundWalletCreditCard(Wallet wallet) {
		try {
			// for credit card
			wallet.setCurrency("SAR");
			wallet.setPaymentType("creditcard");
			wallet.setWalletType('P');
			wallet.setCreated(new Date());
			wallet.setStatus('A');// awaiting sales
			dao.update(wallet);
			for (WalletItem wi : wallet.getWalletItems()) {
				wi.setWalletId(wallet.getId());
				dao.persist(wi);
			}
			// delete wallets that are 0;
			String check = "select b from Wallet b where b.cartId = :value0 and b.created =:value1 and b.status = :value2";
			List<Wallet> wallets = dao.getJPQLParams(Wallet.class, check, wallet.getCartId(), null, 'A');// awaiting
			for (Wallet w : wallets) {
				Long id = w.getId();
				dao.delete(w);
				dao.updateNative("DELETE FROM PAY_WALLET_ITEM WHERE WALLET_ID = " + id);
			}

			return Response.status(201).build();
		} catch (Exception ex) {
			return Response.status(500).build();
		}
	}

	@SecuredUser
	@POST
	@Path("new-wallet-quotation")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createEmptyWalletQuotation(Long walletId) {
		try {
			WalletQuotation w = new WalletQuotation();
			w.setCreatedBy(0);
			w.setWalletId(walletId);
			dao.persist(w);
			return Response.status(201).entity(w.getId()).build();
		} catch (Exception ex) {
			return Response.status(500).build();
		}
	}
	
	@SecuredUser
	@POST
	@Path("new-wallet/refund")
	public Response createEmptyRefundWallet(Long cartId) {
		try {
			Wallet w = new Wallet();
			w.setCartId(cartId);
			w.setCustomerId(0);
			w.setStatus('R');
			w.setWalletType('R');
			dao.persist(w);
			return Response.status(201).entity(w.getId()).build();
		} catch (Exception ex) {
			return Response.status(500).build();
		}
	}

	@SecuredUser
	@POST
	@Path("new-wallet/sales-return")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createEmptySalesReturnWallet(Long cartId) {
		try {
			Wallet w = new Wallet();
			w.setCartId(cartId);
			w.setCustomerId(0);
			w.setStatus('R');
			w.setWalletType('S');
			dao.persist(w);
			return Response.status(201).entity(w.getId()).build();
		} catch (Exception ex) {
			return Response.status(500).build();
		}
	}

	@Secured
	@POST
	@Path("new-wallet")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createEmptyWallet(Long cartId) {
		try {
			Wallet w = new Wallet();
			w.setCartId(cartId);
			w.setCustomerId(0);
			w.setStatus('A');
			w.setWalletType('P');
			dao.persist(w);
			return Response.status(201).entity(w.getId()).build();
		} catch (Exception ex) {
			return Response.status(500).build();
		}
	}

	private void updateDeliveryItemAfterPurchase(Long walletId) {
		// get delivery item and update
		String jpql2 = "select b from WalletItem b where b.walletId = :value0 and b.status =:value1 and b.itemType = :value2";
		List<WalletItem> deliveryItems = dao.getJPQLParams(WalletItem.class, jpql2, walletId, 'A', 'D');
		for (WalletItem wi : deliveryItems) {
			wi.setStatus('V');
			dao.update(wi);
		}
	}

	private void updateDeliveryItemAfterSales(Long walletId) {
		// get delivery item and update
		String jpql2 = "select b from WalletItem b where b.walletId = :value0 and b.status =:value1 and b.itemType = :value2";
		List<WalletItem> deliveryItems = dao.getJPQLParams(WalletItem.class, jpql2, walletId, 'V', 'D');
		for (WalletItem wi : deliveryItems) {
			wi.setStatus('S');
			dao.update(wi);
		}
	}

	private void updateWalletStatus(Long walletId, char status) {
		Wallet origWallet = dao.find(Wallet.class, walletId);
		origWallet.setStatus(status);// original wallet completed
		dao.update(origWallet);
	}
	
	@SecuredUser
	@PUT
	@Path("replace-wallet-item-product")
	public Response updateWalletItemReplaceProduct(Map<String,Number> map) {
		try {
			Long walletItemId = ((Number) map.get("walletItemId")).longValue();
			Long productId = ((Number) map.get("productId")).longValue();
			WalletItem wi = dao.find(WalletItem.class, walletItemId);
			wi.setProductId(productId);
			dao.update(wi);
			return Response.status(201).build();
		}catch(Exception ex) {
			return Response.status(500).build();
		}
	}

	@SecuredUser
	@PUT
	@Path("/wallet-item-sales")
	public Response updateWalletItemsAfterSales(List<Map<String, Number>> list) {
		try {
			Long walletId = 0L;
			for (Map<String, Number> map : list) {
				Long walletItemId = ((Number) map.get("walletItemId")).longValue();
				Long purchaseProductId = ((Number) map.get("purchaseProductId")).longValue();
				Long salesProductId = ((Number) map.get("salesProductId")).longValue();
				Integer salesProductQuantity = ((Number) map.get("salesProductQuantity")).intValue();
				WalletItem walletItem = dao.find(WalletItem.class, walletItemId);
				walletId = walletItem.getWalletId();
				if (walletItem.getQuantity() == salesProductQuantity.intValue()
						&& purchaseProductId.equals(walletItem.getPurchasedItemId())) {
					walletItem.setStatus('S');
					walletItem.setSoldItemId(salesProductId);
					dao.update(walletItem);
				}
			}

			// check if all items purchased
			String jpql = "select b from WalletItem b where b.walletId = :value0 and b.status = :value1 and b.itemType = :value2";
			List<WalletItem> origItems = dao.getJPQLParams(WalletItem.class, jpql, walletId, 'P', 'P');// still
																										// purchased
																										// available
			// no purchased wallet available
			if (origItems.isEmpty()) {
				updateDeliveryItemAfterSales(walletId);
				updateWalletStatus(walletId, 'S');// wallet sold
			}

			return Response.status(201).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(500).build();
		}
	}

	@SecuredUser
	@PUT
	@Path("wallet-item-purchase")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateWalletItems(List<Map<String, Number>> list) {
		try {
			Long walletId = 0L;
			for (Map<String, Number> map : list) {
				Long walletItemId = ((Number) map.get("walletItemId")).longValue();
				Long purchaseProductId = ((Number) map.get("purchaseProductId")).longValue();
				Integer ppQuantity = ((Number) map.get("purchaseProductQuantity")).intValue();
				Integer ppVendorId = ((Number) map.get("vendorId")).intValue();
				WalletItem walletItem = dao.find(WalletItem.class, walletItemId);
				walletId = walletItem.getWalletId();
				if (ppQuantity < walletItem.getQuantity()) {
					WalletItem newWi = new WalletItem();
					newWi.setCartId(walletItem.getCartId());
					newWi.setItemDesc(walletItem.getItemDesc());
					newWi.setItemNumber(walletItem.getItemNumber());
					newWi.setItemType(walletItem.getItemType());
					newWi.setProductId(walletItem.getProductId());
					newWi.setPurchasedItemId(walletItem.getPurchasedItemId());
					newWi.setQuantity(walletItem.getQuantity() - ppQuantity);
					newWi.setRefundedItemId(walletItem.getRefundedItemId());
					newWi.setRefundNote(walletItem.getRefundNote());
					newWi.setStatus('A');
					newWi.setUnitQuotedCost(walletItem.getUnitQuotedCost());
					newWi.setUnitQuotedCostWv(walletItem.getUnitQuotedCostWv());
					newWi.setUnitQuotedCostWv(walletItem.getUnitQuotedCostWv());
					newWi.setUnitSales(walletItem.getUnitSales());
					newWi.setUnitSalesNet(walletItem.getUnitSalesNet());
					newWi.setUnitSalesNetWv(walletItem.getUnitSalesNetWv());
					newWi.setUnitSalesWv(walletItem.getUnitSalesWv());
					newWi.setVendorId(walletItem.getVendorId());
					newWi.setWalletId(walletItem.getWalletId());
					walletItem.setQuantity(ppQuantity);
					dao.persist(newWi);
				}
				walletItem.setVendorId(ppVendorId);
				walletItem.setPurchasedItemId(purchaseProductId);
				walletItem.setStatus('P');
				dao.update(walletItem);
			}
			// check if all items purchased
			String jpql = "select b from WalletItem b where b.walletId = :value0 and b.status = :value1 and b.itemType = :value2";
			List<WalletItem> origItems = dao.getJPQLParams(WalletItem.class, jpql, walletId, 'A', 'P');// still awaiting
			// no awaiting wallet available
			if (origItems.isEmpty()) {
				updateDeliveryItemAfterPurchase(walletId);
				updateWalletStatus(walletId, 'P');// set wallet purchased
			}

			return Response.status(201).build();
		} catch (Exception ex) {
			return Response.status(500).build();
		}
	}

	@SecuredUser
	@PUT
	@Path("sales-return-wallet")
	public Response salesReturnWallet(Map<String, Object> map) {
		try {
			Long id = ((Number) map.get("id")).longValue();
			Long cid = ((Number) map.get("customerId")).longValue();
			String cname = (String) map.get("customerName");
			Long cartId = ((Number) map.get("cartId")).longValue();
			Integer bankId = ((Number) map.get("bankId")).intValue();
			Bank bank = dao.find(Bank.class, bankId);
			Integer confBy = ((Number) map.get("bankConfirmedBy")).intValue();
			Double discPercentage = ((Number) map.get("discountPercentage")).doubleValue();
			List<Map<String, Object>> walletItems = (List<Map<String, Object>>) map.get("walletItems");

			Wallet wallet = new Wallet();
			wallet.setId(id);
			wallet.setCustomerId(cid);
			wallet.setCustomerName(cname);
			wallet.setCartId(cartId);
			wallet.setBankConfirmedBy(confBy);
			wallet.setDiscountPercentage(discPercentage);
			wallet.setBank(bank);
			wallet.setCurrency("SAR");
			wallet.setPaymentType("wiretransfer");
			wallet.setWalletType('S');
			wallet.setCreated(new Date());
			wallet.setStatus('R');
			dao.update(wallet);
			for (Map<String, Object> map2 : walletItems) {
				WalletItem wi = new WalletItem();
				wi.setWalletId(wallet.getId());
				wi.setCartId(((Number) map2.get("cartId")).longValue());
				wi.setItemDesc((String) map2.get("itemDesc"));
				wi.setItemNumber((String) map2.get("itemNumber"));
				wi.setItemType(((String) map2.get("itemType")).charAt(0));
				wi.setProductId(map2.get("productId") == null ? null : ((Number) map2.get("productId")).longValue());
				wi.setQuantity(map2.get("quantity") == null ? null : ((Number) map2.get("quantity")).intValue());
				wi.setStatus(((String) map2.get("status")).charAt(0));
				wi.setUnitQuotedCost(map2.get("unitQuotedCost") == null ? null
						: ((Number) map2.get("unitQuotedCost")).doubleValue());
				wi.setUnitQuotedCostWv(map2.get("unitQuotedCostWv") == null ? null
						: ((Number) map2.get("unitQuotedCostWv")).doubleValue());
				wi.setUnitSales(((Number) map2.get("unitSales")).doubleValue());
				wi.setUnitSalesWv(((Number) map2.get("unitSalesWv")).doubleValue());
				wi.setUnitSalesNet(((Number) map2.get("unitSalesNet")).doubleValue());
				wi.setUnitSalesNetWv(((Number) map2.get("unitSalesNetWv")).doubleValue());
				wi.setPurchasedItemId(map2.get("purchasedItemId") == null ? null
						: ((Number) map2.get("purchasedItemId")).longValue());
				wi.setSoldItemId(map2.get("soldItemId") == null ? null : ((Number) map2.get("soldItemId")).longValue());
				dao.persist(wi);
			}
			return Response.status(201).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return Response.status(500).build();
		}
	}
	
	@SecuredUser
	@PUT
	@Path("quotation-wallet")
	public Response createWalletQuotation(WalletQuotation wq) {
		try {
			wq.setCreated(new Date());
			dao.update(wq);
			for(WalletQuotationItem wqi : wq.getWalletQuotationItems()) {
				wqi.setCreated(new Date());
				dao.persist(wqi);
			}
			return Response.status(201).build();
		}catch(Exception ex) {
			return Response.status(500).build();
		}
	}

	@SecuredUser
	@PUT
	@Path("refund-wallet/wire-transfer")
	public Response refundWalletWireTransfer(@HeaderParam("Authorization") String authHeader, WalletWire walletWire) {
		try {
			Wallet refundWallet = walletWire.getWallet();
			Long origId = walletWire.getOriginalWalletId();
			refundWallet.setCurrency("SAR");
			refundWallet.setPaymentType("wiretransfer");
			refundWallet.setWalletType('R');
			refundWallet.setCreated(new Date());
			refundWallet.setStatus('R');
			dao.update(refundWallet);
			// persist items
			for (WalletItem wi : refundWallet.getWalletItems()) {
				wi.setId(0);
				WalletItem origItem = dao.find(WalletItem.class, wi.getRefundedItemId());
				if (wi.getQuantity() < origItem.getQuantity()) {
					WalletItem newWi = new WalletItem();
					newWi.setCartId(origItem.getCartId());
					newWi.setItemDesc(origItem.getItemDesc());
					newWi.setItemNumber(origItem.getItemNumber());
					newWi.setItemType(origItem.getItemType());
					newWi.setProductId(origItem.getProductId());
					newWi.setPurchasedItemId(origItem.getPurchasedItemId());
					newWi.setQuantity(origItem.getQuantity() - wi.getQuantity());
					newWi.setRefundedItemId(null);
					newWi.setRefundNote(origItem.getRefundNote());
					newWi.setStatus('A');
					newWi.setUnitQuotedCost(origItem.getUnitQuotedCost());
					newWi.setUnitQuotedCostWv(origItem.getUnitQuotedCostWv());
					newWi.setUnitSales(origItem.getUnitSales());
					newWi.setUnitSalesNet(origItem.getUnitSalesNet());
					newWi.setUnitSalesNetWv(origItem.getUnitSalesNetWv());
					newWi.setUnitSalesWv(origItem.getUnitSalesWv());
					newWi.setVendorId(origItem.getVendorId());
					newWi.setWalletId(origItem.getWalletId());
					origItem.setQuantity(wi.getQuantity());
					dao.persist(newWi);
				}
				dao.persist(wi);
				origItem.setStatus('R');
				dao.update(origItem);
			}
			String jpql = "select b from WalletItem b where b.walletId = :value0 and b.status = :value1 and b.itemType = :value2";
			List<WalletItem> origItems = dao.getJPQLParams(WalletItem.class, jpql, origId, 'A', 'P');// still products
																										// awaiting
			if (origItems.isEmpty()) {
				// no more waiting items,
				List<WalletItem> checkItems = dao.getTwoConditions(WalletItem.class, "walletId", "itemType", origId,
						'P');
				char status = 'C';// set wallet status completed
				for (WalletItem wi : checkItems) {
					if (wi.getStatus() == 'P') {
						// there is a purchased item
						status = 'P';// set wallet status purchased
						break;
					}
				}
				if (status == 'P') {
					updateDeliveryItemAfterPurchase(origId);
				}

				updateWalletStatus(origId, status);// wallet is completed
				checkWalletConditionAfterRefund(authHeader, origId);
			}

			return Response.status(201).build();
		} catch (Exception ex) {
			return Response.status(500).build();
		}
	}

	private void checkWalletConditionAfterRefund(String authHeader, Long walletId) {
		boolean allRefunded = true;
		List<WalletItem> checkItems = dao.getCondition(WalletItem.class, "walletId", walletId);
		for (WalletItem wi : checkItems) {
			if (wi.getStatus() == 'A' || wi.getStatus() == 'P') {
				allRefunded = false;
				break;
			}
		}
		if (allRefunded) {
			Wallet w = dao.find(Wallet.class, walletId);
			async.refundCart(authHeader, w.getCartId());
		}
	}

	@SecuredUser
	@PUT
	@Path("/fund-wallet/wire-transfer")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response fundWalletWireTransfer(@HeaderParam("Authorization") String authHeader, WalletWire walletWire) {
		try {
			Wallet wallet = walletWire.getWallet();
			wallet.setCurrency("SAR");
			wallet.setPaymentType("wiretransfer");// wiretransfer
			wallet.setWalletType('P');
			wallet.setCreated(new Date());
			wallet.setStatus('A');// awaiting sales
			dao.update(wallet);
			// persist items
			for (WalletItem wi : wallet.getWalletItems()) {
				wi.setWalletId(wallet.getId());
				dao.persist(wi);
			}
			// delete wallets that are 0;
			String check = "select b from Wallet b where b.cartId = :value0 and b.created =:value1 and b.status = :value2";
			List<Wallet> wallets = dao.getJPQLParams(Wallet.class, check, wallet.getCartId(), null, 'A');// awaiting
			for (Wallet w : wallets) {
				Long id = w.getId();
				dao.delete(w);
				dao.updateNative("DELETE FROM PAY_WALLET_ITEM WHERE WALLET_ID = " + id);
			}
			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put("wireId", walletWire.getWireId());
			map2.put("cartId", wallet.getCartId());
			map2.put("confirmedBy", wallet.getBankConfirmedBy());
			async.confirmWireTransfer(authHeader, map2);
			return Response.status(201).build();
		} catch (Exception ex) {
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
			this.async.createLoyaltyPoints(authHeader, payment.getCartId(), payment.getCustomerId(),
					payment.getAmount());

			return Response.status(200).build();
		} catch (Exception ex) {
			return Response.status(500).build();
		}
	}

	@SecuredUser
	@DELETE
	@Path("purchase-order/cart/{cartId}")
	public Response deletePurchaseOrder(@PathParam(value = "cartId") long cartId) {
		try {
			String sql = "delete from pay_purchase_order where cart_id = " + cartId;
			dao.updateNative(sql);
			return Response.status(201).build();
		} catch (Exception ex) {
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

	// will be retired
	@SecuredVendor
	@GET
	@Path("uninvoiced-purchase-orders/vendor/{vendorId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUninvoicedPurchaseOrders(@PathParam(value = "vendorId") int vendorId) {
		try {
			List<PurchaseOrder> pos = dao.getTwoConditionsOrdered(PurchaseOrder.class, "vendorId", "status", vendorId,
					'W', "id", "asc");
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
			List<PurchaseOrder> pos = dao.getTwoConditionsOrdered(PurchaseOrder.class, "vendorId", "status", vendorId,
					'O', "id", "asc");
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

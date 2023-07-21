package qetaa.service.payment.model.contract;

import java.io.Serializable;
import java.util.Map;

import qetaa.service.payment.model.PartsPayment;

public class CompletedCart implements Serializable {

	private static final long serialVersionUID = 1L;

	private Map<String, Object> cart;
	private Map<String, Object> partsOrder;
	private PartsPayment payment;
	private double partsReturnTotal;

	public double getPartsReturnTotal() {
		return partsReturnTotal;
	}

	public void setPartsReturnTotal(double partsReturnTotal) {
		this.partsReturnTotal = partsReturnTotal;
	}

	public Map<String, Object> getCart() {
		return cart;
	}

	public void setCart(Map<String, Object> cart) {
		this.cart = cart;
	}

	public Map<String, Object> getPartsOrder() {
		return partsOrder;
	}

	public void setPartsOrder(Map<String, Object> partsOrder) {
		this.partsOrder = partsOrder;
	}

	public PartsPayment getPayment() {
		return payment;
	}

	public void setPayment(PartsPayment payment) {
		this.payment = payment;
	}

}

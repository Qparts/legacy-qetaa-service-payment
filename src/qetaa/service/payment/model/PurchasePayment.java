package qetaa.service.payment.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Table(name="pay_purchase_payment")
@Entity
public class PurchasePayment implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "pay_purchase_payment_id_seq_gen", sequenceName = "pay_purchase_payment_id_seq", initialValue=1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pay_purchase_payment_id_seq_gen")
	@Column(name="id")
	private long id;
	
	@Column(name="amount")
	private double amount;
	
	@Column(name="created_by")
	private int createdBy;
	
	@Column(name="created")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	
	@Column(name="vendor_id")
	private int vendorId;
	
	@Column(name="payable_id")
	private long payableId;
	
	@Column(name="cart_id")
	private long cartId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public int getVendorId() {
		return vendorId;
	}

	public void setVendorId(int vendorId) {
		this.vendorId = vendorId;
	}

	public long getPayableId() {
		return payableId;
	}

	public void setPayableId(long payableId) {
		this.payableId = payableId;
	}

	public long getCartId() {
		return cartId;
	}

	public void setCartId(long cartId) {
		this.cartId = cartId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (int) (cartId ^ (cartId >>> 32));
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + createdBy;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + (int) (payableId ^ (payableId >>> 32));
		result = prime * result + vendorId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PurchasePayment other = (PurchasePayment) obj;
		if (Double.doubleToLongBits(amount) != Double.doubleToLongBits(other.amount))
			return false;
		if (cartId != other.cartId)
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (createdBy != other.createdBy)
			return false;
		if (id != other.id)
			return false;
		if (payableId != other.payableId)
			return false;
		if (vendorId != other.vendorId)
			return false;
		return true;
	}
	
}

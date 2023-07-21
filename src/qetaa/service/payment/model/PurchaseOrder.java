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

@Table(name="pay_purchase_order")
@Entity
public class PurchaseOrder implements Serializable{
	private static final long serialVersionUID = 1L;
	@Column(name="id")
	@Id
	@SequenceGenerator(name = "pay_purchase_order_id_seq_gen", sequenceName = "pay_purchase_order_id_seq", initialValue=1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pay_purchase_order_id_seq_gen")
	private int id;
	
	@Column(name="cart_id")
	private long cartId;
	
	@Column(name="amount")
	private double amount;
	
	@Column(name="type")
	private char type;//T = Credit, C = Cash
	
	@Column(name="status")
	private char status;
	
	@Column(name="created")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	
	@Column(name="vendor_id")
	private int vendorId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getCartId() {
		return cartId;
	}

	public void setCartId(long cartId) {
		this.cartId = cartId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (int) (cartId ^ (cartId >>> 32));
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + id;
		result = prime * result + status;
		result = prime * result + type;
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
		PurchaseOrder other = (PurchaseOrder) obj;
		if (Double.doubleToLongBits(amount) != Double.doubleToLongBits(other.amount))
			return false;
		if (cartId != other.cartId)
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (id != other.id)
			return false;
		if (status != other.status)
			return false;
		if (type != other.type)
			return false;
		if (vendorId != other.vendorId)
			return false;
		return true;
	}
	
	
	
}

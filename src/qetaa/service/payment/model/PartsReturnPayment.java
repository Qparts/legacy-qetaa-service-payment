package qetaa.service.payment.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Table(name="pay_parts_return_payment")
@Entity
public class PartsReturnPayment implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "pay_parts_return_payment_id_seq_gen", sequenceName = "pay_parts_return_payment_id_seq", initialValue=1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pay_parts_return_payment_id_seq_gen")
	@Column(name="id")
	private long id;
	@Column(name="customer_id")
	private long customerId;
	@Column(name="cart_id")
	private long cartId;
	@Column(name="status")
	private char status;//P = paid, F = failed
	@Column(name="created_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	@Column(name="amount")
	private int amount;//in halalas
	//for wire transfers
	@JoinColumn(name="bank_id")
	@ManyToOne
	private Bank bank;
	@Column(name="return_by")
	private Integer returnBy;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public long getCartId() {
		return cartId;
	}
	public void setCartId(long cartId) {
		this.cartId = cartId;
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
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public Bank getBank() {
		return bank;
	}
	public void setBank(Bank bank) {
		this.bank = bank;
	}
	public Integer getReturnBy() {
		return returnBy;
	}
	public void setReturnBy(Integer returnBy) {
		this.returnBy = returnBy;
	}
	
	
	
	
	
}



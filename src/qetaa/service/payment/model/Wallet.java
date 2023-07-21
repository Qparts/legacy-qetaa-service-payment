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

@Entity
@Table(name="pay_wallet")
public class Wallet implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "pay_wallet_id_seq_gen", sequenceName = "pay_wallet_id_seq", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pay_wallet_id_seq_gen")
	@Column(name = "id")
	private long id;
	
	@Column(name="wallet_type")
	private char walletType;//Payment , Refund
	
	@Column(name = "customer_id")
	private long customerId;
	
	@Column(name = "customer_name")
	private String customerName;
	
	@Column(name = "cart_id")
	private long cartId;
		
	@Column(name = "status")
	private char status;//A = Awaiting sales, S = Sales Made 	
	
	@Column(name = "created")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	
	@Column(name = "gateway")
	private String gateway;//Moyassar
	
	@Column(name = "transaction_id")
	private String transactionId;//
	
	@Column(name = "currency")
	private String currency;
	
	@Column(name = "payment_type")
	private String paymentType;// creditcard, sadad, wire transfer

	@Column(name = "amount")
	private double amount;//not in halalas
	
	@Column(name = "cc_company")
	private String ccCompany;// visa, mastercard
	@JoinColumn(name = "bank_id")
	@ManyToOne
	private Bank bank;
	
	@Column(name = "bank_confirmed_by")
	private Integer bankConfirmedBy;//user
	
	@Column(name = "credit_fees")
	private Double creditFees;//credit fees

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public char getWalletType() {
		return walletType;
	}

	public void setWalletType(char walletType) {
		this.walletType = walletType;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getCcCompany() {
		return ccCompany;
	}

	public void setCcCompany(String ccCompany) {
		this.ccCompany = ccCompany;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public Integer getBankConfirmedBy() {
		return bankConfirmedBy;
	}

	public void setBankConfirmedBy(Integer bankConfirmedBy) {
		this.bankConfirmedBy = bankConfirmedBy;
	}

	public Double getCreditFees() {
		return creditFees;
	}

	public void setCreditFees(Double creditFees) {
		this.creditFees = creditFees;
	}
	
}

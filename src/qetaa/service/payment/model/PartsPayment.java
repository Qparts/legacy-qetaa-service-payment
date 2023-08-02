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

@Table(name = "pay_parts_payment")
@Entity
public class PartsPayment implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "pay_parts_payment_id_seq_gen", sequenceName = "pay_parts_payment_id_seq", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pay_parts_payment_id_seq_gen")
	@Column(name = "id")
	private long id;
	@Column(name="wallet_type")
	private char walletType;//P = Payment, R = Refund
	@Column(name="wallet_status")
	private char walletStatus;//F = finished, U = under process
	@Column(name = "customer_id")
	private long customerId;
	@Column(name = "cart_id")
	private long cartId;
	@Column(name = "status")
	private char status;// P = paid, F = failed
	@Column(name = "transaction_index")
	private int index;// payment index
	@Column(name = "created_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	@Column(name = "provider")
	private String provider;
	@Column(name = "transaction_id")
	private String transactionId;
	@Column(name = "currency")
	private String currency;
	@Column(name = "type")
	private String paymentType;// creditcard, sadad, wire transfer
	@Column(name = "amount")
	private int amount;// in halalas
	@Column(name = "company")
	private String company;// visa, mastercard
	@Column(name = "card_holder_name")
	private String holderName;// holder name
	@Column(name = "card_masked_number")
	private String maskedNumber;
	// for wire transfers
	@JoinColumn(name = "bank_id")
	@ManyToOne
	private Bank bank;
	@Column(name = "bank_confirmed_by")
	private Integer bankConfirmedBy;
	@Column(name = "credit_fees")
	private Double creditFees;

	public Double getCreditFees() {
		return creditFees;
	}

	public void setCreditFees(Double creditFees) {
		this.creditFees = creditFees;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setPaymentId(long paymentId) {
		this.id = paymentId;
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

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
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

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getHolderName() {
		return holderName;
	}

	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}

	public String getMaskedNumber() {
		return maskedNumber;
	}

	public void setMaskedNumber(String maskedNumber) {
		this.maskedNumber = maskedNumber;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + amount;
		result = prime * result + ((bank == null) ? 0 : bank.hashCode());
		result = prime * result + ((bankConfirmedBy == null) ? 0 : bankConfirmedBy.hashCode());
		result = prime * result + (int) (cartId ^ (cartId >>> 32));
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + (int) (customerId ^ (customerId >>> 32));
		result = prime * result + ((holderName == null) ? 0 : holderName.hashCode());
		result = prime * result + index;
		result = prime * result + ((maskedNumber == null) ? 0 : maskedNumber.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((paymentType == null) ? 0 : paymentType.hashCode());
		result = prime * result + ((provider == null) ? 0 : provider.hashCode());
		result = prime * result + status;
		result = prime * result + ((transactionId == null) ? 0 : transactionId.hashCode());
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
		PartsPayment other = (PartsPayment) obj;
		if (amount != other.amount)
			return false;
		if (bank == null) {
			if (other.bank != null)
				return false;
		} else if (!bank.equals(other.bank))
			return false;
		if (bankConfirmedBy == null) {
			if (other.bankConfirmedBy != null)
				return false;
		} else if (!bankConfirmedBy.equals(other.bankConfirmedBy))
			return false;
		if (cartId != other.cartId)
			return false;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (currency == null) {
			if (other.currency != null)
				return false;
		} else if (!currency.equals(other.currency))
			return false;
		if (customerId != other.customerId)
			return false;
		if (holderName == null) {
			if (other.holderName != null)
				return false;
		} else if (!holderName.equals(other.holderName))
			return false;
		if (index != other.index)
			return false;
		if (maskedNumber == null) {
			if (other.maskedNumber != null)
				return false;
		} else if (!maskedNumber.equals(other.maskedNumber))
			return false;
		if (id != other.id)
			return false;
		if (paymentType == null) {
			if (other.paymentType != null)
				return false;
		} else if (!paymentType.equals(other.paymentType))
			return false;
		if (provider == null) {
			if (other.provider != null)
				return false;
		} else if (!provider.equals(other.provider))
			return false;
		if (status != other.status)
			return false;
		if (transactionId == null) {
			if (other.transactionId != null)
				return false;
		} else if (!transactionId.equals(other.transactionId))
			return false;
		return true;
	}

}

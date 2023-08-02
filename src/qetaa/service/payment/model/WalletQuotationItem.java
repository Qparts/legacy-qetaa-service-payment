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

@Entity
@Table(name="pay_wallet_quotation_item")
public class WalletQuotationItem implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "pay_wallet_quotation_item_id_seq_gen", sequenceName = "pay_wallet_quotation_item_id_seq", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pay_wallet_quotation_item_id_seq_gen")
	@Column(name = "id")
	private long id;
	@Column(name = "wallet_id")
	private long walletId;
	@Column(name="wallet_quotation_id")
	private long walletQuotationId;
	@Column(name="wallet_item_id")
	private long walletItemId;
	@Column(name = "created")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	
	
	
	public long getWalletQuotationId() {
		return walletQuotationId;
	}
	public void setWalletQuotationId(long walletQuotationId) {
		this.walletQuotationId = walletQuotationId;
	}
	public long getWalletItemId() {
		return walletItemId;
	}
	public void setWalletItemId(long walletItemId) {
		this.walletItemId = walletItemId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getWalletId() {
		return walletId;
	}
	public void setWalletId(long walletId) {
		this.walletId = walletId;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	
	
}

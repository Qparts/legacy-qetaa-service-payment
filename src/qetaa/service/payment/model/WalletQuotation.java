package qetaa.service.payment.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name="pay_wallet_quotation")
public class WalletQuotation implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "pay_wallet_quotation_id_seq_gen", sequenceName = "pay_wallet_quotation_id_seq", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pay_wallet_quotation_id_seq_gen")
	@Column(name = "id")
	private long id;
	@Column(name = "wallet_id")
	private long walletId;
	@Column(name = "created")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	@Column(name="created_by")
	private int createdBy;
	@Transient
	private List<WalletQuotationItem> walletQuotationItems;
	
	public List<WalletQuotationItem> getWalletQuotationItems() {
		return walletQuotationItems;
	}
	public void setWalletQuotationItems(List<WalletQuotationItem> walletQuotationItems) {
		this.walletQuotationItems = walletQuotationItems;
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
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	
	
}

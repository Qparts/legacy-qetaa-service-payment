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

@Table(name="pay_wallet_item_vendor")
@Entity
public class WalletItemVendor implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "pay_wallet_item_vendor_id_seq_gen", sequenceName = "pay_wallet_item_vendor_id_seq", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pay_wallet_item_vendor_id_seq_gen")
	@Column(name = "id")
	private long id;
	@Column(name="wallet_item_id")
	private long walletItemId;
	@Column(name="vendor_id")
	private Integer vendorId;
	@Column(name="cost")
	private Double cost;
	@Column(name="created_by")
	private Integer createdBy;
	@Column(name="created")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getWalletItemId() {
		return walletItemId;
	}
	public void setWalletItemId(long walletItemId) {
		this.walletItemId = walletItemId;
	}
	public Integer getVendorId() {
		return vendorId;
	}
	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}
	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	
}

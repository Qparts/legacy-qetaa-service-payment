package qetaa.service.payment.model.shipment;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import qetaa.service.payment.model.WalletItem;

@Entity
@Table(name="pay_shipment_item")
public class ShipmentItem implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Column(name="id")
	@Id
	@SequenceGenerator(name = "pay_shipment_item_id_seq_gen", sequenceName = "pay_shipment_item_id_seq", initialValue=1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pay_shipment_item_id_seq_gen")
	private long id;
	
	@Column(name="shipment_id")
	private long shipmentId;
	
	@Column(name="quantity")
	private int quantity;
	
	@Column(name="wallet_item_id")
	private long walletItemId;
	
	@Column(name="status")
	private char status;
	
	@Column(name="collected_by")
	private Integer collectedBy;

	@Column(name="collected")
	@Temporal(TemporalType.TIMESTAMP)
	private Date collected;
	
	@Column(name="shipped_by")
	private int shippedBy;
	
	@Column(name="shipped")
	@Temporal(TemporalType.TIMESTAMP)
	private Date shipped;
	
	@JoinColumn(name="wallet_item_id", insertable=false, updatable=false)
	@OneToOne()
	private WalletItem walletItem;
	
	

	public WalletItem getWalletItem() {
		return walletItem;
	}

	public void setWalletItem(WalletItem walletItem) {
		this.walletItem = walletItem;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getShipmentId() {
		return shipmentId;
	}

	public void setShipmentId(long shipmentId) {
		this.shipmentId = shipmentId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public long getWalletItemId() {
		return walletItemId;
	}

	public void setWalletItemId(long walletItemId) {
		this.walletItemId = walletItemId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Integer getCollectedBy() {
		return collectedBy;
	}

	public void setCollectedBy(Integer collectedBy) {
		this.collectedBy = collectedBy;
	}

	public Date getCollected() {
		return collected;
	}

	public void setCollected(Date collected) {
		this.collected = collected;
	}

	public int getShippedBy() {
		return shippedBy;
	}

	public void setShippedBy(int shippedBy) {
		this.shippedBy = shippedBy;
	}

	public Date getShipped() {
		return shipped;
	}

	public void setShipped(Date shipped) {
		this.shipped = shipped;
	}	
	
}

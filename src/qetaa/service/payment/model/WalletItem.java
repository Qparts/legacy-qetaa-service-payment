package qetaa.service.payment.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name="pay_wallet_item")
@Entity
public class WalletItem implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "pay_wallet_item_id_seq_gen", sequenceName = "pay_wallet_item_id_seq", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pay_wallet_item_id_seq_gen")
	@Column(name = "id")
	private long id;
	@Column(name="wallet_id")
	private long walletId;
	@Column(name="product_id")
	private Long productId;
	@Column(name="item_type")
	private char itemType;
	@Column(name="item_number")
	private String itemNumber;
	@Column(name="item_desc")
	private String itemDesc;
	@Column(name="quantity")
	private int quantity;
	@Column(name="cart_id")
	private long cartId;
	@Column(name="vendor_id")
	private Integer vendorId;
	@Column(name="unit_sales")
	private double unitSales;
	@Column(name="unit_sales_wv")
	private double unitSalesWv;
	@Column(name="unit_sales_net")
	private double unitSalesNet;
	@Column(name="unit_sales_net_wv")
	private double unitSalesNetWv;
	@Column(name="unit_quoted_cost")
	private double unitQuotedCost;
	@Column(name="unit_quoted_cost_wv")
	private double unitQuotedCostWv;
	@Column(name="status")
	private char status;//A = awaiting, R = Refunded, P = Purchased, S = Sold, H= shipped
	@Column(name="refunded_item_id")
	private Long refundedItemId;
	@Column(name="refund_note")
	private String refundNote;
	@Column(name="purchased_item_id")
	private Long purchasedItemId;
	@Column(name="sold_item_id")
	private Long soldItemId;
	@Transient
	private List<WalletItemVendor> walletItemVendors;
	
	
	
	public List<WalletItemVendor> getWalletItemVendors() {
		return walletItemVendors;
	}
	public void setWalletItemVendors(List<WalletItemVendor> walletItemVendors) {
		this.walletItemVendors = walletItemVendors;
	}
	public Long getSoldItemId() {
		return soldItemId;
	}
	public void setSoldItemId(Long soldItemId) {
		this.soldItemId = soldItemId;
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
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public char getItemType() {
		return itemType;
	}
	public void setItemType(char itemType) {
		this.itemType = itemType;
	}
	public String getItemNumber() {
		return itemNumber;
	}
	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	public long getCartId() {
		return cartId;
	}
	public void setCartId(long cartId) {
		this.cartId = cartId;
	}
	public Integer getVendorId() {
		return vendorId;
	}
	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}
	public double getUnitSales() {
		return unitSales;
	}
	public void setUnitSales(double unitSales) {
		this.unitSales = unitSales;
	}
	public double getUnitSalesWv() {
		return unitSalesWv;
	}
	public void setUnitSalesWv(double unitSalesWv) {
		this.unitSalesWv = unitSalesWv;
	}
	public double getUnitSalesNet() {
		return unitSalesNet;
	}
	public void setUnitSalesNet(double unitSalesNet) {
		this.unitSalesNet = unitSalesNet;
	}
	public double getUnitSalesNetWv() {
		return unitSalesNetWv;
	}
	public void setUnitSalesNetWv(double unitSalesNetWv) {
		this.unitSalesNetWv = unitSalesNetWv;
	}
	public double getUnitQuotedCost() {
		return unitQuotedCost;
	}
	public void setUnitQuotedCost(double unitQuotedCost) {
		this.unitQuotedCost = unitQuotedCost;
	}
	public double getUnitQuotedCostWv() {
		return unitQuotedCostWv;
	}
	public void setUnitQuotedCostWv(double unitQuotedCostWv) {
		this.unitQuotedCostWv = unitQuotedCostWv;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	public Long getRefundedItemId() {
		return refundedItemId;
	}
	public void setRefundedItemId(Long refundedItemId) {
		this.refundedItemId = refundedItemId;
	}
	public String getRefundNote() {
		return refundNote;
	}
	public void setRefundNote(String refundNote) {
		this.refundNote = refundNote;
	}
	public Long getPurchasedItemId() {
		return purchasedItemId;
	}
	public void setPurchasedItemId(Long purchasedItemId) {
		this.purchasedItemId = purchasedItemId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	
}

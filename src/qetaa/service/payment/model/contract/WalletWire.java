package qetaa.service.payment.model.contract;

import java.io.Serializable;

import qetaa.service.payment.model.Wallet;

public class WalletWire implements Serializable{

	private static final long serialVersionUID = 1L;
	private Wallet wallet;
	private Long wireId;
	private Long originalWalletId;
	
	

	public Long getOriginalWalletId() {
		return originalWalletId;
	}
	public void setOriginalWalletId(Long originalWalletId) {
		this.originalWalletId = originalWalletId;
	}
	public Wallet getWallet() {
		return wallet;
	}
	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}
	public Long getWireId() {
		return wireId;
	}
	public void setWireId(Long wireId) {
		this.wireId = wireId;
	}
	
}

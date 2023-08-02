package qetaa.service.payment.model.contract;

import java.io.Serializable;
import java.util.Date;

public class Courier implements Serializable{

	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String nameAr;
	private boolean trackable;
	private char customerStatus;
	private char internalStatus;
	private Date created;
	private int createdBy;
	private String trackLink;
	
	
	public String getTrackLink() {
		return trackLink;
	}
	public void setTrackLink(String trackLink) {
		this.trackLink = trackLink;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNameAr() {
		return nameAr;
	}
	public void setNameAr(String nameAr) {
		this.nameAr = nameAr;
	}
	public boolean isTrackable() {
		return trackable;
	}
	public void setTrackable(boolean trackable) {
		this.trackable = trackable;
	}
	public char getCustomerStatus() {
		return customerStatus;
	}
	public void setCustomerStatus(char customerStatus) {
		this.customerStatus = customerStatus;
	}
	public char getInternalStatus() {
		return internalStatus;
	}
	public void setInternalStatus(char internalStatus) {
		this.internalStatus = internalStatus;
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

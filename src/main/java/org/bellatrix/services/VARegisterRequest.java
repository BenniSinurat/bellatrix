package org.bellatrix.services;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.bellatrix.data.BillerServiceField;

public class VARegisterRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5154537126272102778L;
	private String referenceNumber;
	private String eventID;
	private String username;
	private String fromMember;
	private String description;
	private Integer bankID;
	private BigDecimal amount;
	private BigDecimal minimumPayment;
	private String name;
	private String email;
	private String mobileNo;
	private boolean fullPayment;
	private boolean persistent;
	private List<BillerServiceField> privateField;
	private Date expiredDateTime;
	private String callbackURL;

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isFullPayment() {
		return fullPayment;
	}

	public void setFullPayment(boolean fullPayment) {
		this.fullPayment = fullPayment;
	}

	public boolean isPersistent() {
		return persistent;
	}

	public void setPersistent(boolean persistent) {
		this.persistent = persistent;
	}

	public String getCallbackURL() {
		return callbackURL;
	}

	public void setCallbackURL(String callbackURL) {
		this.callbackURL = callbackURL;
	}

	public Integer getBankID() {
		return bankID;
	}

	public void setBankID(Integer bankID) {
		this.bankID = bankID;
	}

	public BigDecimal getMinimumPayment() {
		return minimumPayment;
	}

	public void setMinimumPayment(BigDecimal minimumPayment) {
		this.minimumPayment = minimumPayment;
	}

	public Date getExpiredDateTime() {
		return expiredDateTime;
	}

	public void setExpiredDateTime(Date expiredDateTime) {
		this.expiredDateTime = expiredDateTime;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEventID() {
		return eventID;
	}

	public void setEventID(String eventID) {
		this.eventID = eventID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<BillerServiceField> getPrivateField() {
		return privateField;
	}

	public void setPrivateField(List<BillerServiceField> privateField) {
		this.privateField = privateField;
	}

	public String getFromMember() {
		return fromMember;
	}

	public void setFromMember(String fromMember) {
		this.fromMember = fromMember;
	}

}

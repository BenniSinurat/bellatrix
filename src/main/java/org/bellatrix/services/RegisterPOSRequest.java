package org.bellatrix.services;

import java.math.BigDecimal;

public class RegisterPOSRequest {

	private String username;
	private Integer transferTypeID;
	private String name;
	private String address;
	private String city;
	private String pic;
	private String email;
	private String msisdn;
	private String postalCode;
	private Boolean openPayment;
	private Boolean fixedAmount;
	private BigDecimal amount;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public Boolean getOpenPayment() {
		return openPayment;
	}

	public void setOpenPayment(Boolean openPayment) {
		this.openPayment = openPayment;
	}

	public Boolean getFixedAmount() {
		return fixedAmount;
	}

	public void setFixedAmount(Boolean fixedAmount) {
		this.fixedAmount = fixedAmount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getTransferTypeID() {
		return transferTypeID;
	}

	public void setTransferTypeID(Integer transferTypeID) {
		this.transferTypeID = transferTypeID;
	}

}

package org.bellatrix.data;

import java.math.BigDecimal;

public class Terminal {

	private Integer id;
	private Members toMember;
	private Integer transferTypeID;
	private String name;
	private String pic;
	private String msisdn;
	private String email;
	private String address;
	private String city;
	private String postalCode;
	private boolean openPayment;
	private boolean fixedAmount;
	private BigDecimal amount;
	
	private POSAcquiring posAcquiring;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public boolean isOpenPayment() {
		return openPayment;
	}

	public void setOpenPayment(boolean openPayment) {
		this.openPayment = openPayment;
	}

	public Integer getTransferTypeID() {
		return transferTypeID;
	}

	public void setTransferTypeID(Integer transferTypeID) {
		this.transferTypeID = transferTypeID;
	}

	public Members getToMember() {
		return toMember;
	}

	public void setToMember(Members toMember) {
		this.toMember = toMember;
	}

	public boolean isFixedAmount() {
		return fixedAmount;
	}

	public void setFixedAmount(boolean fixedAmount) {
		this.fixedAmount = fixedAmount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public POSAcquiring getPosAcquiring() {
		return posAcquiring;
	}

	public void setPosAcquiring(POSAcquiring posAcquiring) {
		this.posAcquiring = posAcquiring;
	}

}

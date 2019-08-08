package org.bellatrix.data;

import java.math.BigDecimal;

public class TerminalView {

	private Integer id;
	private String name;
	private String address;
	private String city;
	private String postalCode;
	private Boolean openPayment;
	private Boolean fixedAmount;
	private BigDecimal amount;
	private String email;
	private String msisdn;
	private String pic;

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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}
}

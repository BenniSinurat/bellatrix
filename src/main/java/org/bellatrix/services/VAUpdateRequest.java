package org.bellatrix.services;

import java.io.Serializable;
import java.math.BigDecimal;

public class VAUpdateRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 477339556502665251L;
	private String paymentCode;
	private String username;
	private BigDecimal amount;
	private BigDecimal minimumPayment;
	private String name;
	private Boolean fullPayment;
	private Boolean persistent;
	private Long expiredAtMinute;
	private String callbackURL;

	private String msisdn;
	private String email;
	private String description;
	
	private String status;

	public String getPaymentCode() {
		return paymentCode;
	}

	public void setPaymentCode(String paymentCode) {
		this.paymentCode = paymentCode;
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

	public BigDecimal getMinimumPayment() {
		return minimumPayment;
	}

	public void setMinimumPayment(BigDecimal minimumPayment) {
		this.minimumPayment = minimumPayment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean isFullPayment() {
		return fullPayment;
	}

	public void setFullPayment(Boolean fullPayment) {
		this.fullPayment = fullPayment;
	}

	public Boolean isPersistent() {
		return persistent;
	}

	public void setPersistent(Boolean persistent) {
		this.persistent = persistent;
	}

	public Long getExpiredAtMinute() {
		return expiredAtMinute;
	}

	public void setExpiredAtMinute(Long expiredAtMinute) {
		this.expiredAtMinute = expiredAtMinute;
	}

	public String getCallbackURL() {
		return callbackURL;
	}

	public void setCallbackURL(String callbackURL) {
		this.callbackURL = callbackURL;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}

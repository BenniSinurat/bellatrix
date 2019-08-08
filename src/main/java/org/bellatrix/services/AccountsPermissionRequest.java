package org.bellatrix.services;

import java.math.BigDecimal;

public class AccountsPermissionRequest {
	private Integer id;
	private Integer accountID;
	private Integer groupID;
	private BigDecimal creditLimit;
	private BigDecimal upperCreditLimit;
	private BigDecimal lowerCreditLimit;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAccountID() {
		return accountID;
	}

	public void setAccountID(Integer accountID) {
		this.accountID = accountID;
	}

	public Integer getGroupID() {
		return groupID;
	}

	public void setGroupID(Integer groupID) {
		this.groupID = groupID;
	}

	public BigDecimal getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(BigDecimal creditLimit) {
		this.creditLimit = creditLimit;
	}

	public BigDecimal getUpperCreditLimit() {
		return upperCreditLimit;
	}

	public void setUpperCreditLimit(BigDecimal upperCreditLimit) {
		this.upperCreditLimit = upperCreditLimit;
	}

	public BigDecimal getLowerCreditLimit() {
		return lowerCreditLimit;
	}

	public void setLowerCreditLimit(BigDecimal lowerCreditLimit) {
		this.lowerCreditLimit = lowerCreditLimit;
	}

}

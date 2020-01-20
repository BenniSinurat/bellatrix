package org.bellatrix.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Accounts implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2489653034882245588L;
	private Integer id;
	private Date createdDate;
	private String formattedCreatedDate;
	private String name;
	private String description;
	private boolean systemAccount;
	private BigDecimal creditLimit;
	private BigDecimal upperCreditLimit;
	private BigDecimal lowerCreditLimit;
	private Currencies currency;
	private Groups group;
	private String formattedCreditLimit;
	private String formattedUpperCreditLimit;
	private String formattedLowerCreditLimit;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public boolean isSystemAccount() {
		return systemAccount;
	}

	public void setSystemAccount(boolean systemAccount) {
		this.systemAccount = systemAccount;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getFormattedCreatedDate() {
		return formattedCreatedDate;
	}

	public void setFormattedCreatedDate(String formattedCreatedDate) {
		this.formattedCreatedDate = formattedCreatedDate;
	}

	public Currencies getCurrency() {
		return currency;
	}

	public void setCurrency(Currencies currency) {
		this.currency = currency;
	}

	public Groups getGroup() {
		return group;
	}

	public void setGroup(Groups group) {
		this.group = group;
	}

	public String getFormattedCreditLimit() {
		return formattedCreditLimit;
	}

	public void setFormattedCreditLimit(String formattedCreditLimit) {
		this.formattedCreditLimit = formattedCreditLimit;
	}

	public String getFormattedUpperCreditLimit() {
		return formattedUpperCreditLimit;
	}

	public void setFormattedUpperCreditLimit(String formattedUpperCreditLimit) {
		this.formattedUpperCreditLimit = formattedUpperCreditLimit;
	}

	public String getFormattedLowerCreditLimit() {
		return formattedLowerCreditLimit;
	}

	public void setFormattedLowerCreditLimit(String formattedLowerCreditLimit) {
		this.formattedLowerCreditLimit = formattedLowerCreditLimit;
	}

}

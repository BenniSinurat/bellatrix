package org.bellatrix.data;

import java.io.Serializable;
import java.math.BigDecimal;

public class TransferTypes implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3151992666472222098L;
	private Integer id;
	private Integer fromAccounts;
	private Integer toAccounts;
	private String fromAccountName;
	private String toAccountName;
	private String name;
	private String description;
	private BigDecimal minAmount;
	private BigDecimal maxAmount;
	private Integer maxCount;

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

	public BigDecimal getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(BigDecimal minAmount) {
		this.minAmount = minAmount;
	}

	public BigDecimal getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(BigDecimal maxAmount) {
		this.maxAmount = maxAmount;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFromAccounts() {
		return fromAccounts;
	}

	public void setFromAccounts(Integer fromAccounts) {
		this.fromAccounts = fromAccounts;
	}

	public Integer getToAccounts() {
		return toAccounts;
	}

	public void setToAccounts(Integer toAccounts) {
		this.toAccounts = toAccounts;
	}

	public String getFromAccountName() {
		return fromAccountName;
	}

	public void setFromAccountName(String fromAccountName) {
		this.fromAccountName = fromAccountName;
	}

	public String getToAccountName() {
		return toAccountName;
	}

	public void setToAccountName(String toAccountName) {
		this.toAccountName = toAccountName;
	}

	public Integer getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(Integer maxCount) {
		this.maxCount = maxCount;
	}

}

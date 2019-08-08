package org.bellatrix.services;

import java.math.BigDecimal;
import java.util.Date;

public class FeeRequest {

	private Integer transferTypeID;
	private Integer fromMemberID;
	private Integer fromAccountID;
	private Integer toMemberID;
	private Integer toAccountID;
	private String name;
	private String description;
	private boolean enabled;
	private boolean deductAmount;
	private boolean fromAllGroup;
	private boolean toAllGroup;
	private BigDecimal fixedAmount;
	private double percentage;
	private BigDecimal initialRangeAmount;
	private BigDecimal maxRangeAmount;
	private Date startDate;
	private Date endDate;

	public Integer getTransferTypeID() {
		return transferTypeID;
	}

	public void setTransferTypeID(Integer transferTypeID) {
		this.transferTypeID = transferTypeID;
	}

	public Integer getFromMemberID() {
		return fromMemberID;
	}

	public void setFromMemberID(Integer fromMemberID) {
		this.fromMemberID = fromMemberID;
	}

	public Integer getFromAccountID() {
		return fromAccountID;
	}

	public void setFromAccountID(Integer fromAccountID) {
		this.fromAccountID = fromAccountID;
	}

	public Integer getToMemberID() {
		return toMemberID;
	}

	public void setToMemberID(Integer toMemberID) {
		this.toMemberID = toMemberID;
	}

	public Integer getToAccountID() {
		return toAccountID;
	}

	public void setToAccountID(Integer toAccountID) {
		this.toAccountID = toAccountID;
	}

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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isDeductAmount() {
		return deductAmount;
	}

	public void setDeductAmount(boolean deductAmount) {
		this.deductAmount = deductAmount;
	}

	public boolean isFromAllGroup() {
		return fromAllGroup;
	}

	public void setFromAllGroup(boolean fromAllGroup) {
		this.fromAllGroup = fromAllGroup;
	}

	public boolean isToAllGroup() {
		return toAllGroup;
	}

	public void setToAllGroup(boolean toAllGroup) {
		this.toAllGroup = toAllGroup;
	}

	public BigDecimal getFixedAmount() {
		return fixedAmount;
	}

	public void setFixedAmount(BigDecimal fixedAmount) {
		this.fixedAmount = fixedAmount;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public BigDecimal getInitialRangeAmount() {
		return initialRangeAmount;
	}

	public void setInitialRangeAmount(BigDecimal initialRangeAmount) {
		this.initialRangeAmount = initialRangeAmount;
	}

	public BigDecimal getMaxRangeAmount() {
		return maxRangeAmount;
	}

	public void setMaxRangeAmount(BigDecimal maxRangeAmount) {
		this.maxRangeAmount = maxRangeAmount;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}

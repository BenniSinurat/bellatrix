package org.bellatrix.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Fees implements Serializable {

	private static final long serialVersionUID = -2377898982801131818L;
	private Integer id;
	private Integer transferTypeID;
	private Integer fromMemberID;
	private Members fromMember;
	private Integer toMemberID;
	private Members toMember;
	private Integer fromAccountID;
	private Integer toAccountID;
	private String name;
	private String description;
	private boolean enabled;
	private boolean deductAmount;
	private boolean fromAllGroup;
	private boolean toAllGroup;
	private BigDecimal fixedAmount;
	private BigDecimal percentageValue;
	private BigDecimal initialRangeAmount;
	private BigDecimal maximumRangeAmount;
	private BigDecimal feeAmount;
	private String transactionNumber;
	private Date startDate;
	private Date endDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public Integer getToMemberID() {
		return toMemberID;
	}

	public void setToMemberID(Integer toMemberID) {
		this.toMemberID = toMemberID;
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

	public BigDecimal getFixedAmount() {
		return fixedAmount;
	}

	public void setFixedAmount(BigDecimal fixedAmount) {
		this.fixedAmount = fixedAmount;
	}

	public BigDecimal getPercentageValue() {
		return percentageValue;
	}

	public void setPercentageValue(BigDecimal percentageValue) {
		this.percentageValue = percentageValue;
	}

	public BigDecimal getInitialRangeAmount() {
		return initialRangeAmount;
	}

	public void setInitialRangeAmount(BigDecimal initialRangeAmount) {
		this.initialRangeAmount = initialRangeAmount;
	}

	public BigDecimal getMaximumRangeAmount() {
		return maximumRangeAmount;
	}

	public void setMaximumRangeAmount(BigDecimal maximumRangeAmount) {
		this.maximumRangeAmount = maximumRangeAmount;
	}

	public Integer getFromAccountID() {
		return fromAccountID;
	}

	public void setFromAccountID(Integer fromAccountID) {
		this.fromAccountID = fromAccountID;
	}

	public Integer getToAccountID() {
		return toAccountID;
	}

	public void setToAccountID(Integer toAccountID) {
		this.toAccountID = toAccountID;
	}

	public BigDecimal getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(BigDecimal feeAmount) {
		this.feeAmount = feeAmount;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
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

	public Members getFromMember() {
		return fromMember;
	}

	public void setFromMember(Members fromMember) {
		this.fromMember = fromMember;
	}

	public Members getToMember() {
		return toMember;
	}

	public void setToMember(Members toMember) {
		this.toMember = toMember;
	}

}

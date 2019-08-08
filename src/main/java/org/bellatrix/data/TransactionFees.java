package org.bellatrix.data;

import java.math.BigDecimal;

public class TransactionFees {

	private String id;
	private String name;
	private String description;
	private boolean enabled;
	private boolean deductAmount;
	private String fromMemberID;
	private String toMemberID;
	//private FeeSchedulerDAO feeScheduler;
	private BigDecimal fixedAmount;
	private BigDecimal percentageValue;
	private BigDecimal initialRangeAmount;
	private BigDecimal finalRangeAmount;

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

	public String getFromMemberID() {
		return fromMemberID;
	}

	public void setFromMemberID(String fromMemberID) {
		this.fromMemberID = fromMemberID;
	}

	public String getToMemberID() {
		return toMemberID;
	}

	public void setToMemberID(String toMemberID) {
		this.toMemberID = toMemberID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public BigDecimal getFinalRangeAmount() {
		return finalRangeAmount;
	}

	public void setFinalRangeAmount(BigDecimal finalRangeAmount) {
		this.finalRangeAmount = finalRangeAmount;
	}


}

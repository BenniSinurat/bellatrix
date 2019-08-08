package org.bellatrix.data;

import java.io.Serializable;
import java.math.BigDecimal;

public class BankTransferRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3063615505929864868L;
	private Members fromMember;
	private String toAccountNumber;
	private String toAccountName;
	private String toResidentStatus;
	private String toProfileType;
	private String toEmailAddress;
	private String fromAccountNumber;
	private String fromUsername;
	private String swiftCode;
	private BigDecimal amount;
	private BigDecimal finalAmount;
	private BigDecimal totalFees;
	private BigDecimal transactionAmount;
	private Integer transferTypeID;
	private String description;
	private String remark;
	private String transferMethod;
	private String chargingCode;
	private String traceNumber;
	private String gatewayURL;
	private String bankCode;
	private String bankName;

	public String getToAccountName() {
		return toAccountName;
	}

	public void setToAccountName(String toAccountName) {
		this.toAccountName = toAccountName;
	}

	public String getToResidentStatus() {
		return toResidentStatus;
	}

	public void setToResidentStatus(String toResidentStatus) {
		this.toResidentStatus = toResidentStatus;
	}

	public String getToProfileType() {
		return toProfileType;
	}

	public void setToProfileType(String toProfileType) {
		this.toProfileType = toProfileType;
	}

	public String getToEmailAddress() {
		return toEmailAddress;
	}

	public void setToEmailAddress(String toEmailAddress) {
		this.toEmailAddress = toEmailAddress;
	}

	public String getSwiftCode() {
		return swiftCode;
	}

	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTransferMethod() {
		return transferMethod;
	}

	public void setTransferMethod(String transferMethod) {
		this.transferMethod = transferMethod;
	}

	public String getTraceNumber() {
		return traceNumber;
	}

	public void setTraceNumber(String traceNumber) {
		this.traceNumber = traceNumber;
	}

	public String getGatewayURL() {
		return gatewayURL;
	}

	public void setGatewayURL(String gatewayURL) {
		this.gatewayURL = gatewayURL;
	}

	public String getChargingCode() {
		return chargingCode;
	}

	public void setChargingCode(String chargingCode) {
		this.chargingCode = chargingCode;
	}

	public BigDecimal getFinalAmount() {
		return finalAmount;
	}

	public void setFinalAmount(BigDecimal finalAmount) {
		this.finalAmount = finalAmount;
	}

	public BigDecimal getTotalFees() {
		return totalFees;
	}

	public void setTotalFees(BigDecimal totalFees) {
		this.totalFees = totalFees;
	}

	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getFromAccountNumber() {
		return fromAccountNumber;
	}

	public void setFromAccountNumber(String fromAccountNumber) {
		this.fromAccountNumber = fromAccountNumber;
	}

	public String getToAccountNumber() {
		return toAccountNumber;
	}

	public void setToAccountNumber(String toAccountNumber) {
		this.toAccountNumber = toAccountNumber;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getFromUsername() {
		return fromUsername;
	}

	public void setFromUsername(String fromUsername) {
		this.fromUsername = fromUsername;
	}

	public Integer getTransferTypeID() {
		return transferTypeID;
	}

	public void setTransferTypeID(Integer transferTypeID) {
		this.transferTypeID = transferTypeID;
	}

	public Members getFromMember() {
		return fromMember;
	}

	public void setFromMember(Members fromMember) {
		this.fromMember = fromMember;
	}

}

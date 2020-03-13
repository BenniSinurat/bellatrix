package org.bellatrix.data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class VAStatusRecordView {

	private String id;
	private Date expiredAt;
	private String formattedExpiredAt;
	private Date createdDate;
	private String formattedCreatedDate;
	private String parentUsername;
	private TransferTypes transferType;
	private Integer bankID;
	private String bankCode;
	private String bankName;
	private String formattedAmount;
	private BigDecimal amount;
	private BigDecimal minimumPayment;
	private String name;
	private String referenceNumber;
	private boolean fullPayment;
	private boolean persistent;
	private String callbackURL;
	private String ticketID;
	private String status;
	private Date transactionDate;
	private String formattedTransactionDate;
	private String transactionNumber;
	private String paymentCode;
	private String description;
	private VAEvent vaEvent;
	private List<BillingStatus> billingStatus;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentUsername() {
		return parentUsername;
	}

	public void setParentUsername(String parentUsername) {
		this.parentUsername = parentUsername;
	}

	public TransferTypes getTransferType() {
		return transferType;
	}

	public void setTransferType(TransferTypes transferType) {
		this.transferType = transferType;
	}

	public Integer getBankID() {
		return bankID;
	}

	public void setBankID(Integer bankID) {
		this.bankID = bankID;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
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

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public boolean isFullPayment() {
		return fullPayment;
	}

	public void setFullPayment(boolean fullPayment) {
		this.fullPayment = fullPayment;
	}

	public boolean isPersistent() {
		return persistent;
	}

	public void setPersistent(boolean persistent) {
		this.persistent = persistent;
	}

	public String getCallbackURL() {
		return callbackURL;
	}

	public void setCallbackURL(String callbackURL) {
		this.callbackURL = callbackURL;
	}

	public String getTicketID() {
		return ticketID;
	}

	public void setTicketID(String ticketID) {
		this.ticketID = ticketID;
	}

	public Date getExpiredAt() {
		return expiredAt;
	}

	public void setExpiredAt(Date expiredAt) {
		this.expiredAt = expiredAt;
	}

	public String getFormattedAmount() {
		return formattedAmount;
	}

	public void setFormattedAmount(String formattedAmount) {
		this.formattedAmount = formattedAmount;
	}

	public String getFormattedExpiredAt() {
		return formattedExpiredAt;
	}

	public void setFormattedExpiredAt(String formattedExpiredAt) {
		this.formattedExpiredAt = formattedExpiredAt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getFormattedTransactionDate() {
		return formattedTransactionDate;
	}

	public void setFormattedTransactionDate(String formattedTransactionDate) {
		this.formattedTransactionDate = formattedTransactionDate;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public VAEvent getVaEvent() {
		return vaEvent;
	}

	public void setVaEvent(VAEvent vaEvent) {
		this.vaEvent = vaEvent;
	}

	public String getPaymentCode() {
		return paymentCode;
	}

	public void setPaymentCode(String paymentCode) {
		this.paymentCode = paymentCode;
	}

	public List<BillingStatus> getBillingStatus() {
		return billingStatus;
	}

	public void setBillingStatus(List<BillingStatus> billingStatus) {
		this.billingStatus = billingStatus;
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

	public String getFormattedCreatedDate() {
		return formattedCreatedDate;
	}

	public void setFormattedCreatedDate(String formattedCreatedDate) {
		this.formattedCreatedDate = formattedCreatedDate;
	}

}

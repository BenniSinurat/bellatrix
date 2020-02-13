package org.bellatrix.data;

import java.util.Date;

public class BillingStatus {
	private String status;
	private String referenceNumber;
	private String formattedTransactionDate;
	private Date transactionDate;
	private String transactionNumber;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getFormattedTransactionDate() {
		return formattedTransactionDate;
	}

	public void setFormattedTransactionDate(String formattedTransactionDate) {
		this.formattedTransactionDate = formattedTransactionDate;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

}

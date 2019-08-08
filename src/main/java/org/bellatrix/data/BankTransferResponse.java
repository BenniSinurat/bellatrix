package org.bellatrix.data;

import java.io.Serializable;

public class BankTransferResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1528543941354676182L;
	private String status;
	private String accountName;
	private String referenceNumber;
	private String transactionNumber;
	private boolean onUs;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public boolean isOnUs() {
		return onUs;
	}

	public void setOnUs(boolean onUs) {
		this.onUs = onUs;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

}

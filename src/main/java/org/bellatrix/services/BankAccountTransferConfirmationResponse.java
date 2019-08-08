package org.bellatrix.services;

import org.bellatrix.data.ResponseStatus;

public class BankAccountTransferConfirmationResponse {

	private String referenceNumber;
	private ResponseStatus status;

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}
}

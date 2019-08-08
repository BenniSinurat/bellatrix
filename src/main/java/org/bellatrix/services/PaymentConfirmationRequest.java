package org.bellatrix.services;

import org.bellatrix.data.ResponseStatus;

public class PaymentConfirmationRequest {

	private String requestID;
	private ResponseStatus status;

	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

}

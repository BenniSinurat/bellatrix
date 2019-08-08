package org.bellatrix.services;

import org.bellatrix.data.ResponseStatus;

public class VAUpdateResponse {

	private String paymentCode;
	private ResponseStatus status;
	private String ticketID;

	public String getPaymentCode() {
		return paymentCode;
	}

	public void setPaymentCode(String paymentCode) {
		this.paymentCode = paymentCode;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public String getTicketID() {
		return ticketID;
	}

	public void setTicketID(String ticketID) {
		this.ticketID = ticketID;
	}
}

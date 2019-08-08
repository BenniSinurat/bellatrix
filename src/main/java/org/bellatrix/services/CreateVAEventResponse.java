package org.bellatrix.services;

import org.bellatrix.data.ResponseStatus;

public class CreateVAEventResponse {

	private ResponseStatus status;
	private String ticketID;

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

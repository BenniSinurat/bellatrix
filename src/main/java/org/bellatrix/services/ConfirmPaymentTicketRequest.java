package org.bellatrix.services;

public class ConfirmPaymentTicketRequest {

	private String fromMember;
	private boolean externalID;
	private String ticket;
	private String traceNumber;
	private Integer accessTypeID;
	private String credential;

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getFromMember() {
		return fromMember;
	}

	public void setFromMember(String fromMember) {
		this.fromMember = fromMember;
	}

	public String getTraceNumber() {
		return traceNumber;
	}

	public void setTraceNumber(String traceNumber) {
		this.traceNumber = traceNumber;
	}

	public boolean isExternalID() {
		return externalID;
	}

	public void setExternalID(boolean externalID) {
		this.externalID = externalID;
	}

	public Integer getAccessTypeID() {
		return accessTypeID;
	}

	public void setAccessTypeID(Integer accessTypeID) {
		this.accessTypeID = accessTypeID;
	}

	public String getCredential() {
		return credential;
	}

	public void setCredential(String credential) {
		this.credential = credential;
	}

}

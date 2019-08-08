package org.bellatrix.services;

import java.io.Serializable;

public class PosInquiryRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8162406463201589789L;
	private String fromMember;
	private String toMember;
	private String ticketID;
	private Integer terminalID;

	public String getFromMember() {
		return fromMember;
	}

	public void setFromMember(String fromMember) {
		this.fromMember = fromMember;
	}

	public String getToMember() {
		return toMember;
	}

	public void setToMember(String toMember) {
		this.toMember = toMember;
	}

	public Integer getTerminalID() {
		return terminalID;
	}

	public void setTerminalID(Integer terminalID) {
		this.terminalID = terminalID;
	}

	public String getTicketID() {
		return ticketID;
	}

	public void setTicketID(String ticketID) {
		this.ticketID = ticketID;
	}

}

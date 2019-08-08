package org.bellatrix.services;

import java.io.Serializable;

public class LoadTerminalByIDRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2163482431198958029L;
	private String toMember;
	private Integer terminalID;

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

}

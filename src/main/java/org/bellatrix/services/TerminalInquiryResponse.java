package org.bellatrix.services;

import java.io.Serializable;
import java.util.List;

import org.bellatrix.data.MemberView;
import org.bellatrix.data.ResponseStatus;
import org.bellatrix.data.TerminalView;

public class TerminalInquiryResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8698598506372750976L;
	private MemberView toMember;
	private List<TerminalView> terminal;
	private ResponseStatus status;

	public MemberView getToMember() {
		return toMember;
	}

	public void setToMember(MemberView toMember) {
		this.toMember = toMember;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public List<TerminalView> getTerminal() {
		return terminal;
	}

	public void setTerminal(List<TerminalView> terminal) {
		this.terminal = terminal;
	}

}

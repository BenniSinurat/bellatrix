package org.bellatrix.services;

import java.math.BigDecimal;

import org.bellatrix.data.MemberView;
import org.bellatrix.data.ResponseStatus;
import org.bellatrix.data.TerminalView;

public class PosCreateInvoiceResponse {

	private String invoiceID;
	private BigDecimal amount;
	private MemberView toMember;
	private TerminalView terminal;
	private String ticketID;
	private ResponseStatus status;

	public String getInvoiceID() {
		return invoiceID;
	}

	public void setInvoiceID(String invoiceID) {
		this.invoiceID = invoiceID;
	}

	public MemberView getToMember() {
		return toMember;
	}

	public void setToMember(MemberView toMember) {
		this.toMember = toMember;
	}

	public TerminalView getTerminal() {
		return terminal;
	}

	public void setTerminal(TerminalView terminal) {
		this.terminal = terminal;
	}

	public String getTicketID() {
		return ticketID;
	}

	public void setTicketID(String ticketID) {
		this.ticketID = ticketID;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}

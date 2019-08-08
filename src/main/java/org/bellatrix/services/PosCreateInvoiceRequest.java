package org.bellatrix.services;

import java.io.Serializable;
import java.math.BigDecimal;

public class PosCreateInvoiceRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1193582141529224155L;
	private BigDecimal amount;
	private String toMember;
	private String invoiceNumber;
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

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}

package org.bellatrix.services;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import org.bellatrix.data.PaymentFields;

public class PosPaymentRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -63039604936619552L;
	private String fromMember;
	private String toMember;
	private Integer terminalID;
	private BigDecimal amount;
	private String traceNumber;
	private String ticketID;
	private String description;
	private String originator;
	private List<PaymentFields> paymentFields;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getTraceNumber() {
		return traceNumber;
	}

	public void setTraceNumber(String traceNumber) {
		this.traceNumber = traceNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<PaymentFields> getPaymentFields() {
		return paymentFields;
	}

	public void setPaymentFields(List<PaymentFields> paymentFields) {
		this.paymentFields = paymentFields;
	}

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

	public String getOriginator() {
		return originator;
	}

	public void setOriginator(String originator) {
		this.originator = originator;
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

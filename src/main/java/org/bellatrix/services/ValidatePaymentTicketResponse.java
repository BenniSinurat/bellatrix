package org.bellatrix.services;

import java.math.BigDecimal;

import org.bellatrix.data.ResponseStatus;

public class ValidatePaymentTicketResponse {

	private String toMember;
	private BigDecimal amount;
	private String description;
	private ResponseStatus status;

	public String getToMember() {
		return toMember;
	}

	public void setToMember(String toMember) {
		this.toMember = toMember;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

}

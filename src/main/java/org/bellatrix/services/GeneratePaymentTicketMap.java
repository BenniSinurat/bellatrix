package org.bellatrix.services;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.bellatrix.data.PaymentFields;
import org.bellatrix.data.TransferTypes;

public class GeneratePaymentTicketMap implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3202674295323857555L;
	private String toMember;
	private String name;
	private String email;
	private BigDecimal amount;
	private String description;
	private TransferTypes transferType;
	private String invoiceNumber;
	private List<PaymentFields> paymentFields;

	public String getToMember() {
		return toMember;
	}

	public void setToMember(String toMember) {
		this.toMember = toMember;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public TransferTypes getTransferType() {
		return transferType;
	}

	public void setTransferType(TransferTypes transferType) {
		this.transferType = transferType;
	}

	public List<PaymentFields> getPaymentFields() {
		return paymentFields;
	}

	public void setPaymentFields(List<PaymentFields> paymentFields) {
		this.paymentFields = paymentFields;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

}

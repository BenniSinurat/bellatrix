package org.bellatrix.services;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.bellatrix.data.PaymentFields;

public class GeneratePaymentTicketRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3202674295323857555L;
	private String toMember;
	private BigDecimal amount;
	private String description;
	private Integer transferTypeID;
	private String invoiceNumber;
	private List<PaymentFields> paymentFields;

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

	public Integer getTransferTypeID() {
		return transferTypeID;
	}

	public void setTransferTypeID(Integer transferTypeID) {
		this.transferTypeID = transferTypeID;
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

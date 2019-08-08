package org.bellatrix.services;

import java.io.Serializable;
import java.math.BigDecimal;

public class InquiryRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5690573947142898779L;
	private String fromMember;
	private String toMember;
	private Integer transferTypeID;
	private BigDecimal amount;

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

	public Integer getTransferTypeID() {
		return transferTypeID;
	}

	public void setTransferTypeID(Integer transferTypeID) {
		this.transferTypeID = transferTypeID;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}

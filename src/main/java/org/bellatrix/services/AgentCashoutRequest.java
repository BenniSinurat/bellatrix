package org.bellatrix.services;

import java.io.Serializable;
import java.math.BigDecimal;

public class AgentCashoutRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5949429892798009565L;
	private String fromMember;
	private BigDecimal amount;
	private Integer transferTypeID;

	public String getFromMember() {
		return fromMember;
	}

	public void setFromMember(String fromMember) {
		this.fromMember = fromMember;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getTransferTypeID() {
		return transferTypeID;
	}

	public void setTransferTypeID(Integer transferTypeID) {
		this.transferTypeID = transferTypeID;
	}

}

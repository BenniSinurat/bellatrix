package org.bellatrix.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class FeeResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3459006939283799838L;
	private BigDecimal finalAmount;
	private BigDecimal totalFees;
	private BigDecimal transactionAmount;
	private List<Fees> listTotalFees;

	public BigDecimal getFinalAmount() {
		return finalAmount;
	}

	public void setFinalAmount(BigDecimal finalAmount) {
		this.finalAmount = finalAmount;
	}

	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public BigDecimal getTotalFees() {
		return totalFees;
	}

	public void setTotalFees(BigDecimal totalFees) {
		this.totalFees = totalFees;
	}

	public List<Fees> getListTotalFees() {
		return listTotalFees;
	}

	public void setListTotalFees(List<Fees> listTotalFees) {
		this.listTotalFees = listTotalFees;
	}

	@Override
	public String toString() {
		return "[TransactionAmount : " + transactionAmount + ", TotalFee : " + totalFees + ", FinalAmount : "
				+ finalAmount + "]";
	}

}

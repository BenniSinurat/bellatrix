package org.bellatrix.data;

import java.io.Serializable;
import java.math.BigDecimal;

public class ReportBillingResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7298223421744230693L;
	private String username;
	private Integer pendingBilling;
	private Integer unpaidBilling;
	private Integer paidBilling;
	private Integer expiredBilling;
	private BigDecimal pendingAmount;
	private String formattedPendingAmount;
	private BigDecimal paidAmount;
	private String formattedPaidAmount;
	private BigDecimal unpaidAmount;
	private String formattedUnpaidAmount;
	private BigDecimal expiredAmount;
	private String formattedExpiredAmount;
	private BigDecimal totalAmount;
	private String formattedTotalAmount;
	private Integer totalRecords;
	private ResponseStatus status;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getPendingBilling() {
		return pendingBilling;
	}

	public void setPendingBilling(Integer pendingBilling) {
		this.pendingBilling = pendingBilling;
	}

	public Integer getUnpaidBilling() {
		return unpaidBilling;
	}

	public void setUnpaidBilling(Integer unpaidBilling) {
		this.unpaidBilling = unpaidBilling;
	}

	public Integer getPaidBilling() {
		return paidBilling;
	}

	public void setPaidBilling(Integer paidBilling) {
		this.paidBilling = paidBilling;
	}

	public BigDecimal getPendingAmount() {
		return pendingAmount;
	}

	public void setPendingAmount(BigDecimal pendingAmount) {
		this.pendingAmount = pendingAmount;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	public BigDecimal getUnpaidAmount() {
		return unpaidAmount;
	}

	public void setUnpaidAmount(BigDecimal unpaidAmount) {
		this.unpaidAmount = unpaidAmount;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getFormattedPendingAmount() {
		return formattedPendingAmount;
	}

	public void setFormattedPendingAmount(String formattedPendingAmount) {
		this.formattedPendingAmount = formattedPendingAmount;
	}

	public String getFormattedPaidAmount() {
		return formattedPaidAmount;
	}

	public void setFormattedPaidAmount(String formattedPaidAmount) {
		this.formattedPaidAmount = formattedPaidAmount;
	}

	public String getFormattedUnpaidAmount() {
		return formattedUnpaidAmount;
	}

	public void setFormattedUnpaidAmount(String formattedUnpaidAmount) {
		this.formattedUnpaidAmount = formattedUnpaidAmount;
	}

	public String getFormattedTotalAmount() {
		return formattedTotalAmount;
	}

	public void setFormattedTotalAmount(String formattedTotalAmount) {
		this.formattedTotalAmount = formattedTotalAmount;
	}

	public Integer getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public Integer getExpiredBilling() {
		return expiredBilling;
	}

	public void setExpiredBilling(Integer expiredBilling) {
		this.expiredBilling = expiredBilling;
	}

	public BigDecimal getExpiredAmount() {
		return expiredAmount;
	}

	public void setExpiredAmount(BigDecimal expiredAmount) {
		this.expiredAmount = expiredAmount;
	}

	public String getFormattedExpiredAmount() {
		return formattedExpiredAmount;
	}

	public void setFormattedExpiredAmount(String formattedExpiredAmount) {
		this.formattedExpiredAmount = formattedExpiredAmount;
	}

}

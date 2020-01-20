package org.bellatrix.services;

import java.math.BigDecimal;

import org.bellatrix.data.Accounts;
import org.bellatrix.data.Members;
import org.bellatrix.data.ResponseStatus;

public class BalanceInquiryResponse {

	private String formattedBalance;
	private BigDecimal balance;
	private BigDecimal reservedAmount;
	private String formattedReservedAmount;
	private ResponseStatus status;
	private Members member;
	private Accounts account;

	public String getFormattedBalance() {
		return formattedBalance;
	}

	public void setFormattedBalance(String formattedBalance) {
		this.formattedBalance = formattedBalance;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public BigDecimal getReservedAmount() {
		return reservedAmount;
	}

	public void setReservedAmount(BigDecimal reservedAmount) {
		this.reservedAmount = reservedAmount;
	}

	public String getFormattedReservedAmount() {
		return formattedReservedAmount;
	}

	public void setFormattedReservedAmount(String formattedReservedAmount) {
		this.formattedReservedAmount = formattedReservedAmount;
	}

	public Members getMember() {
		return member;
	}

	public void setMember(Members member) {
		this.member = member;
	}

	public Accounts getAccount() {
		return account;
	}

	public void setAccount(Accounts account) {
		this.account = account;
	}

}

package org.bellatrix.services;

import java.math.BigDecimal;

import org.bellatrix.data.AccountView;
import org.bellatrix.data.MemberView;
import org.bellatrix.data.ResponseStatus;

public class BalanceInquiryResponse {

	private String formattedBalance;
	private BigDecimal balance;
	private BigDecimal reservedAmount;
	private String formattedReservedAmount;
	private ResponseStatus status;
	private MemberView member;
	private AccountView account;

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

	public MemberView getMember() {
		return member;
	}

	public void setMember(MemberView member) {
		this.member = member;
	}

	public AccountView getAccount() {
		return account;
	}

	public void setAccount(AccountView account) {
		this.account = account;
	}

}

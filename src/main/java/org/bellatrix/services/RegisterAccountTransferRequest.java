package org.bellatrix.services;

public class RegisterAccountTransferRequest {

	private String accountNo;
	private String accountName;
	private String description;
	private Integer bankID;
	private String username;

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getBankID() {
		return bankID;
	}

	public void setBankID(Integer bankID) {
		this.bankID = bankID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}

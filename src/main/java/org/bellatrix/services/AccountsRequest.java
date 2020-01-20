package org.bellatrix.services;

public class AccountsRequest {

	private Integer id;
	private String name;
	private String description;
	private boolean systemAccount;
	private Integer currencyID;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isSystemAccount() {
		return systemAccount;
	}

	public void setSystemAccount(boolean systemAccount) {
		this.systemAccount = systemAccount;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCurrencyID() {
		return currencyID;
	}

	public void setCurrencyID(Integer currencyID) {
		this.currencyID = currencyID;
	}

}

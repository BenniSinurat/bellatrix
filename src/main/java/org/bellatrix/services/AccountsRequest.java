package org.bellatrix.services;

import java.math.BigDecimal;

public class AccountsRequest {

	private Integer id;
	private String name;
	private String description;
	private boolean systemAccount;

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

}

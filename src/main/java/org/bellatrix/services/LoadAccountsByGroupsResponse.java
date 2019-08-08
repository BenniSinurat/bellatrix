package org.bellatrix.services;

import java.util.List;

import org.bellatrix.data.Accounts;
import org.bellatrix.data.ResponseStatus;

public class LoadAccountsByGroupsResponse {

	private List<Accounts> accounts;
	private ResponseStatus status;

	public List<Accounts> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Accounts> accounts) {
		this.accounts = accounts;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

}

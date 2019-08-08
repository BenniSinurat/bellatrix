package org.bellatrix.services;

import org.bellatrix.data.Accounts;
import org.bellatrix.data.ResponseStatus;

public class LoadAccountsByIDResponse {

	private Accounts account;
	private ResponseStatus status;

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public Accounts getAccount() {
		return account;
	}

	public void setAccount(Accounts account) {
		this.account = account;
	}

}

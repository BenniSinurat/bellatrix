package org.bellatrix.services;

import java.util.List;

import org.bellatrix.data.Accounts;
import org.bellatrix.data.ResponseStatus;

public class AccountsPermissionResponse {
	private List<Accounts> accountPermissions;
	private ResponseStatus status;

	public List<Accounts> getAccountPermissions() {
		return accountPermissions;
	}

	public void setAccountPermissions(List<Accounts> accountPermissions) {
		this.accountPermissions = accountPermissions;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

}

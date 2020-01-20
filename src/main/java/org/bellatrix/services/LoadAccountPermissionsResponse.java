package org.bellatrix.services;

import java.util.List;

import org.bellatrix.data.AccountPermissions;
import org.bellatrix.data.ResponseStatus;

public class LoadAccountPermissionsResponse {

	private List<AccountPermissions> accountPermission;
	private ResponseStatus status;


	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public List<AccountPermissions> getAccountPermission() {
		return accountPermission;
	}

	public void setAccountPermission(List<AccountPermissions> accountPermission) {
		this.accountPermission = accountPermission;
	}

}
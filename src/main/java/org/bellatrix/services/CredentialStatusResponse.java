package org.bellatrix.services;

import org.bellatrix.data.AccessStatus;
import org.bellatrix.data.ResponseStatus;

public class CredentialStatusResponse {

	private ResponseStatus status;
	private AccessStatus accessStatus;

	public AccessStatus getAccessStatus() {
		return accessStatus;
	}

	public void setAccessStatus(AccessStatus accessStatus) {
		this.accessStatus = accessStatus;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

}

package org.bellatrix.services;

import org.bellatrix.data.ResponseStatus;

public class ConfirmBankAccountTransferResponse {
	
	private ResponseStatus status;

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

}

package org.bellatrix.services;

import org.bellatrix.data.ResponseStatus;
import org.bellatrix.data.TransferTypes;

public class LoadTransferTypesByIDResponse {
	
	private TransferTypes transferTypes;
	private ResponseStatus status;

	public TransferTypes getTransferTypes() {
		return transferTypes;
	}

	public void setTransferTypes(TransferTypes transferTypes) {
		this.transferTypes = transferTypes;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

}

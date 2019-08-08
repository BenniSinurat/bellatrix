package org.bellatrix.services;

import java.util.List;

import org.bellatrix.data.ResponseStatus;
import org.bellatrix.data.TransferTypes;

public class LoadTransferTypesByAccountIDResponse {
	
	private List<TransferTypes> transferTypes;
	private ResponseStatus status;

	public List<TransferTypes> getTransferTypes() {
		return transferTypes;
	}

	public void setTransferTypes(List<TransferTypes> transferTypes) {
		this.transferTypes = transferTypes;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

}

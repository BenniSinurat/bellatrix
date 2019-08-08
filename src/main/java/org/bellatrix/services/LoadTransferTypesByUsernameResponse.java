package org.bellatrix.services;

import java.util.List;

import org.bellatrix.data.ResponseStatus;
import org.bellatrix.data.TransferTypeView;

public class LoadTransferTypesByUsernameResponse {

	private List<TransferTypeView> transferTypes;
	private ResponseStatus status;

	public List<TransferTypeView> getTransferTypes() {
		return transferTypes;
	}

	public void setTransferTypes(List<TransferTypeView> transferTypes) {
		this.transferTypes = transferTypes;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

}

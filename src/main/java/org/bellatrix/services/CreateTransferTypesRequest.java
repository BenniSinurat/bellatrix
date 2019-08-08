package org.bellatrix.services;

import org.bellatrix.data.TransferTypes;

public class CreateTransferTypesRequest {
	private TransferTypes transferType;

	public TransferTypes getTransferType() {
		return transferType;
	}

	public void setTransferType(TransferTypes transferType) {
		this.transferType = transferType;
	}
}

package org.bellatrix.services;

import java.util.List;

import org.bellatrix.data.Fees;
import org.bellatrix.data.ResponseStatus;

public class LoadFeesByTransferTypeResponse {

	private List<Fees> fees;
	private ResponseStatus status;

	public List<Fees> getFees() {
		return fees;
	}

	public void setFees(List<Fees> fees) {
		this.fees = fees;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

}

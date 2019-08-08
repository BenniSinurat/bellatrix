package org.bellatrix.services;

import java.util.List;

import org.bellatrix.data.BankView;
import org.bellatrix.data.ResponseStatus;

public class LoadBankTransferResponse {

	private ResponseStatus status;
	private List<BankView> bankDetails;

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public List<BankView> getBankDetails() {
		return bankDetails;
	}

	public void setBankDetails(List<BankView> bankDetails) {
		this.bankDetails = bankDetails;
	}

}

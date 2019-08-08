package org.bellatrix.services;

import java.util.List;

import org.bellatrix.data.ResponseStatus;
import org.bellatrix.process.BankVA;

public class VABankResponse {

	private List<BankVA> bank;
	private ResponseStatus status;

	public List<BankVA> getBank() {
		return bank;
	}

	public void setBank(List<BankVA> bank) {
		this.bank = bank;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

}

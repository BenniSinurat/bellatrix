package org.bellatrix.services;

import org.bellatrix.data.ResponseStatus;
import org.bellatrix.process.BankVA;

public class VARegisterBankResponse {

	private BankVA bank;
	private ResponseStatus status;

	public BankVA getBank() {
		return bank;
	}

	public void setBank(BankVA bank) {
		this.bank = bank;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}
}

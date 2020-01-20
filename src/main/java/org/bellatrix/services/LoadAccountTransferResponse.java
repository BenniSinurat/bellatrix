package org.bellatrix.services;

import java.util.List;

import org.bellatrix.data.AccountTransfer;
import org.bellatrix.data.ResponseStatus;

public class LoadAccountTransferResponse {

	private List<AccountTransfer> accountTransfer;
	private Integer totalRecords;
	private ResponseStatus status;

	public List<AccountTransfer> getAccountTransfer() {
		return accountTransfer;
	}

	public void setAccountTransfer(List<AccountTransfer> accountTransfer) {
		this.accountTransfer = accountTransfer;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public Integer getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}

}

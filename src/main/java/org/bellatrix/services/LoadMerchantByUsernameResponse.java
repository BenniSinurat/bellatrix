package org.bellatrix.services;

import org.bellatrix.data.Merchants;
import org.bellatrix.data.ResponseStatus;

public class LoadMerchantByUsernameResponse {
	private ResponseStatus status;
	private Merchants merchants;
	private Integer totalRecords;

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

	public Merchants getMerchants() {
		return merchants;
	}

	public void setMerchants(Merchants merchants) {
		this.merchants = merchants;
	}

}

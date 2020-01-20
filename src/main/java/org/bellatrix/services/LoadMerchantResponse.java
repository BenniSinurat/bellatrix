package org.bellatrix.services;

import java.util.List;

import org.bellatrix.data.Merchants;
import org.bellatrix.data.ResponseStatus;

public class LoadMerchantResponse {
	private List<Merchants> merchants;
	private Integer totalRecords;
	private ResponseStatus status;

	public List<Merchants> getMerchants() {
		return merchants;
	}

	public void setMerchants(List<Merchants> merchants) {
		this.merchants = merchants;
	}

	public Integer getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

}

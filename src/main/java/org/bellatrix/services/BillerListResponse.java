package org.bellatrix.services;

import java.util.List;

import org.bellatrix.data.Billers;
import org.bellatrix.data.ResponseStatus;

public class BillerListResponse {

	private List<Billers> billers;
	private ResponseStatus status;

	public List<Billers> getBillers() {
		return billers;
	}

	public void setBillers(List<Billers> billers) {
		this.billers = billers;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

}

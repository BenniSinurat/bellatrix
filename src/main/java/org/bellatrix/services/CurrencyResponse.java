package org.bellatrix.services;

import java.util.List;

import org.bellatrix.data.Currencies;
import org.bellatrix.data.ResponseStatus;

public class CurrencyResponse {

	private List<Currencies> currency;
	private ResponseStatus status;

	public List<Currencies> getCurrency() {
		return currency;
	}

	public void setCurrency(List<Currencies> currency) {
		this.currency = currency;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}
}
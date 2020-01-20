package org.bellatrix.services;

import java.util.List;

import org.bellatrix.data.MerchantBusinessScale;
import org.bellatrix.data.ResponseStatus;

public class LoadMerchantBusinessScaleResponse {
	private List<MerchantBusinessScale> businessScale;
	private Integer totalRecords;
	private ResponseStatus status;

	public List<MerchantBusinessScale> getBusinessScale() {
		return businessScale;
	}

	public void setBusinessScale(List<MerchantBusinessScale> businessScale) {
		this.businessScale = businessScale;
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

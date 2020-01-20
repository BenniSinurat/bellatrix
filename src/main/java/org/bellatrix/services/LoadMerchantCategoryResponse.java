package org.bellatrix.services;

import java.util.List;

import org.bellatrix.data.MerchantCategory;
import org.bellatrix.data.ResponseStatus;

public class LoadMerchantCategoryResponse {
	private List<MerchantCategory> category;
	private Integer totalRecords;
	private ResponseStatus status;

	public List<MerchantCategory> getCategory() {
		return category;
	}

	public void setCategory(List<MerchantCategory> category) {
		this.category = category;
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

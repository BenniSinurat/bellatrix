package org.bellatrix.services;

import java.util.List;

import org.bellatrix.data.MerchantSubCategory;
import org.bellatrix.data.ResponseStatus;

public class LoadMerchantSubCategoryResponse {
	private List<MerchantSubCategory> subCategory;
	private Integer totalRecords;
	private ResponseStatus status;

	public List<MerchantSubCategory> getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(List<MerchantSubCategory> subCategory) {
		this.subCategory = subCategory;
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

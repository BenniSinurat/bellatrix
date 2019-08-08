package org.bellatrix.services;

import java.util.List;

import org.bellatrix.data.ResponseStatus;
import org.bellatrix.data.WebServices;

public class LoadWSResponse {

	private List<WebServices> webServices;
	private ResponseStatus status;
	private Integer totalRecords;

	public List<WebServices> getWebServices() {
		return webServices;
	}

	public void setWebServices(List<WebServices> webServices) {
		this.webServices = webServices;
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

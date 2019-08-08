package org.bellatrix.services;

import java.util.List;

import org.bellatrix.data.ResponseStatus;
import org.bellatrix.data.WebServicePermission;

public class LoadPermissionByWebServicesResponse {

	private List<WebServicePermission> webServicePermissions;
	private Integer totalRecords;
	private ResponseStatus status;

	public List<WebServicePermission> getWebServicePermissions() {
		return webServicePermissions;
	}

	public void setWebServicePermissions(List<WebServicePermission> webServicePermissions) {
		this.webServicePermissions = webServicePermissions;
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

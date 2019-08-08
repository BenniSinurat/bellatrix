package org.bellatrix.services;

import java.util.List;

import org.bellatrix.data.ResponseStatus;
import org.bellatrix.data.TransferTypesPermission;

public class LoadPermissionByTransferTypesResponse {

	private List<TransferTypesPermission> transferTypePermissions;
	private ResponseStatus status;

	public List<TransferTypesPermission> getTransferTypePermissions() {
		return transferTypePermissions;
	}

	public void setTransferTypePermissions(List<TransferTypesPermission> transferTypePermissions) {
		this.transferTypePermissions = transferTypePermissions;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

}

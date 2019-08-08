package org.bellatrix.services;

import java.util.List;
import org.bellatrix.data.TransferTypes;
import org.bellatrix.data.ResponseStatus;

public class LoadTransferTypesResponse {

	private List<TransferTypes> transferTypesList;
	private Integer totalRecords;
	private ResponseStatus status;

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

	public List<TransferTypes> getTransferTypesList() {
		return transferTypesList;
	}

	public void setTransferTypesList(List<TransferTypes> transferTypesList) {
		this.transferTypesList = transferTypesList;
	}

}

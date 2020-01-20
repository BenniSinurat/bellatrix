package org.bellatrix.services;

import java.util.List;

import org.bellatrix.data.ResponseStatus;
import org.bellatrix.data.VAStatusRecordView;

public class LoadVAStatusByMemberResponse {

	private List<VAStatusRecordView> vaRecord;
	private ResponseStatus status;
	private Integer totalRecords;

	public List<VAStatusRecordView> getVaRecord() {
		return vaRecord;
	}

	public void setVaRecord(List<VAStatusRecordView> vaRecord) {
		this.vaRecord = vaRecord;
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

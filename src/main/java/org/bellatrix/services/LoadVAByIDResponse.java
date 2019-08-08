package org.bellatrix.services;

import java.util.List;

import org.bellatrix.data.ResponseStatus;
import org.bellatrix.data.VARecordView;

public class LoadVAByIDResponse {

	private List<VARecordView> vaRecord;
	private ResponseStatus status;

	public List<VARecordView> getVaRecord() {
		return vaRecord;
	}

	public void setVaRecord(List<VARecordView> vaRecord) {
		this.vaRecord = vaRecord;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

}

package org.bellatrix.services;

import org.bellatrix.data.ResponseStatus;
import org.bellatrix.data.VAPaidRecord;
import org.bellatrix.data.VAUnPaidRecord;

public class LoadBillingStatusByMemberResponse {

	private VAPaidRecord vaPaidRecord;
	private VAUnPaidRecord vaUnPaidRecord;
	private ResponseStatus status;

	public VAPaidRecord getVaPaidRecord() {
		return vaPaidRecord;
	}

	public void setVaPaidRecord(VAPaidRecord vaPaidRecord) {
		this.vaPaidRecord = vaPaidRecord;
	}

	public VAUnPaidRecord getVaUnPaidRecord() {
		return vaUnPaidRecord;
	}

	public void setVaUnPaidRecord(VAUnPaidRecord vaUnPaidRecord) {
		this.vaUnPaidRecord = vaUnPaidRecord;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

}

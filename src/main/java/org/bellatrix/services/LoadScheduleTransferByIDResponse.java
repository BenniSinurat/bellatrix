package org.bellatrix.services;

import org.bellatrix.data.ResponseStatus;
import org.bellatrix.data.ScheduleTransfer;

public class LoadScheduleTransferByIDResponse {
	private ScheduleTransfer scheduleTransfers;
	private ResponseStatus status;

	public ScheduleTransfer getScheduleTransfers() {
		return scheduleTransfers;
	}

	public void setScheduleTransfers(ScheduleTransfer scheduleTransfers) {
		this.scheduleTransfers = scheduleTransfers;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

}

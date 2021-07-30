package org.bellatrix.services;

import java.util.List;

import org.bellatrix.data.ResponseStatus;
import org.bellatrix.data.ScheduleTransfer;

public class LoadScheduleTransferByUsernameResponse {
	private List<ScheduleTransfer> scheduleTransfers;
	private ResponseStatus status;

	public List<ScheduleTransfer> getScheduleTransfers() {
		return scheduleTransfers;
	}

	public void setScheduleTransfers(List<ScheduleTransfer> scheduleTransfers) {
		this.scheduleTransfers = scheduleTransfers;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

}

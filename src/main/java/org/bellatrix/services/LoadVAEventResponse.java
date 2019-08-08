package org.bellatrix.services;

import java.util.List;

import org.bellatrix.data.ResponseStatus;
import org.bellatrix.data.VAEvent;

public class LoadVAEventResponse {

	private List<VAEvent> event;
	private Long totalRecords;
	private ResponseStatus status;

	public List<VAEvent> getEvent() {
		return event;
	}

	public void setEvent(List<VAEvent> event) {
		this.event = event;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public Long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Long totalRecords) {
		this.totalRecords = totalRecords;
	}


}

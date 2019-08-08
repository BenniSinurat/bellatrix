package org.bellatrix.services;

import java.util.List;

import org.bellatrix.data.NotificationMessage;
import org.bellatrix.data.ResponseStatus;

public class LoadMessageResponse {

	private List<NotificationMessage> message;
	private ResponseStatus status;

	public List<NotificationMessage> getMessage() {
		return message;
	}

	public void setMessage(List<NotificationMessage> message) {
		this.message = message;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

}

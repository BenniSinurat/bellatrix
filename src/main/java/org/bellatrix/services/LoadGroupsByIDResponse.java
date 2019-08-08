package org.bellatrix.services;

import org.bellatrix.data.Groups;
import org.bellatrix.data.ResponseStatus;

public class LoadGroupsByIDResponse {

	private Groups groups;
	private ResponseStatus status;

	public LoadGroupsByIDResponse() {
	}

	public Groups getGroups() {
		return groups;
	}

	public void setGroups(Groups groups) {
		this.groups = groups;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

}

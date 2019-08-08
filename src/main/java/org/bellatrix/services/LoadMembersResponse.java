package org.bellatrix.services;

import java.util.List;
import org.bellatrix.data.Members;
import org.bellatrix.data.ResponseStatus;

public class LoadMembersResponse {

	private ResponseStatus status;
	private List<Members> members;
	private Integer totalRecords;

	public List<Members> getMembers() {
		return members;
	}

	public void setMembers(List<Members> members) {
		this.members = members;
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

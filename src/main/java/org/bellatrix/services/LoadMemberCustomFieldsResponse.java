package org.bellatrix.services;

import java.util.LinkedList;
import java.util.List;

import org.bellatrix.data.MemberCustomFields;

public class LoadMemberCustomFieldsResponse {

	private List<MemberCustomFields> customFields;
	private String status;

	public LoadMemberCustomFieldsResponse() {
		customFields = new LinkedList<MemberCustomFields>();
	}

	public List<MemberCustomFields> getCustomFields() {
		return customFields;
	}

	public void setCustomFields(List<MemberCustomFields> customFields) {
		this.customFields = customFields;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}

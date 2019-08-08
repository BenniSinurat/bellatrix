package org.bellatrix.services;

import java.util.List;

import org.bellatrix.data.ExternalMemberFields;
import org.bellatrix.data.MemberFields;

public class RegisterMemberRequest {

	private String username;
	private Integer groupID;
	private String name;
	private String email;
	private String msisdn;
	private ExternalMemberFields externalMemberFields;
	private List<MemberFields> customFields;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public List<MemberFields> getCustomFields() {
		return customFields;
	}

	public void setCustomFields(List<MemberFields> customFields) {
		this.customFields = customFields;
	}

	public Integer getGroupID() {
		return groupID;
	}

	public void setGroupID(Integer groupID) {
		this.groupID = groupID;
	}

	public ExternalMemberFields getExternalMemberFields() {
		return externalMemberFields;
	}

	public void setExternalMemberFields(ExternalMemberFields externalMemberFields) {
		this.externalMemberFields = externalMemberFields;
	}
}

package org.bellatrix.services;

public class ResetCredentialRequest {

	private String username;
	private String usernameMember;
	private String email;
	private Integer accessTypeID;
	private String newCredential;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsernameMember() {
		return usernameMember;
	}

	public void setUsernameMember(String usernameMember) {
		this.usernameMember = usernameMember;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getAccessTypeID() {
		return accessTypeID;
	}

	public void setAccessTypeID(Integer accessTypeID) {
		this.accessTypeID = accessTypeID;
	}

	public String getNewCredential() {
		return newCredential;
	}

	public void setNewCredential(String newCredential) {
		this.newCredential = newCredential;
	}

}

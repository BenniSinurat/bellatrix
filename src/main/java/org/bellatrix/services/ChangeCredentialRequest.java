package org.bellatrix.services;

public class ChangeCredentialRequest {

	private String newCredential;
	private String oldCredential;
	private Integer accessTypeID;
	private String username;
	

	public String getNewCredential() {
		return newCredential;
	}

	public void setNewCredential(String newCredential) {
		this.newCredential = newCredential;
	}

	public String getOldCredential() {
		return oldCredential;
	}

	public void setOldCredential(String oldCredential) {
		this.oldCredential = oldCredential;
	}

	public Integer getAccessTypeID() {
		return accessTypeID;
	}

	public void setAccessTypeID(Integer accessTypeID) {
		this.accessTypeID = accessTypeID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}

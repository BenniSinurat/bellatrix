package org.bellatrix.services;

public class VARegisterBankRequest {

	private String username;
	private Integer vaBankID;
	private Integer vaBinID;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getVaBankID() {
		return vaBankID;
	}

	public void setVaBankID(Integer vaBankID) {
		this.vaBankID = vaBankID;
	}

	public Integer getVaBinID() {
		return vaBinID;
	}

	public void setVaBinID(Integer vaBinID) {
		this.vaBinID = vaBinID;
	}

}

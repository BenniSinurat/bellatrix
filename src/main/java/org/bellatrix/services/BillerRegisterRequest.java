package org.bellatrix.services;

public class BillerRegisterRequest {

	private String username;
	private String billerName;
	private Integer transferTypeID;
	private String moduleURL;
	private Boolean openPayment;
	private String description;
	private Boolean externalSystem;
	private Boolean asyncPayment;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBillerName() {
		return billerName;
	}

	public void setBillerName(String billerName) {
		this.billerName = billerName;
	}

	public Integer getTransferTypeID() {
		return transferTypeID;
	}

	public void setTransferTypeID(Integer transferTypeID) {
		this.transferTypeID = transferTypeID;
	}

	public String getModuleURL() {
		return moduleURL;
	}

	public void setModuleURL(String moduleURL) {
		this.moduleURL = moduleURL;
	}

	public Boolean getOpenPayment() {
		return openPayment;
	}

	public void setOpenPayment(Boolean openPayment) {
		this.openPayment = openPayment;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getExternalSystem() {
		return externalSystem;
	}

	public void setExternalSystem(Boolean externalSystem) {
		this.externalSystem = externalSystem;
	}

	public Boolean getAsyncPayment() {
		return asyncPayment;
	}

	public void setAsyncPayment(Boolean asyncPayment) {
		this.asyncPayment = asyncPayment;
	}
}

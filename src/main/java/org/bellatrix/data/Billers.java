package org.bellatrix.data;

public class Billers {

	private Integer id;
	private String billerName;
	private String moduleURL;
	private Boolean openPayment;
	private String description;
	private Boolean externalSystem;
	private Boolean asyncPayment;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBillerName() {
		return billerName;
	}

	public void setBillerName(String billerName) {
		this.billerName = billerName;
	}

	public String getModuleURL() {
		return moduleURL;
	}

	public void setModuleURL(String moduleURL) {
		this.moduleURL = moduleURL;
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

	public Boolean getOpenPayment() {
		return openPayment;
	}

	public void setOpenPayment(Boolean openPayment) {
		this.openPayment = openPayment;
	}

}

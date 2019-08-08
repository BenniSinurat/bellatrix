package org.bellatrix.services;

public class LoadPermissionByWebServicesRequest {

	private Integer id;
	private Integer webServiceID;
	private Integer groupID;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getWebServiceID() {
		return webServiceID;
	}

	public void setWebServiceID(Integer webServiceID) {
		this.webServiceID = webServiceID;
	}

	public Integer getGroupID() {
		return groupID;
	}

	public void setGroupID(Integer groupID) {
		this.groupID = groupID;
	}

}

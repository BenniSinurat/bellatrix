package org.bellatrix.data;

import java.io.Serializable;

public class WebServicePermission implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3151992666472222098L;
	private Integer id;
	private Integer groupId;
	private Integer webServiceId;
	private String groupName;
	private String name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getWebServiceId() {
		return webServiceId;
	}

	public void setWebServiceId(Integer webServiceId) {
		this.webServiceId = webServiceId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

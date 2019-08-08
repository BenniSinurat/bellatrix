package org.bellatrix.data;

import java.io.Serializable;

public class TransferTypesPermission implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3151992666472222098L;
	private Integer id;
	private Integer groupId;
	private Integer transferTypeId;
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

	public Integer getTransferTypeId() {
		return transferTypeId;
	}

	public void setTransferTypeId(Integer transferTypeId) {
		this.transferTypeId = transferTypeId;
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

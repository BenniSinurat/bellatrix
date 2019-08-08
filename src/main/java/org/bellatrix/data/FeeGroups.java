package org.bellatrix.data;

public class FeeGroups {

	private Integer id;
	private Integer feeID;
	private Integer groupID;
	private boolean destination;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFeeID() {
		return feeID;
	}

	public void setFeeID(Integer feeID) {
		this.feeID = feeID;
	}

	public Integer getGroupID() {
		return groupID;
	}

	public void setGroupID(Integer groupID) {
		this.groupID = groupID;
	}

	public boolean isDestination() {
		return destination;
	}

	public void setDestination(boolean destination) {
		this.destination = destination;
	}

}

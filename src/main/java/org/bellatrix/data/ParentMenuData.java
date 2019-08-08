package org.bellatrix.data;


public class ParentMenuData {

	private Integer id;
	private Integer mainMenuID;
	private String mainMenuName;
	private String parentMenuName;
	private Integer sequenceNo;
	private String icon;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getParentMenuName() {
		return parentMenuName;
	}

	public void setParentMenuName(String parentMenuName) {
		this.parentMenuName = parentMenuName;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public String getMainMenuName() {
		return mainMenuName;
	}

	public void setMainMenuName(String mainMenuName) {
		this.mainMenuName = mainMenuName;
	}

	public Integer getMainMenuID() {
		return mainMenuID;
	}

	public void setMainMenuID(Integer mainMenuID) {
		this.mainMenuID = mainMenuID;
	}

}

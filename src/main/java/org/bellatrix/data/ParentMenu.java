package org.bellatrix.data;

import java.util.List;

public class ParentMenu {

	private Integer id;
	private Integer mainMenuid;
	private String parentMenuName;
	private Integer sequenceNo;
	private String icon;
	private List<ChildMenu> childMenu;

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

	public List<ChildMenu> getChildMenu() {
		return childMenu;
	}

	public void setChildMenu(List<ChildMenu> childMenu) {
		this.childMenu = childMenu;
	}

	public Integer getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public Integer getMainMenuid() {
		return mainMenuid;
	}

	public void setMainMenuid(Integer mainMenuid) {
		this.mainMenuid = mainMenuid;
	}

}

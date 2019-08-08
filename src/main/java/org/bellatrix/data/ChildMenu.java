package org.bellatrix.data;

public class ChildMenu {

	private Integer id;
	private Integer parentMenuID;
	private String childMenuName;
	private String link;
	private Integer sequenceNo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getChildMenuName() {
		return childMenuName;
	}

	public void setChildMenuName(String childMenuName) {
		this.childMenuName = childMenuName;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Integer getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public Integer getParentMenuID() {
		return parentMenuID;
	}

	public void setParentMenuID(Integer parentMenuID) {
		this.parentMenuID = parentMenuID;
	}

}

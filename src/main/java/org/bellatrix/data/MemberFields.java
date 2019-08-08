package org.bellatrix.data;

public class MemberFields {

	private Integer memberCustomFieldID;
	private String internalName;
	private String value;
	private String name;

	public String getInternalName() {
		return internalName;
	}

	public void setInternalName(String internalName) {
		this.internalName = internalName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getMemberCustomFieldID() {
		return memberCustomFieldID;
	}

	public void setMemberCustomFieldID(Integer memberCustomFieldID) {
		this.memberCustomFieldID = memberCustomFieldID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

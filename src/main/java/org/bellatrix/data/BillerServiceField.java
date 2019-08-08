package org.bellatrix.data;

import java.io.Serializable;

public class BillerServiceField implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6817804883935020182L;
	private Integer fieldID;
	private String customFieldName;
	private String internalName;
	private String value;

	public Integer getFieldID() {
		return fieldID;
	}

	public void setFieldID(Integer fieldID) {
		this.fieldID = fieldID;
	}

	public String getCustomFieldName() {
		return customFieldName;
	}

	public void setCustomFieldName(String customFieldName) {
		this.customFieldName = customFieldName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getInternalName() {
		return internalName;
	}

	public void setInternalName(String internalName) {
		this.internalName = internalName;
	}
}

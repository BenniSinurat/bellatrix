package org.bellatrix.data;

public class BillerServiceFieldView {

	private Integer id;
	private Integer serviceID;
	private Integer parentFieldID;
	private String label;
	private String fieldName;
	private String fieldExpression;
	private String dataType;
	private Integer dataLength;

	public Integer getParentFieldID() {
		return parentFieldID;
	}

	public void setParentFieldID(Integer parentFieldID) {
		this.parentFieldID = parentFieldID;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldExpression() {
		return fieldExpression;
	}

	public void setFieldExpression(String fieldExpression) {
		this.fieldExpression = fieldExpression;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Integer getDataLength() {
		return dataLength;
	}

	public void setDataLength(Integer dataLength) {
		this.dataLength = dataLength;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getServiceID() {
		return serviceID;
	}

	public void setServiceID(Integer serviceID) {
		this.serviceID = serviceID;
	}

}

package org.bellatrix.data;

public class PaymentFields {

	private Integer id;
	private Integer transferID;
	private Integer paymentCustomFieldID;
	private String internalName;
	private String value;

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

	public Integer getPaymentCustomFieldID() {
		return paymentCustomFieldID;
	}

	public void setPaymentCustomFieldID(Integer paymentCustomFieldID) {
		this.paymentCustomFieldID = paymentCustomFieldID;
	}

	public Integer getTransferID() {
		return transferID;
	}

	public void setTransferID(Integer transferID) {
		this.transferID = transferID;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}

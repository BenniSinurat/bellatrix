package org.bellatrix.data;

import java.io.Serializable;

public class TransferTypeKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2976711105488625719L;
	private Integer trxTypeID = 0;
	private Integer groupID = 0;

	public TransferTypeKey(Integer trxTypeID) {
		this.trxTypeID = trxTypeID;
	}

	public TransferTypeKey(Integer trxTypeID, Integer groupID) {
		this.trxTypeID = trxTypeID;
		this.groupID = groupID;
	}

	public Integer getTrxTypeID() {
		return trxTypeID;
	}

	public void setTrxTypeID(Integer trxTypeID) {
		this.trxTypeID = trxTypeID;
	}

	public Integer getGroupID() {
		return groupID;
	}

	public void setGroupID(Integer groupID) {
		this.groupID = groupID;
	}

}

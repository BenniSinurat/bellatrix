package org.bellatrix.services;

public class LoadPermissionByPaymentChannelRequest {

	private Integer id;
	private Integer channelID;
	private Integer memberID;
	private Integer transferTypeID;
	private String binPrefix;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getChannelID() {
		return channelID;
	}

	public void setChannelID(Integer channelID) {
		this.channelID = channelID;
	}

	public Integer getMemberID() {
		return memberID;
	}

	public void setMemberID(Integer memberID) {
		this.memberID = memberID;
	}

	public Integer getTransferTypeID() {
		return transferTypeID;
	}

	public void setTransferTypeID(Integer transferTypeID) {
		this.transferTypeID = transferTypeID;
	}

	public String getBinPrefix() {
		return binPrefix;
	}

	public void setBinPrefix(String binPrefix) {
		this.binPrefix = binPrefix;
	}

}

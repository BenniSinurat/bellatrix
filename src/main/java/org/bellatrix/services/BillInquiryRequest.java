package org.bellatrix.services;

public class BillInquiryRequest {

	private Integer merchantID;
	private String billID;
	private String acquiringID;
	private Integer channelID;

	public Integer getMerchantID() {
		return merchantID;
	}

	public void setMerchantID(Integer merchantID) {
		this.merchantID = merchantID;
	}

	public String getBillID() {
		return billID;
	}

	public void setBillID(String billID) {
		this.billID = billID;
	}

	public String getAcquiringID() {
		return acquiringID;
	}

	public void setAcquiringID(String acquiringID) {
		this.acquiringID = acquiringID;
	}

	public Integer getChannelID() {
		return channelID;
	}

	public void setChannelID(Integer channelID) {
		this.channelID = channelID;
	}

}

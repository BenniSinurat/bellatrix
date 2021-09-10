package org.bellatrix.data;

import java.io.Serializable;

public class PaymentChannelPermissions implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3511153635915704349L;
	private Integer id;
	private Integer channelID;
	private Integer transferTypeID;
	private String transferTypeName;
	private Integer memberID;
	private String memberName;
	private String memberUsername;
	private String binPrefix;
	private String inquiryURL;
	private String paymentURL;
	private String reversedURL;

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

	public Integer getTransferTypeID() {
		return transferTypeID;
	}

	public void setTransferTypeID(Integer transferTypeID) {
		this.transferTypeID = transferTypeID;
	}

	public String getTransferTypeName() {
		return transferTypeName;
	}

	public void setTransferTypeName(String transferTypeName) {
		this.transferTypeName = transferTypeName;
	}

	public Integer getMemberID() {
		return memberID;
	}

	public void setMemberID(Integer memberID) {
		this.memberID = memberID;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberUsername() {
		return memberUsername;
	}

	public void setMemberUsername(String memberUsername) {
		this.memberUsername = memberUsername;
	}

	public String getBinPrefix() {
		return binPrefix;
	}

	public void setBinPrefix(String binPrefix) {
		this.binPrefix = binPrefix;
	}

	public String getInquiryURL() {
		return inquiryURL;
	}

	public void setInquiryURL(String inquiryURL) {
		this.inquiryURL = inquiryURL;
	}

	public String getPaymentURL() {
		return paymentURL;
	}

	public void setPaymentURL(String paymentURL) {
		this.paymentURL = paymentURL;
	}

	public String getReversedURL() {
		return reversedURL;
	}

	public void setReversedURL(String reversedURL) {
		this.reversedURL = reversedURL;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}

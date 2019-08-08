package org.bellatrix.data;

public class MultiBillers {

	private Integer id;
	private Integer billerID;
	private Integer memberID;
	private Integer transferTypeID;
	private String inquiryURL;
	private String paymentURL;
	private String reversalURL;
	private String statusURL;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBillerID() {
		return billerID;
	}

	public void setBillerID(Integer billerID) {
		this.billerID = billerID;
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

	public String getReversalURL() {
		return reversalURL;
	}

	public void setReversalURL(String reversalURL) {
		this.reversalURL = reversalURL;
	}

	public String getStatusURL() {
		return statusURL;
	}

	public void setStatusURL(String statusURL) {
		this.statusURL = statusURL;
	}

}

package org.bellatrix.services;

import org.bellatrix.data.MemberView;
import org.bellatrix.data.ResponseStatus;

public class TopupResponse {

	private MemberView member;
	private String otp;
	private String transactionNumber;
	private ResponseStatus status;

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public MemberView getMember() {
		return member;
	}

	public void setMember(MemberView member) {
		this.member = member;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

}

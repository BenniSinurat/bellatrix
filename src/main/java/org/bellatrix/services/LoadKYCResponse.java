package org.bellatrix.services;

import java.util.List;

import org.bellatrix.data.MemberKYC;
import org.bellatrix.data.ResponseStatus;

public class LoadKYCResponse {

	private List<MemberKYC> memberKYC;
	private Integer totalRecords;
	private ResponseStatus status;

	public List<MemberKYC> getMemberKYC() {
		return memberKYC;
	}

	public void setMemberKYC(List<MemberKYC> memberKYC) {
		this.memberKYC = memberKYC;
	}

	public Integer getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

}

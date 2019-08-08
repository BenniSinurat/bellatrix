package org.bellatrix.services;

public class LoadBankTransferRequest {

	private Integer bankID;
	private String username;
	private Integer currentPage;
	private Integer pageSize;

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getBankID() {
		return bankID;
	}

	public void setBankID(Integer bankID) {
		this.bankID = bankID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}

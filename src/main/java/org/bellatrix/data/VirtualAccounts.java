package org.bellatrix.data;

public class VirtualAccounts {

	private String bankCode;
	private String bankName;
	private Integer transferTypeID;
	private String binNumber;
	private String accountNumber;
	private String referenceCodeLength;

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public Integer getTransferTypeID() {
		return transferTypeID;
	}

	public void setTransferTypeID(Integer transferTypeID) {
		this.transferTypeID = transferTypeID;
	}

	public String getBinNumber() {
		return binNumber;
	}

	public void setBinNumber(String binNumber) {
		this.binNumber = binNumber;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getReferenceCodeLength() {
		return referenceCodeLength;
	}

	public void setReferenceCodeLength(String referenceCodeLength) {
		this.referenceCodeLength = referenceCodeLength;
	}


}

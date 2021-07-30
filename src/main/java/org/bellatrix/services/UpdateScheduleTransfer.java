package org.bellatrix.services;

public class UpdateScheduleTransfer {
	private Integer id;
	private Integer fromMemberID;
	private Integer tranferTypeID;
	private Integer bankID;
	private String accountNo;
	private String accountName;
	private boolean enabled;
	private String scheduleDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFromMemberID() {
		return fromMemberID;
	}

	public void setFromMemberID(Integer fromMemberID) {
		this.fromMemberID = fromMemberID;
	}

	public Integer getTranferTypeID() {
		return tranferTypeID;
	}

	public void setTranferTypeID(Integer tranferTypeID) {
		this.tranferTypeID = tranferTypeID;
	}

	public Integer getBankID() {
		return bankID;
	}

	public void setBankID(Integer bankID) {
		this.bankID = bankID;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

}

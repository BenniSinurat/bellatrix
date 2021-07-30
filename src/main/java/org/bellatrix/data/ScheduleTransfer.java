package org.bellatrix.data;

import java.io.Serializable;

public class ScheduleTransfer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7257864749894131819L;
	private Integer id;
	private Integer fromMemberID;
	private Integer transferTypeID;
	private Integer bankID;
	private String accountNo;
	private String accountName;
	private boolean enabled;
	private String scheduletDate;

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

	public Integer getTransferTypeID() {
		return transferTypeID;
	}

	public void setTransferTypeID(Integer transferTypeID) {
		this.transferTypeID = transferTypeID;
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

	public String getScheduletDate() {
		return scheduletDate;
	}

	public void setScheduletDate(String scheduletDate) {
		this.scheduletDate = scheduletDate;
	}

}

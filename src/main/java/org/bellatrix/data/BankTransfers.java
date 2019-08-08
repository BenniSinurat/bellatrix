package org.bellatrix.data;

import java.sql.Timestamp;

public class BankTransfers {

	private Integer id;
	private String bankCode;
	private String bankName;
	private String swiftCode;
	private String transferMethod;
	private String chargingCode;
	private String poolAccountNo;
	private Integer transferTypeID;
	private Integer toMemberID;
	private String toUsername;
	private String gatewayURL;
	private boolean enabled;
	private Timestamp modifiedDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public String getSwiftCode() {
		return swiftCode;
	}

	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}

	public String getTransferMethod() {
		return transferMethod;
	}

	public void setTransferMethod(String transferMethod) {
		this.transferMethod = transferMethod;
	}

	public String getChargingCode() {
		return chargingCode;
	}

	public void setChargingCode(String chargingCode) {
		this.chargingCode = chargingCode;
	}

	public String getPoolAccountNo() {
		return poolAccountNo;
	}

	public void setPoolAccountNo(String poolAccountNo) {
		this.poolAccountNo = poolAccountNo;
	}

	public Integer getTransferTypeID() {
		return transferTypeID;
	}

	public void setTransferTypeID(Integer transferTypeID) {
		this.transferTypeID = transferTypeID;
	}

	public String getGatewayURL() {
		return gatewayURL;
	}

	public void setGatewayURL(String gatewayURL) {
		this.gatewayURL = gatewayURL;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp timestamp) {
		this.modifiedDate = timestamp;
	}

	public Integer getToMemberID() {
		return toMemberID;
	}

	public void setToMemberID(Integer toMemberID) {
		this.toMemberID = toMemberID;
	}

	public String getToUsername() {
		return toUsername;
	}

	public void setToUsername(String toUsername) {
		this.toUsername = toUsername;
	}

}

package org.bellatrix.services;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.bellatrix.data.Accounts;
import org.bellatrix.data.FeeResult;
import org.bellatrix.data.Members;
import org.bellatrix.data.Notifications;
import org.bellatrix.data.Terminal;
import org.bellatrix.data.TransferTypes;
import org.bellatrix.data.WebServices;

public class PaymentDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8208040919455727883L;
	private Integer transferID;
	private Members fromMember;
	private Members toMember;
	private Accounts fromAccount;
	private Accounts toAccount;
	private TransferTypes transferType;
	private FeeResult fees;
	private WebServices webService;
	private PaymentRequest request;
	private String transactionNumber;
	private Date transactionDate;
	private String otp;
	private boolean twoFactorAuthentication;
	private List<Notifications> notification;
	private HashMap<String, Object> privateField;
	private Terminal terminal;
	private String ticketID;
	private String description;
	private String remark;

	public Members getFromMember() {
		return fromMember;
	}

	public void setFromMember(Members fromMember) {
		this.fromMember = fromMember;
	}

	public Members getToMember() {
		return toMember;
	}

	public void setToMember(Members toMember) {
		this.toMember = toMember;
	}

	public Accounts getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(Accounts fromAccount) {
		this.fromAccount = fromAccount;
	}

	public Accounts getToAccount() {
		return toAccount;
	}

	public void setToAccount(Accounts toAccount) {
		this.toAccount = toAccount;
	}

	public FeeResult getFees() {
		return fees;
	}

	public void setFees(FeeResult fees) {
		this.fees = fees;
	}

	public WebServices getWebService() {
		return webService;
	}

	public void setWebService(WebServices webService) {
		this.webService = webService;
	}

	public PaymentRequest getRequest() {
		return request;
	}

	public void setRequest(PaymentRequest request) {
		this.request = request;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public TransferTypes getTransferType() {
		return transferType;
	}

	public void setTransferType(TransferTypes transferType) {
		this.transferType = transferType;
	}

	public Integer getTransferID() {
		return transferID;
	}

	public void setTransferID(Integer transferID) {
		this.transferID = transferID;
	}

	public List<Notifications> getNotification() {
		return notification;
	}

	public void setNotification(List<Notifications> notification) {
		this.notification = notification;
	}

	public boolean isTwoFactorAuthentication() {
		return twoFactorAuthentication;
	}

	public void setTwoFactorAuthentication(boolean twoFactorAuthentication) {
		this.twoFactorAuthentication = twoFactorAuthentication;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public HashMap<String, Object> getPrivateField() {
		return privateField;
	}

	public void setPrivateField(HashMap<String, Object> privateField) {
		this.privateField = privateField;
	}

	public Terminal getTerminal() {
		return terminal;
	}

	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
	}

	public String getTicketID() {
		return ticketID;
	}

	public void setTicketID(String ticketID) {
		this.ticketID = ticketID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}

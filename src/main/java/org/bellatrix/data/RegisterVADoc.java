package org.bellatrix.data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "RegisterVA")
public class RegisterVADoc {

	@Id
	private String id;
	@Indexed(expireAfterSeconds = 0)
	private LocalDateTime expiredAt;
	private Members member;
	private Members fromMember;
	private TransferTypes transferType;
	private Integer bankID;
	private String bankCode;
	private String bankName;
	private BigDecimal amount;
	private BigDecimal minimumPayment;
	private String name;
	private String email;
	private String msisdn;
	private String referenceNumber;
	private List<BillerServiceField> privateField;
	private boolean fullPayment;
	private boolean persistent;
	private String callbackURL;
	private String ticketID;
	private LocalDateTime createdDate;
	private String paymentCode;
	private FeeResult fees;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDateTime getExpiredAt() {
		return expiredAt;
	}

	public void setExpiredAt(LocalDateTime expiredAt) {
		this.expiredAt = expiredAt;
	}

	public Members getMember() {
		return member;
	}

	public void setMember(Members member) {
		this.member = member;
	}

	public TransferTypes getTransferType() {
		return transferType;
	}

	public void setTransferType(TransferTypes transferType) {
		this.transferType = transferType;
	}

	public Integer getBankID() {
		return bankID;
	}

	public void setBankID(Integer bankID) {
		this.bankID = bankID;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isFullPayment() {
		return fullPayment;
	}

	public void setFullPayment(boolean fullPayment) {
		this.fullPayment = fullPayment;
	}

	public boolean isPersistent() {
		return persistent;
	}

	public void setPersistent(boolean persistent) {
		this.persistent = persistent;
	}

	public String getCallbackURL() {
		return callbackURL;
	}

	public void setCallbackURL(String callbackURL) {
		this.callbackURL = callbackURL;
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

	public BigDecimal getMinimumPayment() {
		return minimumPayment;
	}

	public void setMinimumPayment(BigDecimal minimumPayment) {
		this.minimumPayment = minimumPayment;
	}

	public String getTicketID() {
		return ticketID;
	}

	public void setTicketID(String ticketID) {
		this.ticketID = ticketID;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public List<BillerServiceField> getPrivateField() {
		return privateField;
	}

	public void setPrivateField(List<BillerServiceField> privateField) {
		this.privateField = privateField;
	}

	public Members getFromMember() {
		return fromMember;
	}

	public void setFromMember(Members fromMember) {
		this.fromMember = fromMember;
	}

	public String getPaymentCode() {
		return paymentCode;
	}

	public void setPaymentCode(String paymentCode) {
		this.paymentCode = paymentCode;
	}

	public FeeResult getFees() {
		return fees;
	}

	public void setFees(FeeResult fees) {
		this.fees = fees;
	}

}

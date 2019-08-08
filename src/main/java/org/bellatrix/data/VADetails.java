package org.bellatrix.data;

public class VADetails {

	private Members fromMember;
	private Members toMember;
	private TransferTypes trxType;
	private RegisterVADoc regVA;
	private String paymentCode;
	private VirtualAccounts virtualAccount;

	public VirtualAccounts getVirtualAccount() {
		return virtualAccount;
	}

	public void setVirtualAccount(VirtualAccounts virtualAccount) {
		this.virtualAccount = virtualAccount;
	}

	public Members getFromMember() {
		return fromMember;
	}

	public void setFromMember(Members fromMember) {
		this.fromMember = fromMember;
	}

	public RegisterVADoc getRegVA() {
		return regVA;
	}

	public void setRegVA(RegisterVADoc regVA) {
		this.regVA = regVA;
	}

	public Members getToMember() {
		return toMember;
	}

	public void setToMember(Members toMember) {
		this.toMember = toMember;
	}

	public TransferTypes getTrxType() {
		return trxType;
	}

	public void setTrxType(TransferTypes trxType) {
		this.trxType = trxType;
	}

	public String getPaymentCode() {
		return paymentCode;
	}

	public void setPaymentCode(String paymentCode) {
		this.paymentCode = paymentCode;
	}

}

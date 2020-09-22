package org.bellatrix.services;

import java.util.List;

import org.bellatrix.data.PaymentChannelPermissions;
import org.bellatrix.data.ResponseStatus;

public class LoadPermissionByPaymentChannelResponse {

	private List<PaymentChannelPermissions> paymentChannelPermission;
	private ResponseStatus status;

	

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public List<PaymentChannelPermissions> getPaymentChannelPermission() {
		return paymentChannelPermission;
	}

	public void setPaymentChannelPermission(List<PaymentChannelPermissions> paymentChannelPermission) {
		this.paymentChannelPermission = paymentChannelPermission;
	}

}

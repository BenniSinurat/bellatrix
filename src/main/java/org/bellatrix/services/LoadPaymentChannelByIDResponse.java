package org.bellatrix.services;

import org.bellatrix.data.PaymentChannel;
import org.bellatrix.data.ResponseStatus;

public class LoadPaymentChannelByIDResponse {

	private PaymentChannel paymentChannel;
	private ResponseStatus status;

	

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public PaymentChannel getPaymentChannel() {
		return paymentChannel;
	}

	public void setPaymentChannel(PaymentChannel paymentChannel) {
		this.paymentChannel = paymentChannel;
	}

}

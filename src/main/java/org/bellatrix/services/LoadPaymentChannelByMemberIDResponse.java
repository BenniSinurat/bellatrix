package org.bellatrix.services;

import java.util.List;

import org.bellatrix.data.PaymentChannel;
import org.bellatrix.data.ResponseStatus;

public class LoadPaymentChannelByMemberIDResponse {

	private List<PaymentChannel> paymentChannel;
	private ResponseStatus status;

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public List<PaymentChannel> getPaymentChannel() {
		return paymentChannel;
	}

	public void setPaymentChannel(List<PaymentChannel> paymentChannel) {
		this.paymentChannel = paymentChannel;
	}

}

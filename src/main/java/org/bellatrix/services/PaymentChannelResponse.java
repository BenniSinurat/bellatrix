package org.bellatrix.services;

import java.util.List;

import org.bellatrix.data.PaymentChannel;

public class PaymentChannelResponse {

	private List<PaymentChannel> paymentChannel;

	public List<PaymentChannel> getPaymentChannel() {
		return paymentChannel;
	}

	public void setPaymentChannel(List<PaymentChannel> paymentChannel) {
		this.paymentChannel = paymentChannel;
	}

}

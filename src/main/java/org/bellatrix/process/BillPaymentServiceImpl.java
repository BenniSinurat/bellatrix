package org.bellatrix.process;

import java.util.LinkedList;
import java.util.List;
import javax.xml.ws.Holder;
import org.bellatrix.data.Billers;
import org.bellatrix.data.Header;
import org.bellatrix.data.PaymentChannel;
import org.bellatrix.data.Status;
import org.bellatrix.data.StatusBuilder;
import org.bellatrix.data.TransactionException;
import org.bellatrix.services.BillInquiryRequest;
import org.bellatrix.services.BillInquiryResponse;
import org.bellatrix.services.BillPayment;
import org.bellatrix.services.BillPaymentRequest;
import org.bellatrix.services.BillPaymentResponse;
import org.bellatrix.services.BillerDetailsRequest;
import org.bellatrix.services.BillerListResponse;
import org.bellatrix.services.BillerRegisterRequest;
import org.bellatrix.services.PaymentChannelResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BillPaymentServiceImpl implements BillPayment {

	@Autowired
	private WebserviceValidation webserviceValidation;
	@Autowired
	private BillPaymentValidation billPaymentValidation;

	@Override
	public BillerListResponse loadBillerList(Holder<Header> headerParam) throws Exception {
		BillerListResponse blr = new BillerListResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			List<Billers> billers = billPaymentValidation.validateMemberBillers();
			blr.setBillers(billers);
			blr.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return blr;
		} catch (TransactionException e) {
			blr.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return blr;
		}
	}

	@Override
	public BillerListResponse loadBillersFromUsername(Holder<Header> headerParam, BillerDetailsRequest req)
			throws Exception {
		BillerListResponse blr = new BillerListResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			List<Billers> billers = billPaymentValidation.validateMemberBillers(req.getUsername());
			blr.setBillers(billers);
			blr.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return blr;
		} catch (TransactionException e) {
			blr.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return blr;
		}
	}

	@Override
	public void registerBiller(Holder<Header> headerParam, BillerRegisterRequest req) throws Exception {
		webserviceValidation.validateWebservice(headerParam.value.getToken());
		billPaymentValidation.validateMemberBillers(req);
	}

	@Override
	public BillerListResponse loadBillersFromID(Holder<Header> headerParam, BillerDetailsRequest req) throws Exception {
		BillerListResponse blr = new BillerListResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Billers billers = billPaymentValidation.validateMemberBillers(req.getUsername(), req.getBillerID());
			List<Billers> lb = new LinkedList<Billers>();
			lb.add(billers);
			blr.setBillers(lb);
			blr.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return blr;
		} catch (TransactionException e) {
			blr.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return blr;
		}
	}

	@Override
	public BillInquiryResponse inquiryBilling(Holder<Header> headerParam, BillInquiryRequest req) throws Exception {
		webserviceValidation.validateWebservice(headerParam.value.getToken());
		Billers billers = billPaymentValidation.validateMemberBillers(req.getBillID(), req.getMerchantID());
		return null;
	}

	@Override
	public BillPaymentResponse paymentBilling(Holder<Header> headerParam, BillPaymentRequest req) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaymentChannelResponse loadPaymentChannel(Holder<Header> headerParam) throws Exception {
		PaymentChannelResponse pcr = new PaymentChannelResponse();
		webserviceValidation.validateWebservice(headerParam.value.getToken());
		List<PaymentChannel> lpc = billPaymentValidation.validatePaymentChannel();
		pcr.setPaymentChannel(lpc);
		return pcr;
	}

}

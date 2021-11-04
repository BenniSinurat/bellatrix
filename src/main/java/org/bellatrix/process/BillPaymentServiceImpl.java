package org.bellatrix.process;

import java.util.LinkedList;
import java.util.List;
import javax.xml.ws.Holder;

import org.apache.log4j.Logger;
import org.bellatrix.data.Billers;
import org.bellatrix.data.Header;
import org.bellatrix.data.PaymentChannel;
import org.bellatrix.data.PaymentChannelPermissions;
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
import org.bellatrix.services.LoadPaymentChannelByIDRequest;
import org.bellatrix.services.LoadPaymentChannelByIDResponse;
import org.bellatrix.services.LoadPaymentChannelByMemberIDRequest;
import org.bellatrix.services.LoadPaymentChannelByMemberIDResponse;
import org.bellatrix.services.LoadPermissionByPaymentChannelRequest;
import org.bellatrix.services.LoadPermissionByPaymentChannelResponse;
import org.bellatrix.services.PaymentChannelPermissionRequest;
import org.bellatrix.services.PaymentChannelRequest;
import org.bellatrix.services.PaymentChannelResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BillPaymentServiceImpl implements BillPayment {

	@Autowired
	private WebserviceValidation webserviceValidation;
	@Autowired
	private BillPaymentValidation billPaymentValidation;
	@Autowired
	private BaseRepository baseRepository;
	@Autowired
	private TransferTypeValidation transferTypeValidation;
	@Autowired
	private MemberValidation memberValidation;
	private Logger logger = Logger.getLogger(BillPaymentServiceImpl.class);

	/**@Override
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
	}**/

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
		logger.info("Biller: " + billers);
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

	@Override
	public void createPaymentChannel(Holder<Header> headerParam, PaymentChannelRequest req) throws Exception {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			baseRepository.getBillPaymentRepository().createPaymentChannel(req);

		} catch (Exception e) {
			throw new TransactionException(e.getMessage());
		}
	}

	@Override
	public void updatePaymentChannel(Holder<Header> headerParam, PaymentChannelRequest req) throws Exception {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			baseRepository.getBillPaymentRepository().updatePaymentChannel(req);

		} catch (Exception e) {
			throw new TransactionException(e.getMessage());
		}
	}

	@Override
	public void createPaymentChannelPermissions(Holder<Header> headerParam, PaymentChannelPermissionRequest req)
			throws TransactionException {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			billPaymentValidation.validatePaymentChannel(req.getChannelID());
			transferTypeValidation.validateTransferType(req.getTransferTypeID());
			baseRepository.getBillPaymentRepository().createPaymentChannelPermission(req);
		} catch (Exception e) {
			throw new TransactionException(e.getMessage());
		}
	}

	@Override
	public void updatePaymentChannelPermissions(Holder<Header> headerParam, PaymentChannelPermissionRequest req)
			throws TransactionException {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			billPaymentValidation.validatePaymentChannel(req.getChannelID());
			transferTypeValidation.validateTransferType(req.getTransferTypeID());
			baseRepository.getBillPaymentRepository().updatePaymentChannelPermission(req);
		} catch (Exception e) {
			throw new TransactionException(e.getMessage());
		}
	}

	@Override
	public void deletePaymentChannelPermissions(Holder<Header> headerParam, PaymentChannelPermissionRequest req)
			throws TransactionException {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			baseRepository.getBillPaymentRepository().deletePaymentChannelPermission(req.getId());
		} catch (Exception e) {
			throw new TransactionException(e.getMessage());
		}
	}

	@Override
	public LoadPermissionByPaymentChannelResponse loadPermissionsByPaymentChannel(Holder<Header> headerParam,
			LoadPermissionByPaymentChannelRequest req) {
		LoadPermissionByPaymentChannelResponse res = new LoadPermissionByPaymentChannelResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			billPaymentValidation.validatePaymentChannel(req.getChannelID());
			
			List<PaymentChannelPermissions> channelPermission = new LinkedList<PaymentChannelPermissions>();
			if(req.getBinPrefix() == null) {
				channelPermission = baseRepository.getBillPaymentRepository()
					.listPermissionByPaymentChannel(req.getChannelID());
			}else {
				channelPermission = baseRepository.getBillPaymentRepository()
						.listPermissionByPaymentChannel(req.getChannelID(), req.getBinPrefix());
			}
			
			res.setPaymentChannelPermission(channelPermission);
			res.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
		} catch (Exception e) {
			res.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return res;
		}
		return res;
	}
	
	@Override
	public LoadPaymentChannelByMemberIDResponse loadPaymentChannelByMemberID(Holder<Header> headerParam,
			LoadPaymentChannelByMemberIDRequest req) {
		LoadPaymentChannelByMemberIDResponse res = new LoadPaymentChannelByMemberIDResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			memberValidation.validateMemberID(req.getMemberID(), true);
			
			List<PaymentChannel> channel = baseRepository.getBillPaymentRepository().listPaymentChannelByMemberID(req.getMemberID());
			res.setPaymentChannel(channel);
			res.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
		} catch (Exception e) {
			res.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return res;
		}
		return res;
	}
	
	@Override
	public LoadPaymentChannelByIDResponse loadPaymentChannelByID(Holder<Header> headerParam,
			LoadPaymentChannelByIDRequest req) {
		LoadPaymentChannelByIDResponse res = new LoadPaymentChannelByIDResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			
			PaymentChannel channel = baseRepository.getBillPaymentRepository().findPaymentChannelByID(req.getChannelID());
			res.setPaymentChannel(channel);
			res.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
		} catch (Exception e) {
			res.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return res;
		}
		return res;
	}
}

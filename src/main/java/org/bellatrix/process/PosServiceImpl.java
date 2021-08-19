package org.bellatrix.process;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.xml.ws.Holder;

import org.apache.commons.lang.RandomStringUtils;
import org.bellatrix.data.Accounts;
import org.bellatrix.data.FeeResult;
import org.bellatrix.data.Header;
import org.bellatrix.data.MemberView;
import org.bellatrix.data.Members;
import org.bellatrix.data.Notifications;
import org.bellatrix.data.Status;
import org.bellatrix.data.StatusBuilder;
import org.bellatrix.data.Terminal;
import org.bellatrix.data.TerminalView;
import org.bellatrix.data.TransactionException;
import org.bellatrix.data.TransferTypeFields;
import org.bellatrix.data.TransferTypes;
import org.bellatrix.services.DeletePOSRequest;
import org.bellatrix.services.GeneratePaymentTicketRequest;
import org.bellatrix.services.LoadTerminalByUsernameRequest;
import org.bellatrix.services.PaymentDetails;
import org.bellatrix.services.PaymentRequest;
import org.bellatrix.services.PaymentResponse;
import org.bellatrix.services.Pos;
import org.bellatrix.services.PosCreateInvoiceRequest;
import org.bellatrix.services.PosCreateInvoiceResponse;
import org.bellatrix.services.PosInquiryRequest;
import org.bellatrix.services.PosInquiryResponse;
import org.bellatrix.services.PosPaymentRequest;
import org.bellatrix.services.PosPaymentResponse;
import org.bellatrix.services.RegisterPOSRequest;
import org.bellatrix.services.LoadTerminalByIDRequest;
import org.bellatrix.services.TerminalInquiryResponse;
import org.bellatrix.services.UpdatePOSRequest;
import org.mule.api.MuleException;
import org.mule.module.client.MuleClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

public class PosServiceImpl implements Pos {

	@Autowired
	private PosPaymentValidation posPaymentValidation;
	@Autowired
	private PaymentValidation paymentValidation;
	@Autowired
	private MemberValidation memberValidation;
	@Autowired
	private BaseRepository baseRepository;
	@Autowired
	private Configurator configurator;
	@Autowired
	private HazelcastInstance instance;
	@Autowired
	private FeeProcessor feeProcessor;
	@Autowired
	private AccountValidation accountValidation;
	@Autowired
	private TransferTypeValidation transferTypeValidation;
	@Autowired
	private WebserviceValidation webserviceValidation;

	@Override
	public TerminalInquiryResponse loadTerminalByUsername(Holder<Header> headerParam, LoadTerminalByUsernameRequest req)
			throws Exception {

		TerminalInquiryResponse tir = new TerminalInquiryResponse();
		try {
			List<Terminal> terminal = posPaymentValidation.validatePosTerminal(req, headerParam.value.getToken());
			List<TerminalView> ltv = new LinkedList<TerminalView>();

			for (int i = 0; i < terminal.size(); i++) {
				TerminalView tv = new TerminalView();
				tv.setId(terminal.get(i).getId());
				tv.setAddress(terminal.get(i).getAddress());
				tv.setName(terminal.get(i).getName());
				tv.setCity(terminal.get(i).getCity());
				tv.setPostalCode(terminal.get(i).getPostalCode());
				tv.setEmail(terminal.get(i).getEmail());
				tv.setMsisdn(terminal.get(i).getMsisdn());
				tv.setPic(terminal.get(i).getPic());
				tv.setNnsID(terminal.get(i).getNnsID());
				tv.setMerchantCategoryCode(terminal.get(i).getMerchantCategoryCode());
				ltv.add(tv);

				MemberView toTransfer = new MemberView();
				toTransfer.setId(terminal.get(i).getToMember().getId());
				toTransfer.setName(terminal.get(i).getToMember().getName());
				toTransfer.setUsername(terminal.get(i).getToMember().getUsername());
				tir.setToMember(toTransfer);
			}

			tir.setTerminal(ltv);
			tir.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return tir;
		} catch (TransactionException e) {
			tir.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return tir;
		}
	}

	@Override
	public TerminalInquiryResponse loadTerminalByID(Holder<Header> headerParam, LoadTerminalByIDRequest req)
			throws Exception {
		TerminalInquiryResponse tir = new TerminalInquiryResponse();
		try {
			Terminal terminal = posPaymentValidation.validatePosTerminal(req, headerParam.value.getToken());

			TerminalView tv = new TerminalView();
			tv.setId(terminal.getId());
			tv.setAddress(terminal.getAddress());
			tv.setName(terminal.getName());
			tv.setCity(terminal.getCity());
			tv.setPostalCode(terminal.getPostalCode());
			tv.setEmail(terminal.getEmail());
			tv.setMsisdn(terminal.getMsisdn());
			tv.setPic(terminal.getPic());
			tv.setNnsID(terminal.getNnsID());
			tv.setMerchantCategoryCode(terminal.getMerchantCategoryCode());

			MemberView toTransfer = new MemberView();
			toTransfer.setId(terminal.getToMember().getId());
			toTransfer.setName(terminal.getToMember().getName());
			toTransfer.setUsername(terminal.getToMember().getUsername());

			List<TerminalView> ltv = new LinkedList<TerminalView>();
			ltv.add(tv);
			tir.setTerminal(ltv);
			tir.setToMember(toTransfer);
			tir.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return tir;
		} catch (TransactionException e) {
			tir.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return tir;
		}
	}

	@Override
	public PosInquiryResponse posInquiry(Holder<Header> headerParam, PosInquiryRequest req) throws Exception {
		PosInquiryResponse pir = new PosInquiryResponse();
		try {
			Members fromMember = memberValidation.validateMember(req.getFromMember(), true);
			Terminal terminal = posPaymentValidation.validatePosInquiry(req, headerParam.value.getToken());

			TransferTypes transferType = transferTypeValidation.validateTransferType(terminal.getTransferTypeID());

			Accounts fromAccount = accountValidation.validateAccount(transferType, fromMember, true);
			Accounts toAccount = accountValidation.validateAccount(transferType, terminal.getToMember(), false);
			FeeResult fr = null;

			IMap<String, GeneratePaymentTicketRequest> genMap = instance.getMap("GeneratePaymentMap");
			if (req.getTicketID() == null) {
				pir.setStatus(StatusBuilder.getStatus(Status.INVALID_PARAMETER));
				return pir;
			}
			GeneratePaymentTicketRequest gpt = genMap.get(req.getTicketID());
			if (gpt == null) {
				pir.setStatus(StatusBuilder.getStatus(Status.PAYMENT_CODE_NOT_FOUND));
				return pir;
			}

			fr = feeProcessor.CalculateFee(transferType, fromMember, terminal.getToMember(), fromAccount, toAccount,
					gpt.getAmount());
			pir.setInvoiceNumber(gpt.getInvoiceNumber());

			pir.setFinalAmount(fr.getFinalAmount());
			pir.setTotalFees(fr.getTotalFees());
			pir.setTransactionAmount(fr.getTransactionAmount());

			MemberView fromTransfer = new MemberView();
			fromTransfer.setId(fromMember.getId());
			fromTransfer.setName(fromMember.getName());
			fromTransfer.setUsername(fromMember.getUsername());
			pir.setFromMember(fromTransfer);

			MemberView toTransfer = new MemberView();
			toTransfer.setId(terminal.getToMember().getId());
			toTransfer.setName(terminal.getToMember().getName());
			toTransfer.setUsername(terminal.getToMember().getUsername());
			pir.setToMember(toTransfer);

			TerminalView tv = new TerminalView();
			tv.setId(terminal.getId());
			tv.setAddress(terminal.getAddress());
			tv.setName(terminal.getName());
			tv.setCity(terminal.getCity());
			tv.setPostalCode(terminal.getPostalCode());
			tv.setNnsID(terminal.getNnsID());
			tv.setMerchantCategoryCode(terminal.getMerchantCategoryCode());

			pir.setTerminal(tv);
			pir.setStatus(StatusBuilder.getStatus(Status.PROCESSED));

			return pir;
		} catch (TransactionException e) {
			pir.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return pir;
		}
	}

	@Override
	public PosPaymentResponse posPayment(Holder<Header> headerParam, PosPaymentRequest req) throws Exception {
		PosPaymentResponse ppr = new PosPaymentResponse();
		IMap<String, GeneratePaymentTicketRequest> genMap = instance.getMap("GeneratePaymentMap");
		PaymentRequest pr = new PaymentRequest();
		PaymentDetails pd = null;
		try {
			Terminal terminal = posPaymentValidation.validatePosPayment(req);

			if (req.getTicketID() == null) {
				ppr.setStatus(StatusBuilder.getStatus(Status.INVALID_PARAMETER));
				return ppr;
			}
			GeneratePaymentTicketRequest gpt = genMap.get(req.getTicketID());
			if (gpt == null) {
				ppr.setStatus(StatusBuilder.getStatus(Status.PAYMENT_CODE_NOT_FOUND));
				return ppr;
			}

			pr.setAmount(gpt.getAmount());
			pr.setReferenceNumber(gpt.getInvoiceNumber());

			pr.setDescription(req.getDescription() + " " + terminal.getName());
			pr.setFromMember(req.getFromMember());
			pr.setOriginator(req.getOriginator());
			pr.setReferenceNumber(terminal.getId().toString());
			pr.setToMember(req.getToMember());
			pr.setTraceNumber(req.getTraceNumber());
			pr.setTransferTypeID(terminal.getTransferTypeID());
			pd = paymentValidation.validatePosPayment(pr, headerParam.value.getToken(), "PROCESSED");

			if (pd != null) {
				List<Notifications> ln = baseRepository.getTransferTypeRepository()
						.loadNotificationByTransferType(pd.getTransferType().getId());
				pd.setNotification(ln);
				pd.setTerminal(terminal);
				MuleClient client = new MuleClient(configurator.getMuleContext());
				Map<String, Object> header = new HashMap<String, Object>();
				client.dispatch("NotificationVM", pd, header);
			}

			TerminalView tv = new TerminalView();
			tv.setId(terminal.getId());
			tv.setAddress(terminal.getAddress());
			tv.setName(terminal.getName());
			tv.setCity(terminal.getCity());
			tv.setPostalCode(terminal.getPostalCode());
			tv.setNnsID(terminal.getNnsID());
			tv.setMerchantCategoryCode(terminal.getMerchantCategoryCode());
			ppr.setTerminal(tv);

			ppr.setId(pd.getTransferID());
			ppr.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			ppr.setAmount(pd.getFees().getFinalAmount());
			ppr.setDescription(pd.getRequest().getDescription());

			MemberView fromTransfer = new MemberView();
			fromTransfer.setId(pd.getFromMember().getId());
			fromTransfer.setName(pd.getFromMember().getName());
			fromTransfer.setUsername(pd.getFromMember().getUsername());
			ppr.setFromMember(fromTransfer);
			ppr.setPaymentFields(pd.getRequest().getPaymentFields());

			MemberView toTransfer = new MemberView();
			toTransfer.setId(pd.getToMember().getId());
			toTransfer.setName(pd.getToMember().getName());
			toTransfer.setUsername(pd.getToMember().getUsername());

			TransferTypeFields typeField = new TransferTypeFields();
			typeField.setFromAccounts(pd.getFromAccount().getId());
			typeField.setId(pd.getTransferType().getId());
			typeField.setName(pd.getTransferType().getName());
			typeField.setToAccounts(pd.getToAccount().getId());

			ppr.setTransferType(typeField);
			ppr.setToMember(toTransfer);
			ppr.setTraceNumber(pd.getRequest().getTraceNumber());
			ppr.setTransactionNumber(pd.getTransactionNumber());

			ppr.setAmount(pd.getFees().getTransactionAmount());
			ppr.setFinalAmount(pd.getFees().getFinalAmount());
			ppr.setTotalFees(pd.getFees().getTotalFees());
			return ppr;
		} catch (TransactionException e) {
			ppr.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return ppr;
		} catch (SocketTimeoutException ex) {
			ppr.setStatus(StatusBuilder.getStatus(Status.REQUEST_TIMEOUT));
			return ppr;
		} catch (MuleException e) {
			ppr.setStatus(StatusBuilder.getStatus(Status.UNKNOWN_ERROR));
			return ppr;
		} finally {
			genMap.remove(req.getTicketID());
		}
	}

	@Override
	public PosCreateInvoiceResponse posGenerateInvoice(Holder<Header> headerParam, PosCreateInvoiceRequest req)
			throws Exception {
		PosCreateInvoiceResponse pci = new PosCreateInvoiceResponse();
		try {
			Terminal terminal = posPaymentValidation.validatePosTerminal(req, headerParam.value.getToken());

			// String ticket = UUID.randomUUID().toString();
			String ticket = RandomStringUtils.random(25, true, true);

			GeneratePaymentTicketRequest gpt = new GeneratePaymentTicketRequest();
			gpt.setAmount(req.getAmount());
			gpt.setToMember(req.getToMember());
			gpt.setTransferTypeID(terminal.getTransferTypeID());
			gpt.setDescription(req.getTerminalID().toString());
			gpt.setInvoiceNumber(req.getInvoiceNumber());
			IMap<String, GeneratePaymentTicketRequest> genMap = instance.getMap("GeneratePaymentMap");
			genMap.put(ticket, gpt);

			TerminalView tv = new TerminalView();
			tv.setId(terminal.getId());
			tv.setAddress(terminal.getAddress());
			tv.setName(terminal.getName());
			tv.setCity(terminal.getCity());
			tv.setPostalCode(terminal.getPostalCode());
			tv.setNnsID(terminal.getNnsID());
			tv.setMerchantCategoryCode(terminal.getMerchantCategoryCode());

			MemberView toTransfer = new MemberView();
			toTransfer.setId(terminal.getToMember().getId());
			toTransfer.setName(terminal.getToMember().getName());
			toTransfer.setUsername(terminal.getToMember().getUsername());

			pci.setInvoiceID(req.getInvoiceNumber());
			pci.setTerminal(tv);
			pci.setTicketID(ticket);
			pci.setToMember(toTransfer);
			pci.setAmount(req.getAmount());

			pci.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return pci;
		} catch (TransactionException e) {
			pci.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return pci;
		}
	}

	@Override
	public void registerPOS(Holder<Header> headerParam, RegisterPOSRequest req) throws Exception {
		webserviceValidation.validateWebservice(headerParam.value.getToken());
		Members member = memberValidation.validateMember(req.getUsername(), true);
		transferTypeValidation.validateTransferType(req.getTransferTypeID());
		baseRepository.getPosRepository().registerPOS(req, member.getId());
	}

	@Override
	public void updatePOS(Holder<Header> headerParam, UpdatePOSRequest req) throws Exception {
		Terminal terminal = posPaymentValidation.validatePosTerminal(req, headerParam.value.getToken());

		if (req.getAddress() == null) {
			req.setAddress(terminal.getAddress());
		}

		if (req.getCity() == null) {
			req.setCity(terminal.getCity());
		}

		if (req.getEmail() == null) {
			req.setEmail(terminal.getEmail());
		}

		if (req.getMsisdn() == null) {
			req.setMsisdn(terminal.getMsisdn());
		}

		if (req.getName() == null) {
			req.setName(terminal.getName());
		}

		if (req.getPic() == null) {
			req.setPic(terminal.getPic());
		}

		if (req.getPostalCode() == null) {
			req.setPostalCode(terminal.getPostalCode());
		}

		if (req.getTransferTypeID() == null) {
			req.setTerminalID(terminal.getTransferTypeID());
		}
		
		if(req.getMerchantCategoryCode() == null) {
			req.setMerchantCategoryCode(terminal.getMerchantCategoryCode());
		}
		
		baseRepository.getPosRepository().updatePOS(req, terminal.getToMember().getId());
	}

	@Override
	public void deletePOS(Holder<Header> headerParam, DeletePOSRequest req) throws Exception {
		Terminal terminal = posPaymentValidation.validatePosTerminal(req, headerParam.value.getToken());
		baseRepository.getPosRepository().deletePOS(req, terminal.getToMember().getId());
	}

	@Override
	@Transactional
	public PaymentResponse doPaymentQRIS(Holder<Header> headerParam, PaymentRequest req) {
		PaymentResponse pr = new PaymentResponse();
		PaymentDetails pd = null;
		try {
			String trxState = "";
			if (req.getStatus() == null || req.getStatus().equalsIgnoreCase("")) {
				trxState = "PROCESSED";
			} else {
				trxState = "PENDING";
			}
			pd = posPaymentValidation.validatePayment(req, headerParam.value.getToken(), trxState);
			if (pd != null) {
				List<Notifications> ln = baseRepository.getTransferTypeRepository()
						.loadNotificationByTransferType(pd.getTransferType().getId());
				pd.setNotification(ln);
				MuleClient client = new MuleClient(configurator.getMuleContext());
				Map<String, Object> header = new HashMap<String, Object>();
				client.dispatch("NotificationVM", pd, header);
			}

			pr.setId(pd.getTransferID());
			pr.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			pr.setAmount(pd.getFees().getFinalAmount());
			pr.setDescription(pd.getRequest().getDescription());
			MemberView fromTransfer = new MemberView();
			fromTransfer.setId(pd.getFromMember().getId());
			fromTransfer.setName(pd.getFromMember().getName());
			fromTransfer.setUsername(pd.getFromMember().getUsername());
			pr.setFromMember(fromTransfer);
			pr.setPaymentFields(pd.getRequest().getPaymentFields());
			MemberView toTransfer = new MemberView();
			toTransfer.setId(pd.getToMember().getId());
			toTransfer.setName(pd.getToMember().getName());
			toTransfer.setUsername(pd.getToMember().getUsername());
			TransferTypeFields typeField = new TransferTypeFields();
			typeField.setFromAccounts(pd.getFromAccount().getId());
			typeField.setId(pd.getTransferType().getId());
			typeField.setName(pd.getTransferType().getName());
			typeField.setToAccounts(pd.getToAccount().getId());
			pr.setTransferType(typeField);
			pr.setToMember(toTransfer);
			pr.setTraceNumber(pd.getRequest().getTraceNumber());
			pr.setTransactionNumber(pd.getTransactionNumber());
			return pr;

		} catch (TransactionException e) {
			pr.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return pr;
		} catch (SocketTimeoutException ex) {
			pr.setStatus(StatusBuilder.getStatus(Status.REQUEST_TIMEOUT));
			return pr;
		} catch (MuleException e) {
			pr.setStatus(StatusBuilder.getStatus(Status.UNKNOWN_ERROR));
			return pr;
		}
	}

}

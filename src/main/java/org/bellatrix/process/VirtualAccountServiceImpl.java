package org.bellatrix.process;

import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import javax.xml.ws.Holder;

import org.apache.log4j.Logger;
import org.bellatrix.auth.HashProcessor;
import org.bellatrix.data.Header;
import org.bellatrix.data.Members;
import org.bellatrix.data.Notifications;
import org.bellatrix.data.RegisterVADoc;
import org.bellatrix.data.Status;
import org.bellatrix.data.StatusBuilder;
import org.bellatrix.data.TransactionException;
import org.bellatrix.data.VADetails;
import org.bellatrix.data.VAEvent;
import org.bellatrix.data.VAEventDoc;
import org.bellatrix.data.VARecordView;
import org.bellatrix.services.CreateVAEventRequest;
import org.bellatrix.services.CreateVAEventResponse;
import org.bellatrix.services.CredentialResponse;
import org.bellatrix.services.DeleteVAEventRequest;
import org.bellatrix.services.LoadVAByIDRequest;
import org.bellatrix.services.LoadVAByIDResponse;
import org.bellatrix.services.LoadVAByMemberRequest;
import org.bellatrix.services.LoadVAByMemberResponse;
import org.bellatrix.services.LoadVAEventRequest;
import org.bellatrix.services.LoadVAEventResponse;
import org.bellatrix.services.LoadVAStatusByMemberRequest;
import org.bellatrix.services.LoadVAStatusByMemberResponse;
import org.bellatrix.services.PaymentDetails;
import org.bellatrix.services.PaymentRequest;
import org.bellatrix.services.UpdateBillingStatusRequest;
import org.bellatrix.services.VABankRequest;
import org.bellatrix.services.VABankResponse;
import org.bellatrix.services.VADeleteRequest;
import org.bellatrix.services.VAInquiryRequest;
import org.bellatrix.services.VAInquiryResponse;
import org.bellatrix.services.VAPaymentRequest;
import org.bellatrix.services.VAPaymentResponse;
import org.bellatrix.services.VARegisterBankRequest;
import org.bellatrix.services.VARegisterBankResponse;
import org.bellatrix.services.VARegisterRequest;
import org.bellatrix.services.VARegisterResponse;
import org.bellatrix.services.VAUpdateRequest;
import org.bellatrix.services.VAUpdateResponse;
import org.bellatrix.services.VirtualAccount;
import org.mule.api.MuleException;
import org.mule.module.client.MuleClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

public class VirtualAccountServiceImpl implements VirtualAccount {

	@Autowired
	private VirtualAccountValidation virtualAccountValidation;
	@Autowired
	private PaymentValidation paymentValidation;
	@Autowired
	private BaseRepository baseRepository;
	@Autowired
	private Configurator configurator;
	@Value("${secret.auth.access.type.id}")
	private Integer secretAuthID;
	private Logger logger = Logger.getLogger(VirtualAccountServiceImpl.class);
	// @Autowired
	// private HazelcastInstance instance;

	@Override
	public VARegisterResponse registerVA(Holder<Header> headerParam, VARegisterRequest req) {
		// IMap<String, RegisterVADoc> mapRVAMap = instance.getMap("RegisterVAMap");
		try {
			VARegisterResponse var = new VARegisterResponse();
			VADetails vad = virtualAccountValidation.validateVARequest(headerParam.value.getToken(), req);
			String tktID = UUID.randomUUID().toString();
			String ticketID = "";
			if (tktID.length() > 30) {
				ticketID = tktID.substring(0, 30);
			} else {
				ticketID = tktID;
			}

			RegisterVADoc rva = new RegisterVADoc();
			rva.setAmount(req.getAmount());
			rva.setBankID(req.getBankID());
			rva.setCallbackURL(req.getCallbackURL());
			rva.setFullPayment(req.isFullPayment());
			rva.setMember(vad.getToMember());
			rva.setName(req.getName());
			rva.setPersistent(req.isPersistent());
			rva.setTransferType(vad.getTrxType());
			rva.setBankCode(vad.getVirtualAccount().getBankCode());
			rva.setBankName(vad.getVirtualAccount().getBankName());
			rva.setMinimumPayment(req.getMinimumPayment());
			rva.setTicketID(ticketID);
			rva.setId(vad.getPaymentCode());
			rva.setReferenceNumber(req.getReferenceNumber());
			rva.setCreatedDate(LocalDateTime.now());
			rva.setEmail(req.getEmail());
			rva.setMsisdn(req.getMobileNo());
			rva.setFromMember(vad.getFromMember());
			rva.setPrivateField(req.getPrivateField());

			var.setBankCode(vad.getVirtualAccount().getBankCode());
			var.setBankName(vad.getVirtualAccount().getBankName());
			var.setPaymentCode(vad.getPaymentCode());
			var.setReferenceNumber(req.getReferenceNumber());
			var.setPersistent(req.isPersistent());
			var.setTicketID(ticketID);
			var.setStatus(StatusBuilder.getStatus(Status.PROCESSED));

			if (!req.isPersistent()) {
				Date expired = req.getExpiredDateTime();
				LocalDateTime timePoint = LocalDateTime.ofInstant(expired.toInstant(), ZoneId.systemDefault());
				logger.info("EXPIRED DATE DB: " + expired);
				logger.info("EXPIRED DATE MONGO: " + timePoint);
				var.setExpiredAt(expired);
				rva.setExpiredAt(timePoint);
			} else {
				var.setExpiredAt(null);
				rva.setExpiredAt(null);
			}
			/*
			 * SAVE TO HAZELCAST
			 */
			// mapRVAMap.put(vad.getPaymentCode(), rva, arg2, arg3)
			baseRepository.getPersistenceRepository().create(rva);

			baseRepository.getVirtualAccountRepository().registerBillingVA(req.getEventID(), req.getDescription(), rva);

			/*
			 * SEND EMAIL HERE
			 */
			Notifications notif = new Notifications();
			notif.setModuleURL("notification.email");
			notif.setNotificationType("billingPayment");

			List<Notifications> lm = new LinkedList<Notifications>();
			lm.add(notif);

			HashMap<String, Object> notifMap = new HashMap<String, Object>();
			notifMap.put("notification", lm);
			notifMap.put("email", req.getEmail());
			notifMap.put("msisdn", req.getMobileNo());
			notifMap.put("paymentCode", vad.getPaymentCode());
			notifMap.put("amount", Utils.formatAmount(req.getAmount()));
			notifMap.put("billingName", req.getName());
			notifMap.put("to", vad.getToMember().getName());
			if (!req.isPersistent()) {
				Date expired = req.getExpiredDateTime();
				notifMap.put("expiredAt", expired);
			}

			MuleClient client;
			client = new MuleClient(configurator.getMuleContext());
			Map<String, Object> header = new HashMap<String, Object>();
			client.dispatch("NotificationVM", notifMap, header);

			return var;
		} catch (TransactionException ex) {
			VARegisterResponse var = new VARegisterResponse();
			var.setStatus(StatusBuilder.getStatus(ex.getMessage()));
			return var;
		} catch (DuplicateKeyException ex) {
			VARegisterResponse var = new VARegisterResponse();
			var.setStatus(StatusBuilder.getStatus(Status.DUPLICATE_TRANSACTION));
			return var;
		} catch (MuleException e) {
			VARegisterResponse var = new VARegisterResponse();
			var.setStatus(StatusBuilder.getStatus(Status.UNKNOWN_ERROR));
			return var;
		}
	}

	@Override
	public VAInquiryResponse inquiryVA(Holder<Header> headerParam, VAInquiryRequest req) {
		VAInquiryResponse vir = new VAInquiryResponse();
		try {
			RegisterVADoc vadoc = virtualAccountValidation.validateVAPaymentCode(headerParam.value.getToken(),
					req.getPaymentCode(), req.getUsername());

			vir.setAmount(vadoc.getAmount());
			vir.setName(vadoc.getName());
			vir.setPaymentCode(req.getPaymentCode());
			vir.setBankCode(vadoc.getBankCode());
			vir.setBankName(vadoc.getBankName());
			vir.setPersistent(vadoc.isPersistent());
			vir.setReferenceNumber(vadoc.getReferenceNumber());
			vir.setOriginator(vadoc.getMember());

			if (!vadoc.isPersistent()) {
				vir.setExpiredAt(Date.from(vadoc.getExpiredAt().atZone(ZoneId.systemDefault()).toInstant()));
			}

			vir.setFullPayment(vadoc.isFullPayment());
			if (!vadoc.isFullPayment()) {
				vir.setMinimumPayment(vadoc.getMinimumPayment());
			}

			vir.setTicketID(vadoc.getTicketID());
			vir.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return vir;
		} catch (TransactionException ex) {
			vir.setStatus(StatusBuilder.getStatus(ex.getMessage()));
			return vir;
		}
	}

	@Override
	public VAPaymentResponse paymentVA(Holder<Header> headerParam, VAPaymentRequest req) {
		String trxState = "";
		VAPaymentResponse vpr = new VAPaymentResponse();
		try {
			VADetails va = virtualAccountValidation.validateVAPayment(headerParam.value.getToken(), req);

			PaymentRequest preq = new PaymentRequest();
			preq.setBillingID(Integer.parseInt(va.getRegVA().getId()));
			preq.setAmount(req.getAmount());
			preq.setToMember(va.getRegVA().getMember().getUsername());
			preq.setTraceNumber(req.getTraceNumber());
			preq.setOriginator(va.getRegVA().getMember().getUsername());
			
			if (req.getTransactionDate() != null) {
				Date trxDate = req.getTransactionDate();
				logger.info("TRX DATE INQ: "+trxDate);
				preq.setTransactionDate(trxDate);
			} else {
				Date trxDate = new Date();
				preq.setTransactionDate(trxDate);
			}

			if (req.getTransferTypeID() != null) {
				if (va.getFromMember().getGroupID() == 17) {
					trxState = "PENDING";
				} else {
					trxState = "PROCESSED";
				}
				preq.setDescription("Billing Payment: " + va.getPaymentCode() + ", RefNo : "
						+ va.getRegVA().getReferenceNumber() + ", Name : " + va.getRegVA().getName());
				preq.setTransferTypeID(req.getTransferTypeID());
				preq.setFromMember(req.getFromMember());
				preq.setReferenceNumber(va.getPaymentCode());
			} else {
				preq.setDescription(
						"RefNo : " + va.getRegVA().getReferenceNumber() + ", Name : " + va.getRegVA().getName());
				preq.setTransferTypeID(va.getRegVA().getTransferType().getId());
				preq.setFromMember(va.getFromMember().getUsername());
				preq.setReferenceNumber(va.getPaymentCode());
				trxState = "PROCESSED";
			}

			PaymentDetails pd = paymentValidation.validatePayment(preq, headerParam.value.getToken(), trxState);

			if (pd != null) {
				Members members = new Members();
				members.setId(pd.getFromMember().getId());
				members.setEmail(va.getRegVA().getEmail());
				members.setName(pd.getFromMember().getName());
				pd.setFromMember(members);
				List<Notifications> ln = baseRepository.getTransferTypeRepository()
						.loadNotificationByTransferType(pd.getTransferType().getId());
				pd.setNotification(ln);
				MuleClient client = new MuleClient(configurator.getMuleContext());
				Map<String, Object> header = new HashMap<String, Object>();
				client.dispatch("NotificationVM", pd, header);
			}

			if (preq.getTransferTypeID() == null) {
				vpr.setBankCode(va.getRegVA().getBankCode());
				vpr.setBankName(va.getRegVA().getBankName());
			}
			vpr.setCallbackURL(va.getRegVA().getCallbackURL());
			vpr.setOriginator(va.getRegVA().getMember());
			if (!va.getRegVA().isPersistent()) {
				vpr.setExpiredAt(Date.from(va.getRegVA().getExpiredAt().atZone(ZoneId.systemDefault()).toInstant()));
			}
			vpr.setPaymentCode(req.getPaymentCode());
			vpr.setPersistent(va.getRegVA().isPersistent());
			vpr.setTraceNumber(req.getTraceNumber());
			vpr.setTransactionNumber(pd.getTransactionNumber());
			vpr.setReferenceNumber(va.getRegVA().getReferenceNumber());
			vpr.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			vpr.setName(va.getRegVA().getName());
			vpr.setUsername(va.getRegVA().getReferenceNumber());
			vpr.setTransactionDate(pd.getTransactionDate());

			TreeMap<String, String> vaNotif = new TreeMap<String, String>();
			vaNotif.put("transferID", String.valueOf(pd.getTransferID()));
			vaNotif.put("notificationDate", LocalDateTime.now().toString());
			vaNotif.put("paymentCode", va.getPaymentCode());
			vaNotif.put("referenceNumber", va.getRegVA().getReferenceNumber());
			vaNotif.put("transactionNumber", pd.getTransactionNumber());
			vaNotif.put("amount", pd.getRequest().getAmount().toPlainString());
			vaNotif.put("callbackURL", va.getRegVA().getCallbackURL());

			if (preq.getTransferTypeID() == null) {
				vaNotif.put("bankCode", va.getRegVA().getBankCode());
				vaNotif.put("bankName", va.getRegVA().getBankName());
			}

			CredentialResponse secret = baseRepository.getAccessRepository()
					.getSecretFromMemberID(va.getRegVA().getMember().getId(), secretAuthID);

			HashProcessor hp = new HashProcessor();
			String requestAuth = hp.encodeHash(vaNotif, secret.getCredential());

			if (va.getRegVA().getCallbackURL() != null) {
				MuleClient client;
				client = new MuleClient(configurator.getMuleContext());
				Map<String, Object> header = new HashMap<String, Object>();
				header.put("requestAuth", requestAuth);
				header.put("NOTIFICATION_ORIGIN", va.getRegVA().getMember().getUsername());
				client.dispatch("VANotificationVM", vaNotif, header);
			}

			baseRepository.getMessageRepository().sendMessage(pd.getFromMember().getId(), pd.getToMember().getId(),
					pd.getTransferType().getName() + " " + va.getRegVA().getPaymentCode(),
					"You have received payment " + Utils.formatAmount(pd.getRequest().getAmount()) + " from : "
							+ va.getRegVA().getName() + " (" + va.getRegVA().getReferenceNumber()
							+ "). Transaction number : " + pd.getTransactionNumber());

			if (va.getRegVA().isPersistent() == false) {
				baseRepository.getPersistenceRepository()
						.delete(new Query(Criteria.where("_id").is(req.getPaymentCode())), RegisterVADoc.class);
			}

			return vpr;
		} catch (TransactionException ex) {
			vpr.setStatus(StatusBuilder.getStatus(ex.getMessage()));
			return vpr;
		} catch (SocketTimeoutException e) {
			vpr.setStatus(StatusBuilder.getStatus(Status.REQUEST_TIMEOUT));
			return vpr;
		} catch (MuleException e) {
			vpr.setStatus(StatusBuilder.getStatus(Status.UNKNOWN_ERROR));
			return vpr;
		}
	}

	@Override
	public void deleteVA(Holder<Header> headerParam, VADeleteRequest req) throws Exception {
		try {
			virtualAccountValidation.validateVADeletion(headerParam.value.getToken(), req);
		} catch (TransactionException ex) {
			throw new Exception(ex.getMessage());
		}
	}

	@Override
	public VAUpdateResponse updateVA(Holder<Header> headerParam, VAUpdateRequest req) {
		try {
			VAUpdateResponse var = new VAUpdateResponse();
			RegisterVADoc rva = virtualAccountValidation.validateVAPaymentCode(headerParam.value.getToken(),
					req.getPaymentCode(), req.getUsername());
			String ticketID = UUID.randomUUID().toString();

			if (req.getAmount().compareTo(BigDecimal.ZERO) != 0 && req.getAmount().compareTo(rva.getAmount()) != 0) {
				rva.setAmount(req.getAmount());
			}

			if (req.getCallbackURL() != null && !req.getCallbackURL().equalsIgnoreCase(rva.getCallbackURL())) {
				rva.setCallbackURL(req.getCallbackURL());
			}

			if (req.getName() != null && !req.getName().equalsIgnoreCase(rva.getName())) {
				rva.setName(req.getName());
			}

			if (req.isPersistent() != null && req.isPersistent() != rva.isPersistent()) {
				rva.setPersistent(req.isPersistent());
				if (!rva.isPersistent()) {
					LocalDateTime timePoint = LocalDateTime.now();
					timePoint = timePoint.plusMinutes(req.getExpiredAtMinute());
					rva.setExpiredAt(timePoint);
				} else {
					rva.setExpiredAt(null);
				}
			}

			if (req.isFullPayment() != null && req.isFullPayment() != rva.isFullPayment()) {
				rva.setFullPayment(req.isFullPayment());
				if (!rva.isFullPayment() && req.getMinimumPayment() != null) {
					rva.setMinimumPayment(req.getMinimumPayment());
				} else {
					rva.setMinimumPayment(BigDecimal.ZERO);
				}
			}

			rva.setTicketID(ticketID);

			var.setPaymentCode(req.getPaymentCode());
			var.setTicketID(ticketID);
			var.setStatus(StatusBuilder.getStatus(Status.PROCESSED));

			baseRepository.getPersistenceRepository().update(rva);

			return var;
		} catch (TransactionException ex) {
			VAUpdateResponse var = new VAUpdateResponse();
			var.setStatus(StatusBuilder.getStatus(ex.getMessage()));
			return var;
		} catch (DuplicateKeyException ex) {
			VAUpdateResponse var = new VAUpdateResponse();
			var.setStatus(StatusBuilder.getStatus(Status.DUPLICATE_TRANSACTION));
			return var;
		}
	}

	@Override
	public VARegisterBankResponse registerBankVA(Holder<Header> headerParam, VARegisterBankRequest req) {
		VARegisterBankResponse vab = new VARegisterBankResponse();
		try {
			BankVA bankVA = virtualAccountValidation.validateBankVA(headerParam.value.getToken(), req);
			if (bankVA != null) {
				vab.setStatus(StatusBuilder.getStatus(Status.MEMBER_ALREADY_REGISTERED));
				return vab;
			}
			vab.setBank(bankVA);
			vab.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
		} catch (Exception ex) {
			vab.setStatus(StatusBuilder.getStatus(String.valueOf(ex.getMessage())));
		}
		return vab;
	}

	@Override
	public VABankResponse loadBankVA(Holder<Header> headerParam, VABankRequest req) {
		VABankResponse vab = new VABankResponse();
		try {
			List<BankVA> bankVA = virtualAccountValidation.validateBankVA(headerParam.value.getToken(), req);
			vab.setBank(bankVA);
			vab.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
		} catch (TransactionException ex) {
			vab.setStatus(StatusBuilder.getStatus(String.valueOf(ex.getMessage())));
		}
		return vab;
	}

	@Override
	public VABankResponse listBankVA(Holder<Header> headerParam, VABankRequest req) {
		VABankResponse vab = new VABankResponse();
		try {
			List<BankVA> bankVA = virtualAccountValidation.validateListBankVA(headerParam.value.getToken(), req);
			vab.setBank(bankVA);
			vab.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
		} catch (TransactionException ex) {
			vab.setStatus(StatusBuilder.getStatus(String.valueOf(ex.getMessage())));
		}
		return vab;
	}

	@Override
	public LoadVAByMemberResponse loadVAByMember(Holder<Header> headerParam, LoadVAByMemberRequest req) {
		LoadVAByMemberResponse loadVAByMemberResponse = new LoadVAByMemberResponse();
		try {
			List<RegisterVADoc> listVA = virtualAccountValidation.validateLoadVA(headerParam.value.getToken(), req);
			List<VARecordView> listVaView = new LinkedList<VARecordView>();
			for (int i = 0; i < listVA.size(); i++) {
				VARecordView vaView = new VARecordView();
				vaView.setAmount(listVA.get(i).getAmount());
				vaView.setFormattedAmount(Utils.formatAmount(listVA.get(i).getAmount()));
				vaView.setBankCode(listVA.get(i).getBankCode());
				vaView.setBankID(listVA.get(i).getBankID());
				vaView.setBankName(listVA.get(i).getBankName());
				vaView.setCallbackURL(listVA.get(i).getCallbackURL());

				if (listVA.get(i).isPersistent() == false && listVA.get(i).getExpiredAt() != null) {
					Date date = Date.from(listVA.get(i).getExpiredAt().atZone(ZoneId.systemDefault()).toInstant());
					vaView.setExpiredAt(date);
					vaView.setFormattedExpiredAt(Utils.formatDate(date));
				}

				vaView.setFullPayment(listVA.get(i).isFullPayment());
				vaView.setId(listVA.get(i).getId());
				vaView.setMinimumPayment(listVA.get(i).getMinimumPayment());
				vaView.setName(listVA.get(i).getName());
				vaView.setParentUsername(listVA.get(i).getMember().getUsername());
				vaView.setPersistent(listVA.get(i).isPersistent());
				vaView.setReferenceNumber(listVA.get(i).getReferenceNumber());
				vaView.setTicketID(listVA.get(i).getTicketID());
				vaView.setTransferType(listVA.get(i).getTransferType());
				listVaView.add(vaView);
			}

			Long totalRecord = baseRepository.getPersistenceRepository()
					.count(new Query(Criteria.where("member.username").is(req.getUsername())), RegisterVADoc.class);

			loadVAByMemberResponse.setTotalRecords(totalRecord);
			loadVAByMemberResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			loadVAByMemberResponse.setVaRecord(listVaView);
			return loadVAByMemberResponse;
		} catch (TransactionException ex) {
			loadVAByMemberResponse.setStatus(StatusBuilder.getStatus(String.valueOf(ex.getMessage())));
			return loadVAByMemberResponse;
		}
	}

	@Override
	public LoadVAEventResponse loadVAEvent(Holder<Header> headerParam, LoadVAEventRequest req) {
		LoadVAEventResponse loadVAEventResponse = new LoadVAEventResponse();
		try {
			List<VAEventDoc> vadoc = virtualAccountValidation.validateLoadVAEvent(headerParam.value.getToken(),
					req.getUsername(), req.getCurrentPage(), req.getPageSize());

			Long totalRecord = baseRepository.getPersistenceRepository()
					.count(new Query(Criteria.where("member.username").is(req.getUsername())), VAEventDoc.class);
			List<VAEvent> listEvent = new LinkedList<VAEvent>();
			for (int i = 0; i < vadoc.size(); i++) {
				VAEvent event = new VAEvent();
				event.setUsername(vadoc.get(i).getMember().getUsername());
				event.setAmount(vadoc.get(i).getAmount());
				event.setFormattedAmount(Utils.formatAmount(vadoc.get(i).getAmount()));
				event.setDescription(vadoc.get(i).getDescription());
				event.setEventName(vadoc.get(i).getEventName());
				event.setTicketID(vadoc.get(i).getId());
				Date date = Date.from(vadoc.get(i).getExpiredAt().atZone(ZoneId.systemDefault()).toInstant());
				event.setExpiredAt(date);
				event.setFormattedExpiredAt(Utils.formatDate(date));
				listEvent.add(event);
			}

			loadVAEventResponse.setTotalRecords(totalRecord);
			loadVAEventResponse.setEvent(listEvent);
			loadVAEventResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
		} catch (TransactionException e) {
			loadVAEventResponse.setStatus(StatusBuilder.getStatus(e.getMessage()));
		}
		return loadVAEventResponse;
	}

	@Override
	public CreateVAEventResponse createVAEvent(Holder<Header> headerParam, CreateVAEventRequest req) {
		CreateVAEventResponse createVAEventResponse = new CreateVAEventResponse();
		try {
			Members member = virtualAccountValidation.validateVAEvent(headerParam.value.getToken(), req.getUsername());
			String ticketID = UUID.randomUUID().toString();
			VAEventDoc vadoc = new VAEventDoc();
			vadoc.setMember(member);
			vadoc.setAmount(req.getAmount());
			vadoc.setDescription(req.getDescription());
			vadoc.setEventName(req.getEventName());
			vadoc.setId(ticketID);
			vadoc.setCallbackURL(req.getCallbackURL());
			vadoc.setCreatedDate(LocalDateTime.now());

			Date expired = req.getExpiredDateTime();
			LocalDateTime timePoint = LocalDateTime.ofInstant(expired.toInstant(), ZoneId.systemDefault());
			vadoc.setExpiredAt(timePoint);
			Integer eventID = baseRepository.getVirtualAccountRepository().registerEventVA(member.getId(), ticketID,
					req.getEventName(), req.getDescription(), timePoint);
			vadoc.setEventID(eventID);
			baseRepository.getPersistenceRepository().create(vadoc);
			createVAEventResponse.setTicketID(ticketID);
			createVAEventResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));

		} catch (TransactionException ex) {
			createVAEventResponse.setStatus(StatusBuilder.getStatus(ex.getMessage()));
			return createVAEventResponse;
		}
		return createVAEventResponse;
	}

	@Override
	public void deleteVAEvent(Holder<Header> headerParam, DeleteVAEventRequest req) throws Exception {
		virtualAccountValidation.validateVAEventDelete(headerParam.value.getToken(), req.getUsername(),
				req.getTicketID());
	}

	@Override
	public LoadVAEventResponse loadVAEventByID(Holder<Header> headerParam, LoadVAEventRequest req) {
		LoadVAEventResponse loadVAEventResponse = new LoadVAEventResponse();
		try {
			VAEventDoc vadoc = virtualAccountValidation.validateLoadVAEventByID(headerParam.value.getToken(),
					req.getUsername(), req.getTicketID());
			List<VAEvent> listEvent = new LinkedList<VAEvent>();
			VAEvent event = new VAEvent();
			event.setAmount(vadoc.getAmount());
			event.setDescription(vadoc.getDescription());
			event.setEventName(vadoc.getEventName());
			event.setTicketID(vadoc.getId());
			event.setUsername(vadoc.getMember().getUsername());
			Date date = Date.from(vadoc.getExpiredAt().atZone(ZoneId.systemDefault()).toInstant());
			event.setExpiredAt(date);
			event.setFormattedExpiredAt(Utils.formatDate(date));
			event.setFormattedAmount(Utils.formatAmount(vadoc.getAmount()));
			listEvent.add(event);
			loadVAEventResponse.setEvent(listEvent);
			loadVAEventResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
		} catch (TransactionException ex) {
			loadVAEventResponse.setStatus(StatusBuilder.getStatus(ex.getMessage()));
			return loadVAEventResponse;
		}
		return loadVAEventResponse;
	}

	@Override
	public LoadVAByIDResponse loadVAByID(Holder<Header> headerParam, LoadVAByIDRequest req) {
		LoadVAByIDResponse loadVAByIDResponse = new LoadVAByIDResponse();
		try {
			List<RegisterVADoc> listVA = virtualAccountValidation.validateLoadVA(headerParam.value.getToken(), req);
			List<VARecordView> listVaView = new LinkedList<VARecordView>();
			for (int i = 0; i < listVA.size(); i++) {
				VARecordView vaView = new VARecordView();
				vaView.setAmount(listVA.get(i).getAmount());
				vaView.setFormattedAmount(Utils.formatAmount(listVA.get(i).getAmount()));
				vaView.setBankCode(listVA.get(i).getBankCode());
				vaView.setBankID(listVA.get(i).getBankID());
				vaView.setBankName(listVA.get(i).getBankName());
				vaView.setCallbackURL(listVA.get(i).getCallbackURL());

				if (listVA.get(i).isPersistent() == false && listVA.get(i).getExpiredAt() != null) {
					Date date = Date.from(listVA.get(i).getExpiredAt().atZone(ZoneId.systemDefault()).toInstant());
					vaView.setExpiredAt(date);
					vaView.setFormattedExpiredAt(Utils.formatDate(date));
				}

				vaView.setFullPayment(listVA.get(i).isFullPayment());
				vaView.setId(listVA.get(i).getId());
				vaView.setMinimumPayment(listVA.get(i).getMinimumPayment());
				vaView.setName(listVA.get(i).getName());
				vaView.setParentUsername(listVA.get(i).getMember().getUsername());
				vaView.setPersistent(listVA.get(i).isPersistent());
				vaView.setReferenceNumber(listVA.get(i).getReferenceNumber());
				vaView.setTicketID(listVA.get(i).getTicketID());
				vaView.setTransferType(listVA.get(i).getTransferType());
				listVaView.add(vaView);
			}

			loadVAByIDResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			loadVAByIDResponse.setVaRecord(listVaView);
			return loadVAByIDResponse;
		} catch (TransactionException ex) {
			loadVAByIDResponse.setStatus(StatusBuilder.getStatus(String.valueOf(ex.getMessage())));
			return loadVAByIDResponse;
		}
	}

	@Override
	public LoadVAStatusByMemberResponse loadVAByMemberStatus(Holder<Header> headerParam,
			LoadVAStatusByMemberRequest req) {
		LoadVAStatusByMemberResponse loadVAByMemberResponse = new LoadVAStatusByMemberResponse();
		try {
			Members members = baseRepository.getMembersRepository().findOneMembers("username", req.getUsername());
			List<VARecordView> listVA = virtualAccountValidation.validateLoadVAStatus(headerParam.value.getToken(), req,
					members.getId());

			List<VARecordView> listVaView = new LinkedList<VARecordView>();
			List<VARecordView> status = new LinkedList<VARecordView>();
			for (int i = 0; i < listVA.size(); i++) {
				VARecordView vaView = new VARecordView();
				RegisterVADoc rva = baseRepository.getPersistenceRepository().retrieve(
						new Query(Criteria.where("_id").is(listVA.get(i).getPaymentCode())), RegisterVADoc.class);

				if (rva != null) {
					Date expiredDate = rva.getExpiredAt() != null
							? Date.from(rva.getExpiredAt().atZone(ZoneId.systemDefault()).toInstant())
							: null;
					status = baseRepository.getVirtualAccountRepository().getVAPaymentStatus(
							Integer.valueOf(listVA.get(i).getId()), req.getFromDate(), req.getToDate());
					for (int l = 0; l < status.size(); l++) {
						logger.info("ID: " + listVA.get(i).getId() + " /Payment Code: " + listVA.get(i).getPaymentCode()
								+ " /Status: " + status.get(l).getStatus());
						vaView.setExpiredAt(expiredDate);
						vaView.setBankID(listVA.get(i).getBankID());
						vaView.setBankCode(listVA.get(i).getBankCode());
						vaView.setBankName(listVA.get(i).getBankName());
						vaView.setId(listVA.get(i).getId());
						vaView.setPaymentCode(listVA.get(i).getPaymentCode());
						vaView.setName(listVA.get(i).getName());
						vaView.setParentUsername(listVA.get(i).getParentUsername());
						vaView.setReferenceNumber(listVA.get(i).getReferenceNumber());
						vaView.setAmount(listVA.get(i).getAmount());
						vaView.setFormattedAmount(Utils.formatAmount(listVA.get(i).getAmount()));
						vaView.setTransactionDate(listVA.get(i).getTransactionDate());
						vaView.setFullPayment(listVA.get(i).isFullPayment());
						vaView.setPersistent(listVA.get(i).isPersistent());
						if (status.get(l).getStatus() == null) {
							vaView.setStatus("UNPAID");
						} else if (status.get(l).getStatus().equalsIgnoreCase("PROCESSED")) {
							vaView.setStatus("PAID");
							vaView.setTransactionDate(status.get(l).getTransactionDate());
						} else if (status.get(l).getStatus().equalsIgnoreCase("REVERSED")) {
							vaView.setStatus("REVERSED");
						} else if (status.get(l).getStatus().equalsIgnoreCase("PENDING")) {
							vaView.setStatus("PENDING");
						}
						listVaView.add(vaView);
					}
				} else {
					status = baseRepository.getVirtualAccountRepository().getVAPaymentStatus(
							Integer.valueOf(listVA.get(i).getId()), req.getFromDate(), req.getToDate());
					for (int l = 0; l < status.size(); l++) {
						logger.info("ID: " + listVA.get(i).getId() + " /Payment Code: " + listVA.get(i).getPaymentCode()
								+ " /Status: " + status.get(l).getStatus());
						vaView.setExpiredAt(listVA.get(i).getExpiredAt());
						vaView.setBankID(listVA.get(i).getBankID());
						vaView.setBankCode(listVA.get(i).getBankCode());
						vaView.setBankName(listVA.get(i).getBankName());
						vaView.setId(listVA.get(i).getId());
						vaView.setPaymentCode(listVA.get(i).getPaymentCode());
						vaView.setName(listVA.get(i).getName());
						vaView.setParentUsername(listVA.get(i).getParentUsername());
						vaView.setReferenceNumber(listVA.get(i).getReferenceNumber());
						vaView.setAmount(listVA.get(i).getAmount());
						vaView.setFormattedAmount(Utils.formatAmount(listVA.get(i).getAmount()));
						vaView.setTransactionDate(listVA.get(i).getTransactionDate());
						vaView.setFullPayment(listVA.get(i).isFullPayment());
						vaView.setPersistent(listVA.get(i).isPersistent());
						if (status.get(l).getStatus() == null) {
							vaView.setStatus("EXPIRED");
						} else if (status.get(l).getStatus().equalsIgnoreCase("PROCESSED")) {
							vaView.setStatus("PAID");
						} else if (status.get(l).getStatus().equalsIgnoreCase("REVERSED")) {
							vaView.setStatus("REVERSED");
						} else if (status.get(l).getStatus().equalsIgnoreCase("PENDING")) {
							vaView.setStatus("PENDING");
						}
						listVaView.add(vaView);
					}
				}
			}

			// Integer totalRecord =
			// baseRepository.getVirtualAccountRepository().countVAReport(members.getId());

			loadVAByMemberResponse.setTotalRecords(listVA.size());
			loadVAByMemberResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			loadVAByMemberResponse.setVaRecord(listVaView);

			return loadVAByMemberResponse;
		} catch (TransactionException ex) {
			loadVAByMemberResponse.setStatus(StatusBuilder.getStatus(String.valueOf(ex.getMessage())));
			return loadVAByMemberResponse;
		}
	}

	@Override
	public void updateBillingStatus(Holder<Header> headerParam, UpdateBillingStatusRequest req) throws Exception {
		virtualAccountValidation.validateVABillingStatus(headerParam.value.getToken(), req.getUsername(),
				req.getTraceNumber(), req.getTransactionNumber());
	}
}

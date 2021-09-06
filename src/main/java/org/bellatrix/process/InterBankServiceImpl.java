package org.bellatrix.process;

import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.ws.Holder;

import org.apache.log4j.Logger;
import org.bellatrix.data.AccountTransfer;
import org.bellatrix.data.BankTransferRequest;
import org.bellatrix.data.BankTransfers;
import org.bellatrix.data.BankView;
import org.bellatrix.data.GenerateTopupDoc;
import org.bellatrix.data.Header;
import org.bellatrix.data.MemberView;
import org.bellatrix.data.Members;
import org.bellatrix.data.Notifications;
import org.bellatrix.data.ScheduleTransfer;
import org.bellatrix.data.Status;
import org.bellatrix.data.StatusBuilder;
import org.bellatrix.data.TransactionException;
import org.bellatrix.data.TransferTypes;
import org.bellatrix.data.Transfers;
import org.bellatrix.services.BankAccountTransferPaymentConfirmation;
import org.bellatrix.services.BankAccountTransferRequest;
import org.bellatrix.services.BankAccountTransferResponse;
import org.bellatrix.services.CreateScheduleTransferRequest;
import org.bellatrix.services.DeleteScheduleTransferRequest;
import org.bellatrix.services.InterBank;
import org.bellatrix.services.LoadAccountTransferRequest;
import org.bellatrix.services.LoadAccountTransferResponse;
import org.bellatrix.services.LoadBankTransferRequest;
import org.bellatrix.services.LoadBankTransferResponse;
import org.bellatrix.services.LoadScheduleTransferByIDRequest;
import org.bellatrix.services.LoadScheduleTransferByIDResponse;
import org.bellatrix.services.LoadScheduleTransferByUsernameRequest;
import org.bellatrix.services.LoadScheduleTransferByUsernameResponse;
import org.bellatrix.services.PaymentDetails;
import org.bellatrix.services.PaymentRequest;
import org.bellatrix.services.RegisterAccountTransferRequest;
import org.bellatrix.services.TopupParamRequest;
import org.bellatrix.services.TopupRequest;
import org.bellatrix.services.TopupResponse;
import org.bellatrix.services.UpdateScheduleTransfer;
import org.mule.api.MuleException;
import org.mule.module.client.MuleClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

public class InterBankServiceImpl implements InterBank {

	@Autowired
	private WebserviceValidation webserviceValidation;
	@Autowired
	private MemberValidation memberValidation;
	@Autowired
	private PaymentValidation paymentValidation;
	@Autowired
	private HazelcastInstance instance;
	@Autowired
	private InterBankValidation interbankValidation;
	@Autowired
	private BaseRepository baseRepository;
	@Autowired
	private Configurator configurator;
	private Logger logger = Logger.getLogger(InterBankServiceImpl.class);

	@Override
	public TopupResponse topupRequest(Holder<Header> headerParam, TopupRequest req) throws Exception {
		TopupResponse tr = new TopupResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Members member = memberValidation.validateMember(req.getUsername(), false);

			String refNo = Utils.GenerateRandomNumber(8);
			IMap<String, GenerateTopupDoc> topupMap = instance.getMap("GenerateTopupMap");
			if (topupMap.get(refNo) != null) {
				tr.setStatus(StatusBuilder.getStatus(Status.DUPLICATE_TRANSACTION));
				return tr;
			} else {
				GenerateTopupDoc td = new GenerateTopupDoc();
				td.setId(refNo);
				td.setMember(member);
				td.setReferenceNumber(refNo);
				td.setRequest(req);
				topupMap.put(refNo, td);

				Notifications notif = baseRepository.getGroupsRepository()
						.loadDefaultNotificationByGroupID(member.getGroupID());

				MuleClient client;
				client = new MuleClient(configurator.getMuleContext());
				Map<String, Object> header = new HashMap<String, Object>();
				header.put("NOTIFICATION_TYPE", "requestTopup");
				header.put("NOTIFICATION_URL", notif.getModuleURL());
				client.dispatch("NotificationVM", td, header);

				tr.setOtp(refNo);
				tr.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
				return tr;
			}

		} catch (TransactionException ex) {
			tr.setStatus(StatusBuilder.getStatus(ex.getMessage()));
			return tr;
		}
	}

	@Override
	public TopupResponse topupInquiry(Holder<Header> headerParam, TopupParamRequest req) throws Exception {
		TopupResponse tr = new TopupResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Members member = memberValidation.validateMember(req.getReferenceNumber(), true);
			MemberView mv = new MemberView();
			mv.setId(member.getId());
			mv.setName(member.getName());
			mv.setUsername(member.getUsername());

			tr.setMember(mv);
			tr.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return tr;
		} catch (TransactionException ex) {
			tr.setStatus(StatusBuilder.getStatus(ex.getMessage()));
			return tr;
		}
	}

	@Override
	public TopupResponse topupPayment(Holder<Header> headerParam, TopupParamRequest req) throws Exception {
		TopupResponse tr = new TopupResponse();
		try {
			PaymentRequest pr = new PaymentRequest();
			pr.setAmount(req.getAmount());
			pr.setTransferTypeID(req.getTransferTypeID());
			pr.setFromMember(req.getFromMember());
			pr.setToMember(req.getReferenceNumber());
			pr.setTraceNumber(req.getTraceNumber());
			pr.setDescription("Topup ChannelID: " + req.getChannelID());
			pr.setReferenceNumber(req.getTransactionID());

			PaymentDetails pd = paymentValidation.validatePayment(pr, headerParam.value.getToken(), "PROCESSED");

			if (pd != null) {
				List<Notifications> ln = baseRepository.getTransferTypeRepository()
						.loadNotificationByTransferType(pd.getTransferType().getId());

				if (ln.size() > 0) {
					pd.setNotification(ln);
					MuleClient client;
					client = new MuleClient(configurator.getMuleContext());
					Map<String, Object> header = new HashMap<String, Object>();
					client.dispatch("NotificationVM", pd, header);
				}
			}

			MemberView mv = new MemberView();
			mv.setId(pd.getToMember().getId());
			mv.setName(pd.getToMember().getName());
			mv.setUsername(pd.getToMember().getUsername());
			tr.setMember(mv);
			tr.setTransactionNumber(pd.getTransactionNumber());
			tr.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return tr;
		} catch (TransactionException ex) {
			Members topupDestination = memberValidation.validateMember(req.getReferenceNumber(), false);
			List<Notifications> ln = new LinkedList<Notifications>();
			Notifications notif = baseRepository.getGroupsRepository()
					.loadDefaultNotificationByGroupID(topupDestination.getGroupID());
			notif.setNotificationType("topupFailedConfirmation");
			ln.add(notif);
			PaymentDetails pd = new PaymentDetails();
			pd.setNotification(ln);
			pd.setFromMember(topupDestination);
			MuleClient client;
			client = new MuleClient(configurator.getMuleContext());
			Map<String, Object> header = new HashMap<String, Object>();
			client.dispatch("NotificationVM", pd, header);
			tr.setStatus(StatusBuilder.getStatus(ex.getMessage()));
			return tr;
		} catch (MuleException e) {
			tr.setStatus(StatusBuilder.getStatus(Status.UNKNOWN_ERROR));
			return tr;
		}
	}

	@Override
	public TopupResponse topupReversal(Holder<Header> headerParam, TopupParamRequest req) throws Exception {
		TopupResponse tr = new TopupResponse();
		List<Transfers> listMainTransfers;
		try {
			listMainTransfers = paymentValidation.validateReversal(req.getTraceNumber(), headerParam.value.getToken());
			if (listMainTransfers == null) {
				tr.setStatus(StatusBuilder.getStatus(Status.PAYMENT_NOT_FOUND));
				return tr;
			}

			Transfers mainTransfers = listMainTransfers.get(0);
			baseRepository.getTransferRepository().reverseTransaction(mainTransfers.getToMemberID(),
					mainTransfers.getTransactionNumber());
			tr.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return tr;

		} catch (SocketTimeoutException e) {
			tr.setStatus(StatusBuilder.getStatus(Status.REQUEST_TIMEOUT));
			return tr;
		} catch (TransactionException e) {
			tr.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return tr;
		}
	}

	@Override
	public BankAccountTransferResponse bankAccountTransferInquiry(Holder<Header> headerParam,
			BankAccountTransferRequest req) throws Exception {
		BankAccountTransferResponse atr = new BankAccountTransferResponse();
		try {
			IMap<String, PaymentDetails> mapLrpcMap = instance.getMap("RequestPaymentMap");
			BankTransferRequest bank = interbankValidation.validateTransferBank(headerParam.value.getToken(), req);
			PaymentDetails pd = new PaymentDetails();
			pd.setFromMember(bank.getFromMember());

			if (bank.getFinalAmount().compareTo(new BigDecimal(baseRepository.getGlobalConfigRepository()
					.loadGlobalConfigByInternalName("two.factor.amount.treshold").getValue())) == 1) {
				String otp = Utils.GenerateRandomNumber(6);
				pd.setTwoFactorAuthentication(true);
				pd.setOtp(otp);
				atr.setTwoFactorAuthentication(true);

				List<Notifications> ln = new LinkedList<Notifications>();
				Notifications notif = baseRepository.getGroupsRepository()
						.loadDefaultNotificationByGroupID(bank.getFromMember().getGroupID());
				notif.setNotificationType("requestPaymentConfirmation");
				ln.add(notif);
				pd.setNotification(ln);

				MuleClient client;
				client = new MuleClient(configurator.getMuleContext());
				Map<String, Object> header = new HashMap<String, Object>();
				client.dispatch("NotificationVM", pd, header);
			}

			mapLrpcMap.put(req.getUsername() + req.getAccountNumber(), pd);

			atr.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			atr.setFinalAmount(bank.getTransactionAmount()); // Change finalAmount to transactionAmount
			atr.setTotalFees(bank.getTotalFees());
			atr.setTransactionAmount(bank.getTransactionAmount());
			atr.setAccountName(bank.getToAccountName());
			atr.setAccountNumber(bank.getToAccountNumber());
			atr.setBankName(bank.getBankName());
			atr.setBankCode(bank.getBankCode());

			return atr;
		} catch (TransactionException ex) {
			atr.setStatus(StatusBuilder.getStatus(ex.getMessage()));
			return atr;
		}
	}

	@Override
	public BankAccountTransferResponse bankAccountTransferPayment(Holder<Header> headerParam,
			BankAccountTransferRequest req) throws Exception {
		BankAccountTransferResponse atr = new BankAccountTransferResponse();
		IMap<String, PaymentDetails> mapLrpcMap = instance.getMap("RequestPaymentMap");
		PaymentDetails pc = mapLrpcMap.get(req.getUsername() + req.getAccountNumber());

		try {

			if (pc == null) {
				atr.setStatus(StatusBuilder.getStatus(Status.INVALID_PARAMETER));
				return atr;
			}

			if (pc.isTwoFactorAuthentication()) {
				if (req.getOtp() != null) {
					if (!req.getOtp().equalsIgnoreCase(pc.getOtp())) {
						mapLrpcMap.remove(req.getUsername() + req.getAccountNumber());
						atr.setStatus(StatusBuilder.getStatus(Status.OTP_VALIDATION_FAILED));
						return atr;
					}
				} else {
					mapLrpcMap.remove(req.getUsername() + req.getAccountNumber());
					atr.setStatus(StatusBuilder.getStatus(Status.INVALID_PARAMETER));
					return atr;
				}
			}

			mapLrpcMap.remove(req.getUsername() + req.getAccountNumber());
			BankTransferRequest bank = interbankValidation.validateTransferBank(headerParam.value.getToken(), req);
			
			PaymentRequest pr = new PaymentRequest();
			pr.setAccessTypeID(req.getAccessTypeID());
			pr.setAmount(req.getAmount());
			pr.setCredential(req.getCredential());
			pr.setFromMember(req.getUsername());
			pr.setToMember(bank.getFromUsername());
			pr.setTraceNumber(req.getTraceNumber());
			pr.setTransferTypeID(bank.getTransferTypeID());
			pr.setReferenceNumber(req.getAccountNumber());
			if (req.getDescription().equalsIgnoreCase(null) || req.getDescription().equalsIgnoreCase("")) {
				pr.setDescription("Transfer " + bank.getBankName() + ", AccountNo : " + req.getAccountNumber());
			} else {
				pr.setDescription(req.getDescription());
			}

			/*
			 * INSERT Pending Transfers
			 */
			PaymentDetails pd = paymentValidation.validatePayment(pr, headerParam.value.getToken(), "PENDING");
			
			Map<String, Object> bankMap = new HashMap<String, Object>();
			bankMap.put("toAccountNumber", bank.getToAccountNumber());
			bankMap.put("toAccountName", req.getAccountName());
			bankMap.put("toResidentStatus", bank.getToResidentStatus());
			bankMap.put("toProfileType", bank.getToProfileType());
			bankMap.put("toEmailAddress", pd.getFromMember().getEmail());
			bankMap.put("fromAccountNumber", bank.getFromAccountNumber());
			bankMap.put("username", req.getUsername());
			bankMap.put("bankCode", bank.getBankCode());
			bankMap.put("swiftCode", bank.getSwiftCode());
			// Change finalAmount to transactionAmount field amount
			bankMap.put("amount", pd.getFees().getTransactionAmount().toPlainString());
			if (req.getDescription().equalsIgnoreCase(null) || req.getDescription().equalsIgnoreCase("")) {
				bankMap.put("description",
						"Transfer " + bank.getBankName() + ", AccountNo : " + req.getAccountNumber());
			} else {
				bankMap.put("description", req.getDescription());
			}

			bankMap.put("remark", req.getDescription());

			if (!bank.getTransferMethod().equalsIgnoreCase("0")
					&& pd.getFees().getFinalAmount().intValue() > 100000000) {
				bankMap.put("transferMethod", "1");
				logger.info("[Transfer Method: 1]");
			} else {
				bankMap.put("transferMethod", bank.getTransferMethod());
				logger.info("[Transfer Method: " + bank.getTransferMethod() + "]");
			}

			bankMap.put("chargingCode", bank.getChargingCode());
			bankMap.put("traceNumber", req.getTraceNumber());
			bankMap.put("transactionNumber", pd.getTransactionNumber());
			bankMap.put("transferTypeID", bank.getTransferTypeID());
			bankMap.put("transferID", pd.getTransferID());
			bankMap.put("token", headerParam.value.getToken());
			bankMap.put("bankName", bank.getBankName());

			MuleClient client;
			client = new MuleClient(configurator.getMuleContext());
			Map<String, Object> header = new HashMap<String, Object>();
			header.put("TRANSACTION_TYPE", "payment");
			header.put("GATEWAY_URL", bank.getGatewayURL());

			client.dispatch("InterbankVM", bankMap, header);

			atr.setStatus(StatusBuilder.getStatus(Status.REQUEST_RECEIVED));
			atr.setFinalAmount(bank.getTransactionAmount());// Change finalAmount to transactionAmount
			atr.setTotalFees(bank.getTotalFees());
			atr.setTransactionAmount(bank.getTransactionAmount());
			atr.setAccountNumber(req.getAccountNumber());
			atr.setBankName(bank.getBankName());
			atr.setTransactionNumber(pd.getTransactionNumber());
			atr.setTraceNumber(req.getTraceNumber());
			return atr;

		} catch (TransactionException ex) {
			mapLrpcMap.remove(req.getUsername() + req.getAccountNumber());
			atr.setStatus(StatusBuilder.getStatus(ex.getMessage()));
			return atr;
		}
	}

	@Override
	public LoadBankTransferResponse loadBankTransfer(Holder<Header> headerParam, LoadBankTransferRequest req)
			throws Exception {
		LoadBankTransferResponse lbr = new LoadBankTransferResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Members member = memberValidation.validateMember(req.getUsername(), true);

			List<Integer> bankIDs = baseRepository.getInterBankRepository()
					.loadBankTransferPermissionByGroup(member.getGroupID());

			if (bankIDs.size() == 0) {
				lbr.setStatus(StatusBuilder.getStatus(Status.BANK_NOT_FOUND));
				return lbr;
			}
			List<BankView> lbv = new LinkedList<BankView>();
			List<BankTransfers> banks = baseRepository.getInterBankRepository().loadBanksFromIDs(bankIDs,
					member.getGroupID(), req.getCurrentPage(), req.getPageSize());

			for (int i = 0; i < banks.size(); i++) {
				BankView bv = new BankView();
				bv.setBankCode(banks.get(i).getBankCode());
				bv.setBankName(banks.get(i).getBankName());
				bv.setBankId(banks.get(i).getId());
				bv.setUsername(banks.get(i).getToUsername());
				bv.setTransferTypeID(banks.get(i).getTransferTypeID());
				lbv.add(bv);
			}

			lbr.setBankDetails(lbv);
			lbr.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return lbr;
		} catch (TransactionException ex) {
			lbr.setStatus(StatusBuilder.getStatus(ex.getMessage()));
			return lbr;
		}
	}

	@Override
	public LoadAccountTransferResponse loadAccountTransfer(Holder<Header> headerParam, LoadAccountTransferRequest req)
			throws Exception {
		LoadAccountTransferResponse loadAccountResponse = new LoadAccountTransferResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Members member = memberValidation.validateMember(req.getUsername(), true);
			if (req.getCurrentPage() == null || req.getPageSize() == null) {
				req.setCurrentPage(0);
				req.setPageSize(0);
			}

			List<AccountTransfer> accTransfer = new LinkedList<AccountTransfer>();
			if (req.getAccountNo() == null || req.getAccountNo().isEmpty()) {
				accTransfer = baseRepository.getInterBankRepository().loadBankAccountListByMember(member.getId(),
						req.getCurrentPage(), req.getPageSize());

			} else {
				accTransfer = baseRepository.getInterBankRepository().loadBankAccountListByNo(member.getId(),
						req.getAccountNo());
			}
			Integer totalRecords = baseRepository.getInterBankRepository().countTotalbankAccounts(member.getId());

			loadAccountResponse.setAccountTransfer(accTransfer);
			loadAccountResponse.setTotalRecords(totalRecords);
			loadAccountResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return loadAccountResponse;
		} catch (TransactionException ex) {
			loadAccountResponse.setStatus(StatusBuilder.getStatus(ex.getMessage()));
			return loadAccountResponse;
		}
	}

	@Override
	public void registerAccountTransfer(Holder<Header> headerParam, RegisterAccountTransferRequest req)
			throws Exception {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Members member = memberValidation.validateMember(req.getUsername(), true);
			BankTransfers bankTransfers = baseRepository.getInterBankRepository().loadBanksFromID(req.getBankID(),
					member.getGroupID());
			if (bankTransfers == null) {
				throw new TransactionException(String.valueOf(Status.BANK_NOT_FOUND));
			}

			List<AccountTransfer> accTransfer = baseRepository.getInterBankRepository()
					.loadBankAccountListByMember(member.getId(), 0, 100);
			logger.info("[Bank Account Transfer List Size: " + accTransfer.size() + "]");
			if (accTransfer.size() > 0 && accTransfer.size() < 4) {
				List<AccountTransfer> accTransferNo = baseRepository.getInterBankRepository()
						.loadBankAccountListByNo(member.getId(), req.getAccountNo());
				if (accTransferNo.size() > 0) {
					throw new TransactionException(String.valueOf(Status.BANK_ACCOUNT_ALREADY_REGISTERED));
				} else {
					baseRepository.getInterBankRepository().insertBankAccount(member.getId(), req);
				}
			} else if (accTransfer.size() == 0) {
				baseRepository.getInterBankRepository().insertBankAccount(member.getId(), req);
			} else {
				throw new TransactionException(String.valueOf(Status.BANK_ACCOUNT_LIMIT));
			}
		} catch (TransactionException ex) {
			throw new TransactionException(String.valueOf(ex.getMessage()));
		}

	}

	@Override
	@Transactional
	public void bankAccountTransferPaymentConfirmation(Holder<Header> headerParam,
			BankAccountTransferPaymentConfirmation req) throws Exception {
		try {
			MuleClient client;
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			List<Notifications> ln = new LinkedList<Notifications>();
			Integer fromMember = baseRepository.getTransferRepository().selectSourceMemberFromID(req.getTransferID());
			Members member = baseRepository.getMembersRepository().findOneMembers("id", fromMember);
			HashMap<String, Object> payMap = new HashMap<String, Object>();
			payMap.put("bankName", req.getBankName());
			payMap.put("accountNo", req.getAccountNo());
			payMap.put("trxAmount", req.getAmount());

			PaymentDetails pd = new PaymentDetails();
			pd.setFromMember(member);
			pd.setNotification(ln);
			pd.setTransferID(req.getTransferID());
			pd.setTransactionNumber(req.getTransactionNumber());
			pd.setPrivateField(payMap);
			pd.setTransactionDate(new Date());

			if (req.getStatus().equalsIgnoreCase("00")) {
				baseRepository.getTransferRepository().confirmPendingTransfers(req.getTransferID(),
						req.getTransactionNumber(), req.getReferenceNumber());

				Notifications notif = baseRepository.getGroupsRepository()
						.loadDefaultNotificationByGroupID(member.getGroupID());

				if (notif != null) {
					notif.setNotificationType("successBankTransferConfirmation");
					ln.add(notif);

					client = new MuleClient(configurator.getMuleContext());
					Map<String, Object> header = new HashMap<String, Object>();
					client.dispatch("NotificationVM", pd, header);
				}

			} else if (req.getStatus().equalsIgnoreCase("01")) {
				logger.info("[SUSPENDED Transfer Transaction]");
			} else {
				baseRepository.getTransferRepository().deletePendingTransfers(req.getTransactionNumber());
				Notifications notif = baseRepository.getGroupsRepository()
						.loadDefaultNotificationByGroupID(member.getGroupID());
				if (notif != null) {
					notif.setNotificationType("failedBankTransferConfirmation");
					ln.add(notif);

					client = new MuleClient(configurator.getMuleContext());
					Map<String, Object> header = new HashMap<String, Object>();
					client.dispatch("NotificationVM", pd, header);
				}
			}
		} catch (TransactionException ex) {
			throw new TransactionException(String.valueOf(ex.getMessage()));
		}
	}

	@Override
	public void removeAccountTransfer(Holder<Header> headerParam, LoadAccountTransferRequest req) throws Exception {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Members member = memberValidation.validateMember(req.getUsername(), true);
			baseRepository.getInterBankRepository().removeBankAccount(req.getId(), member.getId());
		} catch (Exception ex) {
			throw new TransactionException(String.valueOf(ex.getMessage()));
		}
	}

	@Override
	public void createScheduleTransfer(Holder<Header> headerParam, CreateScheduleTransferRequest req) throws Exception {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Members fromMember = baseRepository.getMembersRepository().findOneMembers("id", req.getFromMemberID());
			if (fromMember == null) {
				throw new TransactionException(String.valueOf(Status.MEMBER_NOT_FOUND));
			}

			TransferTypes transferType = baseRepository.getTransferTypeRepository()
					.findTransferTypeByID(req.getTranferTypeID());
			if (transferType == null) {
				throw new TransactionException(String.valueOf(Status.INVALID_TRANSFER_TYPE));
			}

			BankTransfers bank = baseRepository.getInterBankRepository().loadBanksFromID(req.getBankID(),
					fromMember.getGroupID());
			if (bank == null) {
				throw new TransactionException(String.valueOf(Status.BANK_NOT_FOUND));
			}
			baseRepository.getInterBankRepository().createScheduleTransfer(req);
		} catch (DuplicateKeyException ex) {
			throw new TransactionException(String.valueOf(Status.MEMBER_ALREADY_REGISTERED));
		}
	}

	@Override
	public void updateScheduleTransfer(Holder<Header> headerParam, UpdateScheduleTransfer req) throws Exception {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			ScheduleTransfer transfers = baseRepository.getInterBankRepository().findOneScheduleTransfer("id",
					req.getId());
			if (transfers == null) {
				throw new TransactionException(String.valueOf(Status.SCHEDULED_TRANSFER_NOT_FOUND));
			}
			req.setId(transfers.getId());

			if (req.getTranferTypeID() != null && req.getTranferTypeID() != transfers.getTransferTypeID()) {
				TransferTypes transferType = baseRepository.getTransferTypeRepository()
						.findTransferTypeByID(req.getTranferTypeID());
				if (transferType == null) {
					throw new TransactionException(String.valueOf(Status.INVALID_TRANSFER_TYPE));
				}
			} else {
				req.setTranferTypeID(transfers.getTransferTypeID());
			}

			if (req.getFromMemberID() != null && req.getFromMemberID() != transfers.getFromMemberID()) {
				Members fromMember = baseRepository.getMembersRepository().findOneMembers("id", req.getFromMemberID());
				if (fromMember == null) {
					throw new TransactionException(String.valueOf(Status.MEMBER_NOT_FOUND));
				}
			} else {
				req.setFromMemberID(transfers.getFromMemberID());
			}

			if (req.getBankID() != null && req.getBankID() != transfers.getBankID()) {
				Members fromMember = baseRepository.getMembersRepository().findOneMembers("id", req.getFromMemberID());
				if (fromMember == null) {
					throw new TransactionException(String.valueOf(Status.MEMBER_NOT_FOUND));
				}
				BankTransfers bank = baseRepository.getInterBankRepository().loadBanksFromID(req.getBankID(),
						fromMember.getGroupID());
				if (bank == null) {
					throw new TransactionException(String.valueOf(Status.BANK_NOT_FOUND));
				}
			} else {
				req.setBankID(transfers.getBankID());
			}

			if (req.getAccountName() == null) {
				req.setAccountName(transfers.getAccountName());
			}

			if (req.getAccountNo() == null) {
				req.setAccountNo(transfers.getAccountNo());
			}

			req.setEnabled(req.isEnabled());
			
			if(req.getScheduleDate() == null) {
				req.setScheduleDate(transfers.getScheduletDate());
			}

			baseRepository.getInterBankRepository().updateScheduleTransfer(req);
		} catch (DataIntegrityViolationException e) {
			throw new TransactionException(String.valueOf(Status.INVALID_PARAMETER));
		}
	}

	@Override
	public void deleteScheduleTransfer(Holder<Header> headerParam, DeleteScheduleTransferRequest req)
			throws Exception {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			baseRepository.getInterBankRepository().deleteScheduleTransfer(req.getId());
		} catch (Exception e) {
			throw new TransactionException(e.getMessage());
		}
	}

	@Override
	public LoadScheduleTransferByUsernameResponse loadScheduleTransferByUsername(Holder<Header> headerParam,
			LoadScheduleTransferByUsernameRequest req) throws Exception {
		LoadScheduleTransferByUsernameResponse res = new LoadScheduleTransferByUsernameResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Members member = baseRepository.getMembersRepository().findOneMembers("username", req.getUsername());
			if (member == null) {
				throw new TransactionException(String.valueOf(Status.MEMBER_NOT_FOUND));
			}
			List<ScheduleTransfer> list = baseRepository.getInterBankRepository().findScheduleTransfer("from_member_id",
					member.getId());
			res.setScheduleTransfers(list);
			res.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return res;
		} catch (Exception e) {
			res.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return res;
		}

	}

	@Override
	public LoadScheduleTransferByIDResponse loadScheduleTransferByID(Holder<Header> headerParam,
			LoadScheduleTransferByIDRequest req) {
		LoadScheduleTransferByIDResponse res = new LoadScheduleTransferByIDResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			ScheduleTransfer st = baseRepository.getInterBankRepository().findOneScheduleTransfer("id", req.getId());

			res.setScheduleTransfers(st);
			res.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			
			return res;
		} catch (Exception e) {
			res.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return res;
		}
	}


}

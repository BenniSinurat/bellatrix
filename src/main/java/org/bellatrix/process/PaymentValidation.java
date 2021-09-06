package org.bellatrix.process;

import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.bellatrix.data.Accounts;
import org.bellatrix.data.FeeResult;
import org.bellatrix.data.Fees;
import org.bellatrix.data.MemberView;
import org.bellatrix.data.Members;
import org.bellatrix.data.Status;
import org.bellatrix.data.TransactionException;
import org.bellatrix.data.TransferTypeFields;
import org.bellatrix.data.TransferTypes;
import org.bellatrix.data.Transfers;
import org.bellatrix.data.WebServices;
import org.bellatrix.services.AgentCashoutRequest;
import org.bellatrix.services.GeneratePaymentTicketRequest;
import org.bellatrix.services.InquiryRequest;
import org.bellatrix.services.InquiryResponse;
import org.bellatrix.services.PaymentDetails;
import org.bellatrix.services.PaymentRequest;
import org.bellatrix.services.UpdateTransferRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

@Component
public class PaymentValidation {

	@Autowired
	private BaseRepository baseRepository;
	@Autowired
	private FeeProcessor feeProcessor;
	@Autowired
	private CredentialValidation credentialValidation;
	@Autowired
	private PaymentCustomFieldValidation customFieldValidation;
	@Autowired
	private TransferTypeValidation transferTypeValidation;
	@Autowired
	private GroupPermissionValidation groupValidation;
	@Autowired
	private WebserviceValidation webserviceValidation;
	@Autowired
	private MemberValidation memberValidation;
	@Autowired
	private AccountValidation accountValidation;
	@Autowired
	private HazelcastInstance instance;
	@Autowired
	private TraceNumberValidation traceNumberValidation;

	private Logger logger = Logger.getLogger(PaymentValidation.class);

	public InquiryResponse validateInquiry(InquiryRequest req, String token)
			throws SocketTimeoutException, TransactionException {

		/*
		 * Validate Webservice Access
		 */
		WebServices ws = webserviceValidation.validateWebservice(token);
		Integer wsID = ws.getId();

		/*
		 * Validate FromMember
		 */
		Members fromMember = memberValidation.validateMember(req.getFromMember(), true);

		/*
		 * Validate FromMember Group Permission to Webservice
		 */
		groupValidation.validateGroupPermission(wsID, fromMember.getGroupID());

		/*
		 * Validate ToMember
		 */
		Members toMember = memberValidation.validateMember(req.getToMember(), false);

		/*
		 * Validate TransferType
		 */
		TransferTypes transferType = transferTypeValidation.validateTransferType(req.getTransferTypeID(),
				fromMember.getGroupID(), req.getAmount(), fromMember.getId());

		/*
		 * Validate FromAccount
		 */
		Accounts fromAccount = accountValidation.validateAccount(transferType, fromMember, true);
		Accounts toAccount = null;
		//if (transferType.getFromAccounts() != transferType.getToAccounts()) {

			/*
			 * Validate ToAccount
			 */
			toAccount = accountValidation.validateAccount(transferType, toMember, false);
		//} else {
		//	toAccount = fromAccount;
		//}

		/*
		 * PRIORITY Fees Processing (if Priority Fee != null then skip the
		 * Regular Fee Processing)
		 */

		// Code Here

		/*
		 * Regular Fees Processing (Skip this if Priority Fee != null)
		 */

		FeeResult feeResult = feeProcessor.CalculateFee(transferType, fromMember, toMember, fromAccount, toAccount,
				req.getAmount());

		logger.info("[Transaction Amount : " + feeResult.getFinalAmount() + "/ OTP Threshold : " + baseRepository
				.getGlobalConfigRepository().loadGlobalConfigByInternalName("two.factor.amount.treshold").getValue()
				+ "]");

		InquiryResponse ir = new InquiryResponse();

		MemberView fromView = new MemberView();
		MemberView toView = new MemberView();
		TransferTypeFields tfield = new TransferTypeFields();

		fromView.setId(fromMember.getId());
		fromView.setName(fromMember.getName());
		fromView.setUsername(fromMember.getUsername());

		toView.setId(toMember.getId());
		toView.setName(toMember.getName());
		toView.setUsername(toMember.getUsername());

		tfield.setFromAccounts(fromAccount.getId());
		tfield.setToAccounts(toAccount.getId());
		tfield.setName(transferType.getName());
		tfield.setId(transferType.getId());

		ir.setFinalAmount(feeResult.getFinalAmount());
		ir.setTotalFees(feeResult.getTotalFees());
		ir.setTransactionAmount(feeResult.getTransactionAmount());
		ir.setFromMember(fromView);
		ir.setToMember(toView);
		ir.setTransferType(tfield);

		return ir;

	}

	public PaymentDetails validatePayment(PaymentRequest req, String token, String transactionState)
			throws TransactionException, SocketTimeoutException {
		IMap<String, BigDecimal> mapLock = instance.getMap("AccountLock");
		List<Fees> fees = new LinkedList<Fees>();
		Members sourceMember = new Members();
		Members targetMember = new Members();
		Accounts fromAccount = new Accounts();
		Accounts toAccount = new Accounts();
		try {

			/*
			 * Validate Webservice Access
			 */
			WebServices ws = webserviceValidation.validateWebservice(token);
			Integer wsID = ws.getId();

			/*
			 * Validate FromMember
			 */
			Members fromMember = memberValidation.validateMember(req.getFromMember(), true);
			sourceMember = fromMember;

			/*
			 * Validate FromMember Group Permission to Webservice
			 */
			groupValidation.validateGroupPermission(wsID, fromMember.getGroupID());

			/*
			 * Validate FromMember Credential
			 */
			credentialValidation.validateCredential(ws, req.getAccessTypeID(), req.getCredential(), fromMember);

			/*
			 * Validate ToMember
			 */
			Members toMember = memberValidation.validateMember(req.getToMember(), false);
			targetMember = toMember;

			/*
			 * Validate Trace Number (Save to cache also ?)
			 */
			traceNumberValidation.validateTraceNumber(wsID, req.getTraceNumber());

			/*
			 * Validate TransferType
			 */
			TransferTypes transferType = transferTypeValidation.validateTransferType(req.getTransferTypeID(),
					fromMember.getGroupID(), req.getAmount(), fromMember.getId());

			if (req.getDescription() == null) {
				req.setDescription(transferType.getName());
			}

			/*
			 * Validate PaymentCustomField
			 */
			if (req.getPaymentFields() != null) {
				customFieldValidation.validatePaymentCustomField(transferType.getId(), req.getPaymentFields());
			}

			/*
			 * 
			 * Validate FromAccount + Lock Accounts
			 *
			 */
			fromAccount = accountValidation.validateAccount(transferType, fromMember, true);

			//if (transferType.getFromAccounts() != transferType.getToAccounts()) {

				/*
				 * Validate ToAccount
				 */
				toAccount = accountValidation.validateAccount(transferType, toMember, false);
			//} else {
			//	toAccount = fromAccount;
			//}

			/*
			 * Lock Member
			 */
			mapLock.put(fromMember.getUsername() + fromAccount.getId(), BigDecimal.ZERO);
			mapLock.lock(fromMember.getUsername() + fromAccount.getId(), 80000, TimeUnit.MILLISECONDS);
			logger.info("[LOCK Source Member/Account : " + req.getFromMember() + "/" + fromAccount.getId() + "]");

			mapLock.put(toMember.getUsername() + toAccount.getId(), BigDecimal.ZERO);
			mapLock.lock(toMember.getUsername() + toAccount.getId(), 80000, TimeUnit.MILLISECONDS);
			logger.info("[LOCK Destination Member/Account : " + req.getToMember() + "/" + toAccount.getId() + "]");

			/*
			 * TODO : PRIORITY Fees Processing (if Priority Fee != null then
			 * skip the Regular Fee Processing)
			 */

			// Code Here

			/*
			 * Regular Fees Processing (Skip this if Priority Fee != null)
			 */

			FeeResult feeResult = feeProcessor.ProcessFee(transferType, fromMember, toMember, fromAccount, toAccount,
					req.getAmount());
			fees = feeResult.getListTotalFees();

			/*
			 * If Source Account is SystemAccount then skip this validation,
			 * otherwise check for Monthly Limit
			 */

			logger.info("[Source Account : " + fromAccount.getName() + "]");

			if (fromAccount.isSystemAccount() == false) {

				logger.info(
						"[Validating Source Account balance/Monthly limit validation : " + fromAccount.getName() + "]");

				/*
				 * Validate FromMember UpperCredit Limit --> Commented for
				 * validating incoming transaction ONLY (PBI Terbaru 2016) --> change to validating outcoming transaction ONLY
				 */
				accountValidation.validateMonthlyLimit(fromMember, fromAccount, feeResult.getFinalAmount(), true);

				/*
				 * Get FromMember Balance Inquiry (Lock Account)
				 */
				accountValidation.validateAccountBalance(fromMember, fromAccount, feeResult.getFinalAmount(), true);
			}

			/*
			 * If Destination Account is SystemAccount then skip this
			 * validation, otherwise check for Monthly Limit
			 */

			logger.info("[Destination Account : " + toAccount.getName() + "]");

			if (toAccount.isSystemAccount() == false) {

				logger.info("[Validating Destination Account balance/Monthly limit validation : " + toAccount.getName()
						+ "]");

				/*
				 * Get ToMember UpperCredit Limit
				 */
				accountValidation.validateMonthlyLimit(toMember, toAccount, feeResult.getFinalAmount(), false);

				/*
				 * Get ToMember Balance Inquiry (Lock Account)
				 */
				accountValidation.validateAccountBalance(toMember, toAccount, feeResult.getFinalAmount(), false);
			}

			/*
			 * All Validation PASSED then generate Transaction Number
			 */
			String clusterid = System.getProperty("mule.clusterId") != null ? System.getProperty("mule.clusterId")
					: "00";
			String trxNo = Utils.GetDate("yyyyMMddkkmmssSSS") + clusterid + Utils.GenerateTransactionNumber();
			

			Integer transferID = baseRepository.getTransferRepository().createTransfers(req,
					feeResult.getTransactionAmount(), transferType, fromAccount, toAccount, fromMember, toMember, trxNo,
					null, transactionState, wsID);

			/*
			 * Insert Fees (If any)
			 */
			if (!feeResult.getListTotalFees().isEmpty()) {
				baseRepository.getTransferRepository().insertFees(req, feeResult.getListTotalFees(), transferType,
						trxNo, transactionState, wsID);
			}

			PaymentDetails pc = new PaymentDetails();
			pc.setTransferID(transferID);
			pc.setFees(feeResult);
			pc.setFromMember(fromMember);
			pc.setFromAccount(fromAccount);
			pc.setToMember(toMember);
			pc.setToAccount(toAccount);
			pc.setRequest(req);
			pc.setWebService(ws);
			pc.setTransactionNumber(trxNo);
			pc.setTransferType(transferType);
			pc.setTransactionDate(req.getTransactionDate());
			return pc;

		} finally {
			if (mapLock != null && sourceMember != null && targetMember != null && fromAccount != null
					&& toAccount != null) {
				if (mapLock.isLocked(req.getFromMember() + fromAccount.getId())) {
					logger.info(
							"[UNLOCK Source Member/Account : " + req.getFromMember() + "/" + fromAccount.getId() + "]");
					mapLock.unlock(req.getFromMember() + fromAccount.getId());
				}
				if (mapLock.isLocked(req.getToMember() + toAccount.getId())) {
					logger.info("[UNLOCK Destination Member/Account : " + req.getToMember() + "/" + toAccount.getId()
							+ "]");
					mapLock.unlock(req.getToMember() + toAccount.getId());
				}

				if (fees.size() > 0) {
					for (int i = 0; i < fees.size(); i++) {
						if (!fees.get(i).getFromMember().getUsername().equalsIgnoreCase(sourceMember.getUsername())
								&& mapLock.isLocked(
										fees.get(i).getFromMember().getUsername() + fees.get(i).getFromAccountID())) {
							logger.info(
									"[UNLOCK Fee Source Member/Account : " + fees.get(i).getFromMember().getUsername()
											+ "/" + fees.get(i).getFromAccountID() + "]");
							mapLock.unlock(fees.get(i).getFromMember().getUsername() + fees.get(i).getFromAccountID());
						}
						if (!fees.get(i).getToMember().getUsername().equalsIgnoreCase(targetMember.getUsername())
								&& mapLock.isLocked(
										fees.get(i).getToMember().getUsername() + fees.get(i).getToAccountID())) {
							logger.info("[UNLOCK Fee Destination Member/Account : "
									+ fees.get(i).getToMember().getUsername() + "/" + fees.get(i).getToAccountID()
									+ "]");
							mapLock.unlock(fees.get(i).getToMember().getUsername() + fees.get(i).getToAccountID());
						}
					}
				}

			}

		}
	}

	public PaymentDetails validatePosPayment(PaymentRequest req, String token, String transactionState)
			throws TransactionException, SocketTimeoutException {
		IMap<String, BigDecimal> mapLock = instance.getMap("AccountLock");
		List<Fees> fees = new LinkedList<Fees>();
		Members sourceMember = new Members();
		Members targetMember = new Members();
		Accounts fromAccount = new Accounts();
		Accounts toAccount = new Accounts();
		try {

			/*
			 * Validate Webservice Access
			 */
			WebServices ws = webserviceValidation.validateWebservice(token);
			Integer wsID = ws.getId();

			/*
			 * Validate FromMember
			 */
			Members fromMember = memberValidation.validateMember(req.getFromMember(), true);
			sourceMember = fromMember;

			/*
			 * Validate ToMember
			 */
			Members toMember = memberValidation.validateMember(req.getToMember(), false);
			targetMember = toMember;

			/*
			 * Validate Trace Number (Save to cache also ?)
			 */
			traceNumberValidation.validateTraceNumber(wsID, req.getTraceNumber());

			/*
			 * Validate TransferType
			 */
			TransferTypes transferType = transferTypeValidation.validateTransferType(req.getTransferTypeID(),
					fromMember.getGroupID(), req.getAmount(), fromMember.getId());

			if (req.getDescription() == null) {
				req.setDescription(transferType.getName());
			}

			/*
			 * Validate PaymentCustomField
			 */
			if (req.getPaymentFields() != null) {
				customFieldValidation.validatePaymentCustomField(transferType.getId(), req.getPaymentFields());
			}

			/*
			 * 
			 * Validate FromAccount + Lock Accounts
			 *
			 */
			fromAccount = accountValidation.validateAccount(transferType, fromMember, true);

			//if (transferType.getFromAccounts() != transferType.getToAccounts()) {

				/*
				 * Validate ToAccount
				 */
				toAccount = accountValidation.validateAccount(transferType, toMember, false);
			//} else {
			//	toAccount = fromAccount;
			//}

			/*
			 * Lock Member
			 */
			mapLock.put(fromMember.getUsername() + fromAccount.getId(), BigDecimal.ZERO);
			mapLock.lock(fromMember.getUsername() + fromAccount.getId(), 80000, TimeUnit.MILLISECONDS);
			logger.info("[LOCK Source Member/Account : " + req.getFromMember() + "/" + fromAccount.getId() + "]");

			mapLock.put(toMember.getUsername() + toAccount.getId(), BigDecimal.ZERO);
			mapLock.lock(toMember.getUsername() + toAccount.getId(), 80000, TimeUnit.MILLISECONDS);
			logger.info("[LOCK Destination Member/Account : " + req.getToMember() + "/" + toAccount.getId() + "]");

			/*
			 * TODO : PRIORITY Fees Processing (if Priority Fee != null then
			 * skip the Regular Fee Processing)
			 */

			// Code Here

			/*
			 * Regular Fees Processing (Skip this if Priority Fee != null)
			 */

			FeeResult feeResult = feeProcessor.ProcessFee(transferType, fromMember, toMember, fromAccount, toAccount,
					req.getAmount());
			fees = feeResult.getListTotalFees();

			/*
			 * If Source Account is SystemAccount then skip this validation,
			 * otherwise check for Monthly Limit
			 */

			logger.info("[Source Account : " + fromAccount.getName() + "]");

			if (fromAccount.isSystemAccount() == false) {

				logger.info(
						"[Validating Source Account balance/Monthly limit validation : " + fromAccount.getName() + "]");

				/*
				 * Validate FromMember UpperCredit Limit --> Commented for
				 * validating incoming transaction ONLY (PBI Terbaru 2016)
				 */
				// accountValidation.validateMonthlyLimit(fromMember,
				// fromAccount, feeResult.getFinalAmount(), true);

				/*
				 * Get FromMember Balance Inquiry (Lock Account)
				 */
				accountValidation.validateAccountBalance(fromMember, fromAccount, feeResult.getFinalAmount(), true);
			}

			/*
			 * If Destination Account is SystemAccount then skip this
			 * validation, otherwise check for Monthly Limit
			 */

			logger.info("[Destination Account : " + toAccount.getName() + "]");

			if (toAccount.isSystemAccount() == false) {

				logger.info("[Validating Destination Account balance/Monthly limit validation : " + toAccount.getName()
						+ "]");

				/*
				 * Get ToMember UpperCredit Limit
				 */
				accountValidation.validateMonthlyLimit(toMember, toAccount, feeResult.getFinalAmount(), false);

				/*
				 * Get ToMember Balance Inquiry (Lock Account)
				 */
				accountValidation.validateAccountBalance(toMember, toAccount, feeResult.getFinalAmount(), false);
			}

			/*
			 * All Validation PASSED then generate Transaction Number
			 */
			String clusterid = System.getProperty("mule.clusterId") != null ? System.getProperty("mule.clusterId")
					: "00";
			String trxNo = Utils.GetDate("yyyyMMddkkmmssSSS") + clusterid + Utils.GenerateTransactionNumber();

			Integer transferID = baseRepository.getTransferRepository().createTransfers(req,
					feeResult.getTransactionAmount(), transferType, fromAccount, toAccount, fromMember, toMember, trxNo,
					null, transactionState, wsID);

			/*
			 * Insert Fees (If any)
			 */
			if (!feeResult.getListTotalFees().isEmpty()) {
				baseRepository.getTransferRepository().insertFees(req, feeResult.getListTotalFees(), transferType,
						trxNo, transactionState, wsID);
			}

			PaymentDetails pc = new PaymentDetails();
			pc.setTransferID(transferID);
			pc.setFees(feeResult);
			pc.setFromMember(fromMember);
			pc.setFromAccount(fromAccount);
			pc.setToMember(toMember);
			pc.setToAccount(toAccount);
			pc.setRequest(req);
			pc.setWebService(ws);
			pc.setTransactionNumber(trxNo);
			pc.setTransferType(transferType);
			pc.setTransactionDate(new Date());
			return pc;

		} finally {
			if (mapLock != null && sourceMember != null && targetMember != null && fromAccount != null
					&& toAccount != null) {
				if (mapLock.isLocked(req.getFromMember() + fromAccount.getId())) {
					logger.info(
							"[UNLOCK Source Member/Account : " + req.getFromMember() + "/" + fromAccount.getId() + "]");
					mapLock.unlock(req.getFromMember() + fromAccount.getId());
				}
				if (mapLock.isLocked(req.getToMember() + toAccount.getId())) {
					logger.info("[UNLOCK Destination Member/Account : " + req.getToMember() + "/" + toAccount.getId()
							+ "]");
					mapLock.unlock(req.getToMember() + toAccount.getId());
				}

				if (fees.size() > 0) {
					for (int i = 0; i < fees.size(); i++) {
						if (!fees.get(i).getFromMember().getUsername().equalsIgnoreCase(sourceMember.getUsername())
								&& mapLock.isLocked(
										fees.get(i).getFromMember().getUsername() + fees.get(i).getFromAccountID())) {
							logger.info(
									"[UNLOCK Fee Source Member/Account : " + fees.get(i).getFromMember().getUsername()
											+ "/" + fees.get(i).getFromAccountID() + "]");
							mapLock.unlock(fees.get(i).getFromMember().getUsername() + fees.get(i).getFromAccountID());
						}
						if (!fees.get(i).getToMember().getUsername().equalsIgnoreCase(targetMember.getUsername())
								&& mapLock.isLocked(
										fees.get(i).getToMember().getUsername() + fees.get(i).getToAccountID())) {
							logger.info("[UNLOCK Fee Destination Member/Account : "
									+ fees.get(i).getToMember().getUsername() + "/" + fees.get(i).getToAccountID()
									+ "]");
							mapLock.unlock(fees.get(i).getToMember().getUsername() + fees.get(i).getToAccountID());
						}
					}
				}

			}

		}
	}

	public GeneratePaymentTicketRequest validatePaymentRequest(String token, GeneratePaymentTicketRequest req)
			throws TransactionException, SocketTimeoutException {
		/*
		 * Validate Webservice Access
		 */
		webserviceValidation.validateWebservice(token);

		/*
		 * Validate ToMember
		 */
		Members toMember = memberValidation.validateMember(req.getToMember(), false);

		/*
		 * Validate TransferType
		 */
		TransferTypes transferType = transferTypeValidation.validateTransferType(req.getTransferTypeID(),
				req.getAmount());

		/*
		 * Validate PaymentCustomField
		 */
		if (req.getPaymentFields() != null) {
			customFieldValidation.validatePaymentCustomField(transferType.getId(), req.getPaymentFields());
		}

		/*
		 * Validate ToAccount
		 */
		accountValidation.validateAccount(transferType, toMember, false);
		
		GeneratePaymentTicketRequest gtm = new GeneratePaymentTicketRequest();
		gtm.setAmount(req.getAmount());
		gtm.setDescription(req.getDescription());
		gtm.setInvoiceNumber(req.getInvoiceNumber());
		gtm.setTransferTypeID(req.getTransferTypeID());
		gtm.setToMember(req.getToMember());
		gtm.setName(req.getName());
		gtm.setEmail(req.getEmail());
		return gtm;
	}

	public List<Transfers> validateReversal(String traceNumber, String token)
			throws SocketTimeoutException, TransactionException {
		/*
		 * Validate Webservice Access
		 */

		WebServices ws = webserviceValidation.validateWebservice(token);

		List<Transfers> mainTransfers = baseRepository.getTransferRepository().getTransfersFromField("trace_number",
				ws.getId() + traceNumber);
		if (mainTransfers.size() == 0) {
			return null;
		} else {
			return mainTransfers;
		}

	}

	public List<Transfers> validateReversal(String transactionNumber)
			throws SocketTimeoutException, TransactionException {

		List<Transfers> mainTransfers = baseRepository.getTransferRepository()
				.getTransfersFromField("transaction_number", transactionNumber);
		if (mainTransfers.size() == 0) {
			return null;
		} else {
			return mainTransfers;
		}
	}

	public List<Transfers> validateTransactionStatus(String traceNumber, String token)
			throws SocketTimeoutException, TransactionException {
		/*
		 * Validate Webservice Access
		 */

		WebServices ws = webserviceValidation.validateWebservice(token);

		List<Transfers> mainTransfers = baseRepository.getTransferRepository().getTransfersFromField("trace_number",
				ws.getId() + traceNumber);
		if (mainTransfers.size() == 0) {
			throw new TransactionException(String.valueOf(Status.PAYMENT_NOT_FOUND));
		} else {
			return mainTransfers;
		}

	}

	public List<Transfers> validateReversalFromTrxRef(String trxRef, String token)
			throws SocketTimeoutException, TransactionException {
		/*
		 * Validate Webservice Access
		 */

		webserviceValidation.validateWebservice(token);

		List<Transfers> mainTransfers = baseRepository.getTransferRepository().getTransfersFromField("reference_number",
				trxRef);
		if (mainTransfers.size() == 0) {
			return null;
		} else {
			return mainTransfers;
		}

	}

	public Members validateCashoutRequest(String token, AgentCashoutRequest req)
			throws TransactionException, SocketTimeoutException {
		/*
		 * Validate Webservice Access
		 */
		webserviceValidation.validateWebservice(token);

		/*
		 * Validate ToMember
		 */
		Members member = memberValidation.validateMember(req.getFromMember(), false);

		/*
		 * Validate TransferType
		 */
		transferTypeValidation.validateTransferType(req.getTransferTypeID(), member.getGroupID(), req.getAmount(),
				member.getId());

		return member;
	}
	
	public Transfers validatePaymentTransferID(UpdateTransferRequest req) throws TransactionException {
		Transfers transfer = baseRepository.getTransferRepository().findOneTransfers("id", req.getTransferID());
		if (transfer == null) {
				throw new TransactionException(String.valueOf(Status.PAYMENT_NOT_FOUND));
		}
		return transfer;
	}
}

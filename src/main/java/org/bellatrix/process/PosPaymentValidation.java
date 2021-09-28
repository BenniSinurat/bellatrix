package org.bellatrix.process;

import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.bellatrix.data.Accounts;
import org.bellatrix.data.FeeResult;
import org.bellatrix.data.Fees;
import org.bellatrix.data.Members;
import org.bellatrix.data.Status;
import org.bellatrix.data.Terminal;
import org.bellatrix.data.TransactionException;
import org.bellatrix.data.TransferTypes;
import org.bellatrix.data.WebServices;
import org.bellatrix.services.LoadTerminalByUsernameRequest;
import org.bellatrix.services.PaymentDetails;
import org.bellatrix.services.PaymentRequest;
import org.bellatrix.services.PosCreateInvoiceRequest;
import org.bellatrix.services.PosInquiryRequest;
import org.bellatrix.services.PosPaymentRequest;
import org.bellatrix.services.UpdatePOSRequest;
import org.bellatrix.services.DeletePOSRequest;
import org.bellatrix.services.LoadTerminalByIDRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

@Component
public class PosPaymentValidation {

	@Autowired
	private BaseRepository baseRepository;
	@Autowired
	private MemberValidation memberValidation;
	@Autowired
	private WebserviceValidation webserviceValidation;
	@Autowired
	private TransferTypeValidation transferTypeValidation;
	@Autowired
	private AccountValidation accountValidation;
	@Autowired
	private HazelcastInstance instance;
	@Autowired
	private GroupPermissionValidation groupValidation;
	@Autowired
	private CredentialValidation credentialValidation;
	@Autowired
	private TraceNumberValidation traceNumberValidation;
	@Autowired
	private PaymentCustomFieldValidation customFieldValidation;
	@Autowired
	private FeeProcessor feeProcessor;

	private Logger logger = Logger.getLogger(PosPaymentValidation.class);

	public Terminal validatePosTerminal(PosCreateInvoiceRequest req, String token) throws Exception {
		/*
		 * Validate Webservice Access
		 */
		webserviceValidation.validateWebservice(token);

		/*
		 * Validate ToMember
		 */
		Members toMember = memberValidation.validateMember(req.getToMember(), false);

		/*
		 * Validate Terminal ID
		 */
		Terminal terminal = baseRepository.getPosRepository().getPosTerminalDetail(req.getTerminalID(),
				toMember.getId());

		if (terminal == null) {
			throw new TransactionException(String.valueOf(Status.TERMINAL_NOT_FOUND));
		}

		return terminal;
	}

	public Terminal validatePosTerminal(UpdatePOSRequest req, String token) throws Exception {
		/*
		 * Validate Webservice Access
		 */
		webserviceValidation.validateWebservice(token);

		/*
		 * Validate ToMember
		 */
		Members toMember = memberValidation.validateMember(req.getUsername(), false);

		/*
		 * Validate Terminal ID
		 */
		Terminal terminal = baseRepository.getPosRepository().getPosTerminalDetail(req.getTerminalID(),
				toMember.getId());

		if (terminal == null) {
			throw new TransactionException(String.valueOf(Status.TERMINAL_NOT_FOUND));
		}

		return terminal;
	}

	public Terminal validatePosTerminal(DeletePOSRequest req, String token) throws Exception {
		/*
		 * Validate Webservice Access
		 */
		webserviceValidation.validateWebservice(token);

		/*
		 * Validate ToMember
		 */
		Members toMember = memberValidation.validateMember(req.getUsername(), false);

		/*
		 * Validate Terminal ID
		 */
		Terminal terminal = baseRepository.getPosRepository().getPosTerminalDetail(req.getTerminalID(),
				toMember.getId());

		if (terminal == null) {
			throw new TransactionException(String.valueOf(Status.TERMINAL_NOT_FOUND));
		}

		return terminal;
	}

	public List<Terminal> validatePosTerminal(LoadTerminalByUsernameRequest req, String token) throws Exception {
		/*
		 * Validate Webservice Access
		 */
		webserviceValidation.validateWebservice(token);

		/*
		 * Validate ToMember
		 */
		Members toMember = memberValidation.validateMember(req.getUsername(), true);

		/*
		 * Validate Terminal ID
		 */
		List<Terminal> terminal = baseRepository.getPosRepository().getPosTerminalDetail(toMember.getId(),
				req.getCurrentPage(), req.getPageSize());

		if (terminal == null) {
			throw new TransactionException(String.valueOf(Status.TERMINAL_NOT_FOUND));
		}

		return terminal;
	}

	public Terminal validatePosTerminal(LoadTerminalByIDRequest req, String token) throws Exception {
		/*
		 * Validate Webservice Access
		 */
		webserviceValidation.validateWebservice(token);

		/*
		 * Validate ToMember dan Terminal ID
		 */
		Terminal terminal = new Terminal();
		Members member = new Members();
		Members toMember = new Members();
		if (req.getNnsID() == null) {
			member = memberValidation.validateMember(req.getToMember(), false);
			terminal = baseRepository.getPosRepository().getPosTerminalDetail(req.getTerminalID(), member.getId());

			if (terminal == null) {
				throw new TransactionException(String.valueOf(Status.TERMINAL_NOT_FOUND));
			}
			terminal.setMember(member);

			if (terminal.getToMember() == null) {
				terminal.setToMember(null);
			} else {
				toMember = baseRepository.getMembersRepository().findOneMembers("id", terminal.getToMember().getId());
				if (toMember == null) {
					terminal.setToMember(null);
				} else {
					terminal.setToMember(toMember);
				}
			}
		} else {
			terminal = baseRepository.getPosRepository().getPosTerminalDetail(req.getNnsID());
			if (terminal == null) {
				throw new TransactionException(String.valueOf(Status.TERMINAL_NOT_FOUND));
			}
			member = memberValidation.validateMemberID(terminal.getMember().getId(), false);
			terminal.setMember(member);
			
			if (terminal.getToMember() == null) {
				terminal.setToMember(null);
			} else {
				toMember = baseRepository.getMembersRepository().findOneMembers("id", terminal.getToMember().getId());
				if (toMember == null) {
					terminal.setToMember(null);
				} else {
					terminal.setToMember(toMember);
				}
			}
		}

		return terminal;
	}

	public Terminal validatePosInquiry(PosInquiryRequest req, String token) throws Exception {
		/*
		 * Validate Webservice Access
		 */
		webserviceValidation.validateWebservice(token);

		/*
		 * Validate FromMember
		 */
		Members fromMember = memberValidation.validateMember(req.getFromMember(), true);

		/*
		 * Validate ToMember
		 */
		Members toMember = memberValidation.validateMember(req.getToMember(), false);

		/*
		 * Validate Terminal ID
		 */
		Terminal terminal = baseRepository.getPosRepository().getPosTerminalDetail(req.getTerminalID(),
				toMember.getId());

		if (terminal == null) {
			throw new TransactionException(String.valueOf(Status.TERMINAL_NOT_FOUND));
		}

		/*
		 * Validate TransferType
		 */
		TransferTypes transferType = transferTypeValidation.validateTransferType(terminal.getTransferTypeID(),
				fromMember.getGroupID(), fromMember.getId());

		/*
		 * Validate FromAccount
		 */
		accountValidation.validateAccount(transferType, fromMember, true);
		// if (transferType.getFromAccounts() != transferType.getToAccounts()) {

		/*
		 * Validate ToAccount
		 */
		accountValidation.validateAccount(transferType, toMember, false);
		// }

		return terminal;
	}

	public Terminal validatePosPayment(PosPaymentRequest req) throws Exception {
		/*
		 * Validate ToMember
		 */
		Members toMember = memberValidation.validateMember(req.getToMember(), true);

		/*
		 * Validate Terminal ID
		 */
		Terminal terminal = baseRepository.getPosRepository().getPosTerminalDetail(req.getTerminalID(),
				toMember.getId());

		if (terminal == null) {
			throw new TransactionException(String.valueOf(Status.TERMINAL_NOT_FOUND));
		}

		return terminal;
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
			 * Validate NNS ID
			 */
			Terminal terminal = new Terminal();
			if (req.getNnsID() == null) {
				// POSAcquiring acquiring =
				// baseRepository.getPosRepository().getPosAcquiring(req.getAcquiringID());
				/*
				 * Validate ToMember
				 */
				// toMember = memberValidation.validateMemberID(acquiring.getMemberID(), false);
				// targetMember = toMember;
				throw new TransactionException(String.valueOf(Status.DESTINATION_MEMBER_NOT_FOUND));
			} else {
				terminal = baseRepository.getPosRepository().getPosTerminalByNNSID(req.getNnsID());
				if (terminal == null) {
					throw new TransactionException(String.valueOf(Status.DESTINATION_MEMBER_NOT_FOUND));
				}
				targetMember = terminal.getToMember();
			}

			/*
			 * Validate Trace Number (Save to cache also ?)
			 */
			traceNumberValidation.validateTraceNumber(wsID, req.getTraceNumber());

			/*
			 * Validate TransferType
			 */
			TransferTypes transferType = transferTypeValidation.validateTransferType(terminal.getTransferTypeID(),
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

			// if (transferType.getFromAccounts() != transferType.getToAccounts()) {

			/*
			 * Validate ToAccount
			 */
			toAccount = accountValidation.validateAccount(transferType, targetMember, false);
			// } else {
			// toAccount = fromAccount;
			// }

			/*
			 * Lock Member
			 */
			mapLock.put(fromMember.getUsername() + fromAccount.getId(), BigDecimal.ZERO);
			mapLock.lock(fromMember.getUsername() + fromAccount.getId(), 80000, TimeUnit.MILLISECONDS);
			logger.info("[LOCK Source Member/Account : " + req.getFromMember() + "/" + fromAccount.getId() + "]");

			mapLock.put(targetMember.getUsername() + toAccount.getId(), BigDecimal.ZERO);
			mapLock.lock(targetMember.getUsername() + toAccount.getId(), 80000, TimeUnit.MILLISECONDS);
			logger.info("[LOCK Destination Member/Account : " + req.getToMember() + "/" + toAccount.getId() + "]");

			/*
			 * TODO : PRIORITY Fees Processing (if Priority Fee != null then skip the
			 * Regular Fee Processing)
			 */

			// Code Here

			/*
			 * Regular Fees Processing (Skip this if Priority Fee != null)
			 */

			FeeResult feeResult = feeProcessor.ProcessFee(transferType, fromMember, targetMember, fromAccount,
					toAccount, req.getAmount());
			fees = feeResult.getListTotalFees();

			/*
			 * If Source Account is SystemAccount then skip this validation, otherwise check
			 * for Monthly Limit
			 */

			logger.info("[Source Account : " + fromAccount.getName() + "]");

			if (fromAccount.isSystemAccount() == false) {

				logger.info(
						"[Validating Source Account balance/Monthly limit validation : " + fromAccount.getName() + "]");

				/*
				 * Validate FromMember UpperCredit Limit --> Commented for validating incoming
				 * transaction ONLY (PBI Terbaru 2016)
				 */
				// accountValidation.validateMonthlyLimit(fromMember,
				// fromAccount, feeResult.getFinalAmount(), true);

				/*
				 * Get FromMember Balance Inquiry (Lock Account)
				 */
				accountValidation.validateAccountBalance(fromMember, fromAccount, feeResult.getFinalAmount(), true);
			}

			/*
			 * If Destination Account is SystemAccount then skip this validation, otherwise
			 * check for Monthly Limit
			 */

			logger.info("[Destination Account : " + toAccount.getName() + "]");

			if (toAccount.isSystemAccount() == false) {

				logger.info("[Validating Destination Account balance/Monthly limit validation : " + toAccount.getName()
						+ "]");

				/*
				 * Get ToMember UpperCredit Limit
				 */
				accountValidation.validateMonthlyLimit(targetMember, toAccount, feeResult.getFinalAmount(), false);

				/*
				 * Get ToMember Balance Inquiry (Lock Account)
				 */
				accountValidation.validateAccountBalance(targetMember, toAccount, feeResult.getFinalAmount(), false);
			}

			/*
			 * All Validation PASSED then generate Transaction Number
			 */
			String clusterid = System.getProperty("mule.clusterId") != null ? System.getProperty("mule.clusterId")
					: "00";
			String trxNo = Utils.GetDate("yyyyMMddkkmmssSSS") + clusterid + Utils.GenerateTransactionNumber();

			Integer transferID = baseRepository.getTransferRepository().createTransfers(req,
					feeResult.getTransactionAmount(), transferType, fromAccount, toAccount, fromMember, targetMember,
					trxNo, null, transactionState, wsID);

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
			pc.setToMember(targetMember);
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

}

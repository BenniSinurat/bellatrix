package org.bellatrix.process;

import org.apache.log4j.Logger;
import org.bellatrix.data.Accounts;
import org.bellatrix.data.BankTransferRequest;
import org.bellatrix.data.BankTransfers;
import org.bellatrix.data.FeeResult;
import org.bellatrix.data.Members;
import org.bellatrix.data.Status;
import org.bellatrix.data.TransactionException;
import org.bellatrix.data.TransferTypes;
import org.bellatrix.data.WebServices;
import org.bellatrix.services.BankAccountTransferConfirmationRequest;
import org.bellatrix.services.BankAccountTransferRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InterBankValidation {

	@Autowired
	private BaseRepository baseRepository;
	@Autowired
	private FeeProcessor feeProcessor;
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
	private BankTransferValidation bankTransferValidation;
	private Logger logger = Logger.getLogger(PaymentValidation.class);

	public BankTransferRequest validateTransferBank(String token, BankAccountTransferRequest req)
			throws TransactionException {

		/*
		 * Validate Webservice Access
		 */
		WebServices ws = webserviceValidation.validateWebservice(token);
		Integer wsID = ws.getId();

		/*
		 * Validate FromMember
		 */
		Members fromMember = memberValidation.validateMember(req.getUsername(), true);

		/*
		 * Validate FromMember Credential
		 */
		//credentialValidation.validateCredential(ws, req.getAccessTypeID(), req.getCredential(), fromMember);

		/*
		 * Validate Bank Code
		 */
		BankTransfers bank = bankTransferValidation.validateBank(req.getBankID(), fromMember.getGroupID());

		/*
		 * Validate Account No
		 */
		String name = bankTransferValidation.validateAccountNo(bank.getId(), fromMember.getId(),
				req.getAccountNumber());

		/*
		 * Validate FromMember Group Permission to Webservice
		 */
		groupValidation.validateGroupPermission(wsID, fromMember.getGroupID());

		/*
		 * Validate ToMember
		 */
		Members toMember = memberValidation.validateMemberID(bank.getToMemberID(), false);

		/*
		 * Validate TransferType
		 */
		TransferTypes transferType = transferTypeValidation.validateTransferType(bank.getTransferTypeID(),
				fromMember.getGroupID(), req.getAmount(), fromMember.getId());

		/*
		 * Validate FromAccount
		 */
		Accounts fromAccount = accountValidation.validateAccount(transferType, fromMember, true);
		Accounts toAccount = null;
		if (transferType.getFromAccounts() != transferType.getToAccounts()) {

			/*
			 * Validate ToAccount
			 */
			toAccount = accountValidation.validateAccount(transferType, toMember, false);
		} else {
			toAccount = fromAccount;
		}

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

		BankTransferRequest btr = new BankTransferRequest();
		btr.setAmount(req.getAmount());
		btr.setFinalAmount(feeResult.getFinalAmount());
		btr.setTotalFees(feeResult.getTotalFees());
		btr.setTransactionAmount(feeResult.getTransactionAmount());
		btr.setBankCode(bank.getBankCode());
		btr.setChargingCode(bank.getChargingCode());
		btr.setDescription(req.getDescription());
		btr.setFromAccountNumber(bank.getPoolAccountNo());
		btr.setGatewayURL(bank.getGatewayURL());
		btr.setRemark(req.getUsername());
		btr.setSwiftCode(bank.getSwiftCode());
		btr.setToAccountNumber(req.getAccountNumber());
		btr.setToAccountName(name);
		btr.setBankName(bank.getBankName());
		btr.setFromUsername(toMember.getUsername());
		btr.setTransferTypeID(bank.getTransferTypeID());
		btr.setFromMember(fromMember);

		/*
		 * Destination Customer Profile : (1=Indvidual, 2= Company,
		 * 3=Government)
		 * 
		 */
		btr.setToProfileType("1");

		/*
		 * Destination Customer Residential Status : (1=Resident, 2= Non
		 * Resident)
		 * 
		 */
		btr.setToResidentStatus("1");
		btr.setTraceNumber(req.getTraceNumber());

		/*
		 * Transfer Method : (0=Inhouse, 1=RTGS, 2=SKN, 3=TT or Swift)
		 */
		btr.setTransferMethod(bank.getTransferMethod());
		return btr;
	}

	public BankTransfers validateAccountTransferRequest(BankAccountTransferConfirmationRequest req)
			throws TransactionException {
		Members member = memberValidation.validateMember(req.getUsername(), true);
		BankTransfers bank = baseRepository.getInterBankRepository().validateBankAccountNoByMember(member.getId(),
				req.getAccountNumber());

		if (bank == null) {
			throw new TransactionException(String.valueOf(Status.ACCOUNT_NOT_FOUND));
		}

		return bank;

	}

}

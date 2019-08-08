package org.bellatrix.process;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.xml.ws.Holder;

import org.apache.log4j.Logger;
import org.bellatrix.data.AccountView;
import org.bellatrix.data.Accounts;
import org.bellatrix.data.Header;
import org.bellatrix.data.MemberView;
import org.bellatrix.data.Members;
import org.bellatrix.data.PaymentFields;
import org.bellatrix.data.Status;
import org.bellatrix.data.StatusBuilder;
import org.bellatrix.data.TransactionException;
import org.bellatrix.data.TransferHistory;
import org.bellatrix.data.TransferTypeFields;
import org.bellatrix.data.Transfers;
import org.bellatrix.data.WebServices;
import org.bellatrix.services.Account;
import org.bellatrix.services.AccountsPermissionRequest;
import org.bellatrix.services.AccountsPermissionResponse;
import org.bellatrix.services.BalanceInquiryRequest;
import org.bellatrix.services.BalanceInquiryResponse;
import org.bellatrix.services.AccountsRequest;
import org.bellatrix.services.LoadAccountsByGroupsRequest;
import org.bellatrix.services.LoadAccountsByGroupsResponse;
import org.bellatrix.services.LoadAccountsByIDRequest;
import org.bellatrix.services.LoadAccountsByIDResponse;
import org.bellatrix.services.LoadAccountsRequest;
import org.bellatrix.services.LoadAccountsResponse;
import org.bellatrix.services.TransactionHistoryRequest;
import org.bellatrix.services.TransactionHistoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountServiceImpl implements Account {

	@Autowired
	private BaseRepository baseRepository;
	@Autowired
	private CredentialValidation credentialValidation;
	@Autowired
	private WebserviceValidation webserviceValidation;
	@Autowired
	private MemberValidation memberValidation;
	@Autowired
	private AccountValidation accountValidation;
	private Logger logger = Logger.getLogger(AccessServiceImpl.class);

	@Override
	public LoadAccountsByIDResponse loadAccountsByID(Holder<Header> headerParam, LoadAccountsByIDRequest req) {
		LoadAccountsByIDResponse accountResponse = new LoadAccountsByIDResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			if (req.getGroupID() != null) {
				Accounts accounts = baseRepository.getAccountRepository().loadAccountsByID(req.getId(),
						req.getGroupID());
				if (accounts == null) {
					accountResponse.setStatus(StatusBuilder.getStatus(Status.ACCOUNT_NOT_FOUND));
					return accountResponse;
				}
				accountResponse.setAccount(accounts);
			} else {
				Accounts accounts = baseRepository.getAccountRepository().loadAccountsByID(req.getId());
				if (accounts == null) {
					accountResponse.setStatus(StatusBuilder.getStatus(Status.ACCOUNT_NOT_FOUND));
					return accountResponse;
				}
				accountResponse.setAccount(accounts);
			}
			accountResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return accountResponse;
		} catch (TransactionException e) {
			accountResponse.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return accountResponse;
		}
	}

	@Override
	public LoadAccountsByGroupsResponse loadAccountsByGroups(Holder<Header> headerParam,
			LoadAccountsByGroupsRequest req) {
		LoadAccountsByGroupsResponse accountResponse = new LoadAccountsByGroupsResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			List<Accounts> accounts = baseRepository.getAccountRepository().loadAccountsByGroups(req);
			accountResponse.setAccounts(accounts);
			accountResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return accountResponse;
		} catch (TransactionException e) {
			accountResponse.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return accountResponse;
		}
	}

	@Override
	public BalanceInquiryResponse loadBalanceInquiry(Holder<Header> headerParam, BalanceInquiryRequest req) {
		BalanceInquiryResponse balanceResponse = new BalanceInquiryResponse();

		/*
		 * Validate Webservice
		 */
		try {
			WebServices ws = webserviceValidation.validateWebservice(headerParam.value.getToken());

			/*
			 * Validate FromMember
			 */
			Members fromMember = memberValidation.validateMember(req.getUsername(), true);

			MemberView memberView = new MemberView();
			memberView.setId(fromMember.getId());
			memberView.setName(fromMember.getName());
			memberView.setUsername(fromMember.getUsername());

			/*
			 * Validate FromMember Credential
			 */
			credentialValidation.validateCredential(ws, req.getAccessTypeID(), req.getCredential(), fromMember);

			/*
			 * Validate AccountID
			 */
			Accounts account = accountValidation.validateAccount(req.getAccountID(), fromMember.getGroupID());
			AccountView accountView = new AccountView();
			accountView.setId(account.getId());
			accountView.setName(account.getName());

			BigDecimal balance = baseRepository.getAccountRepository().loadBalanceInquiry(req.getUsername(),
					req.getAccountID());

			BigDecimal reservedAmount = baseRepository.getAccountRepository().loadReservedAmount(req.getUsername(),
					req.getAccountID());

			balanceResponse.setBalance(balance);
			balanceResponse.setFormattedBalance(Utils.formatAmount(balance));
			balanceResponse.setReservedAmount(reservedAmount);
			balanceResponse.setFormattedReservedAmount(Utils.formatAmount(reservedAmount));
			balanceResponse.setAccount(accountView);
			balanceResponse.setMember(memberView);
			balanceResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return balanceResponse;
		} catch (TransactionException ce) {
			balanceResponse.setStatus(StatusBuilder.getStatus(ce.getMessage()));
			return balanceResponse;
		}

	}

	@Override
	public TransactionHistoryResponse loadTransactionHistory(Holder<Header> headerParam,
			TransactionHistoryRequest req) {
		TransactionHistoryResponse history = new TransactionHistoryResponse();
		try {
			WebServices ws = webserviceValidation.validateWebservice(headerParam.value.getToken());

			/*
			 * Validate FromMember
			 */
			Members fromMember = memberValidation.validateMember(req.getUsername(), true);

			/*
			 * Validate FromMember Credential
			 */
			credentialValidation.validateCredential(ws, req.getAccessTypeID(), req.getCredential(), fromMember);

			String fromDate = req.getFromDate() != null ? req.getFromDate() : Utils.GetDate("yyyy-MM-dd");
			String toDate = req.getFromDate() != null ? req.getToDate() : Utils.GetDate("yyyy-MM-dd");
			String orderBy = req.getOrderBy() != null ? req.getOrderBy() : "id";
			String descendingOrder = req.getDescendingOrder() == null || req.getDescendingOrder() == true ? "desc"
					: "asc";
			List<Transfers> trf = baseRepository.getAccountRepository().loadTransferFromUsername(req.getUsername(),
					req.getAccountID(), req.getCurrentPage(), req.getPageSize(), fromDate, toDate, orderBy,
					descendingOrder);

			if (trf.size() == 0) {
				history.setStatus(StatusBuilder.getStatus(Status.NO_TRANSACTION));
				return history;
			}

			Integer totalDisplayRecords = baseRepository.getAccountRepository()
					.countTotalTransaction(fromMember.getId(), req.getAccountID(), fromDate, toDate);
			Integer totalRecords = baseRepository.getAccountRepository().countTotalRecords();

			List<Integer> transferIDs = new LinkedList<Integer>();
			for (Transfers t : trf) {
				transferIDs.add(t.getId());
			}

			List<PaymentFields> pfield = baseRepository.getCustomFieldRepository()
					.loadMultiPaymentFieldValuesByTransferID(transferIDs);

			Map<Integer, List<PaymentFields>> result = pfield.stream()
					.collect(Collectors.groupingBy(PaymentFields::getTransferID, Collectors.toList()));

			List<TransferHistory> lth = new LinkedList<TransferHistory>();
			for (int i = 0; i < trf.size(); i++) {
				TransferHistory trfHistory = new TransferHistory();
				if (trf.get(i).isCustomField()) {
					List<PaymentFields> paymentFields = result.get(trf.get(i).getId());
					trfHistory.setCustomFields(paymentFields);
				}
				MemberView fromMemberTrf = new MemberView();
				fromMemberTrf.setId(trf.get(i).getFromMemberID());
				fromMemberTrf.setName(trf.get(i).getFromName());
				fromMemberTrf.setUsername(trf.get(i).getFromUsername());

				MemberView toMemberTrf = new MemberView();
				toMemberTrf.setId(trf.get(i).getToMemberID());
				toMemberTrf.setName(trf.get(i).getToName());
				toMemberTrf.setUsername(trf.get(i).getToUsername());

				if (trf.get(i).getParentID() == null) {
					TransferTypeFields typeField = new TransferTypeFields();
					typeField.setFromAccounts(trf.get(i).getFromAccountID());
					typeField.setToAccounts(trf.get(i).getToAccountID());
					typeField.setName(trf.get(i).getName());
					trfHistory.setTransferType(typeField);
				}

				trfHistory.setAmount(trf.get(i).getAmount());
				trfHistory.setChargedBack(trf.get(i).isChargedBack());
				trfHistory.setDescription(trf.get(i).getDescription());
				trfHistory.setFromMember(fromMemberTrf);
				trfHistory.setToMember(toMemberTrf);
				trfHistory.setId(trf.get(i).getId());
				trfHistory.setParentID(trf.get(i).getParentID());
				trfHistory.setTraceNumber(trf.get(i).getTraceNumber().substring(1));
				trfHistory.setTransactionDate(trf.get(i).getTransactionDate());
				trfHistory.setTransactionNumber(trf.get(i).getTransactionNumber());
				trfHistory.setTransactionState(trf.get(i).getTransactionState());
				lth.add(trfHistory);
			}

			history.setTransfers(lth);
			history.setTotalRecords(totalRecords);
			history.setDisplayRecords(totalDisplayRecords);
			history.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return history;
		} catch (TransactionException ce) {
			history.setStatus(StatusBuilder.getStatus(ce.getMessage()));
			return history;
		}
	}

	@Override
	public LoadAccountsResponse loadAccounts(Holder<Header> headerParam, LoadAccountsRequest req) {
		LoadAccountsResponse accountResponse = new LoadAccountsResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			if (req.getCurrentPage() == null || req.getPageSize() == null) {
				req.setCurrentPage(0);
				req.setPageSize(0);
			}
			List<Accounts> accounts = baseRepository.getAccountRepository().loadAccounts(req.getCurrentPage(),
					req.getPageSize());

			Integer totalRecords = baseRepository.getAccountRepository().countTotalAccount();
			accountResponse.setAccount(accounts);
			accountResponse.setTotalRecords(totalRecords);
			accountResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return accountResponse;
		} catch (TransactionException e) {
			accountResponse.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return accountResponse;
		}
	}

	@Override
	public LoadAccountsResponse loadAllAccounts(Holder<Header> headerParam, LoadAccountsRequest req) {
		LoadAccountsResponse accountResponse = new LoadAccountsResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			if (req.getCurrentPage() == null || req.getPageSize() == null) {
				req.setCurrentPage(0);
				req.setPageSize(0);
			}
			List<Accounts> accounts = baseRepository.getAccountRepository().loadAllAccounts(req.getCurrentPage(),
					req.getPageSize());

			Integer totalRecords = baseRepository.getAccountRepository().countTotalAccount();
			accountResponse.setAccount(accounts);
			accountResponse.setTotalRecords(totalRecords);
			accountResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return accountResponse;
		} catch (TransactionException e) {
			accountResponse.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return accountResponse;
		}
	}

	@Override
	public void createAccount(Holder<Header> headerParam, AccountsRequest req) throws TransactionException {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			baseRepository.getAccountRepository().createAccount(req.getName(), req.getDescription(),
					req.isSystemAccount());
		} catch (TransactionException e) {
			throw new TransactionException(e.getMessage());
		}
	}

	@Override
	public void updateAccount(Holder<Header> headerParam, AccountsRequest req) throws TransactionException {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			accountValidation.validateAccount(req.getId());
			baseRepository.getAccountRepository().updateAccount(req.getId(), req.getName(), req.getDescription(),
					req.isSystemAccount());
		} catch (TransactionException e) {
			throw new TransactionException(e.getMessage());
		}
	}

	@Override
	public void createAccountPermission(Holder<Header> headerParam, AccountsPermissionRequest req)
			throws TransactionException {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			accountValidation.validateAccount(req.getAccountID());
			baseRepository.getAccountRepository().addAccountPermission(req.getAccountID(), req.getGroupID(),
					req.getCreditLimit(), req.getUpperCreditLimit(), req.getLowerCreditLimit());
		} catch (TransactionException e) {
			throw new TransactionException(e.getMessage());
		}
	}

	@Override
	public void updateAccountPermission(Holder<Header> headerParam, AccountsPermissionRequest req)
			throws TransactionException {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			accountValidation.validateAccount(req.getAccountID());
			baseRepository.getAccountRepository().updateAccountPermission(req);
		} catch (TransactionException e) {
			throw new TransactionException(e.getMessage());
		}
	}

	@Override
	public void deleteAccountPermission(Holder<Header> headerParam, AccountsPermissionRequest req)
			throws TransactionException {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			accountValidation.validateAccount(req.getAccountID());
			baseRepository.getAccountRepository().deleteAccountPermission(req.getId());
		} catch (TransactionException e) {
			throw new TransactionException(e.getMessage());
		}
	}
	
	@Override
	public AccountsPermissionResponse loadPermissionByAccountID(Holder<Header> headerParam, AccountsPermissionRequest req) {
		AccountsPermissionResponse apr = new AccountsPermissionResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			accountValidation.validateAccount(req.getAccountID());
			List<Accounts> accountPermission = baseRepository.getAccountRepository().listGroupByAccountID(req.getAccountID());
			apr.setAccountPermissions(accountPermission);
			apr.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
		}catch (Exception e) {
			apr.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return apr;
		}
		return apr;
	}
}

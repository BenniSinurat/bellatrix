package org.bellatrix.process;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;
import org.bellatrix.data.AccountPermissions;
import org.bellatrix.data.Accounts;
import org.bellatrix.data.Members;
import org.bellatrix.data.Status;
import org.bellatrix.data.TransactionException;
import org.bellatrix.data.TransferTypes;
import org.bellatrix.data.Transfers;
import org.bellatrix.services.LoadAccountsByGroupsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

@Component
public class AccountValidation {

	@Autowired
	private BaseRepository baseRepository;
	@Autowired
	private HazelcastInstance instance;
	@Value("${global.cache.config.enabled}")
	private boolean useCache;
	private Logger logger = Logger.getLogger(AccountValidation.class);

	public Accounts validateAccount(Integer accountID) throws TransactionException {
		Accounts fromAccount = null;
		fromAccount = baseRepository.getAccountRepository().loadAccountsByID(accountID);

		if (fromAccount == null) {
			logger.info("[Invalid AccountID [" + accountID + "]");
			throw new TransactionException(String.valueOf(Status.INVALID_ACCOUNT));
		}
		return fromAccount;
	}

	public List<AccountPermissions> validateAccountPermission(Integer accountID) throws TransactionException {
		List<AccountPermissions> accountPermission = null;
		accountPermission = baseRepository.getAccountRepository().loadGroupPermissionByAccounts(accountID);

		if (accountPermission.size() == 0) {
			logger.info("[Invalid AccountID [" + accountID + "]");
			throw new TransactionException(String.valueOf(Status.INVALID_ACCOUNT));
		}
		return accountPermission;
	}

	public List<AccountPermissions> validateAccountPermissionByID(Integer id) throws TransactionException {
		List<AccountPermissions> accountPermission = null;
		accountPermission = baseRepository.getAccountRepository().loadGroupPermissionByID(id);

		if (accountPermission.size() == 0) {
			logger.info("[Invalid Permission ID [" + id + "]");
			throw new TransactionException(String.valueOf(Status.INVALID_ACCOUNT));
		}
		return accountPermission;
	}

	public Accounts validateAccount(Integer accountID, Integer groupID) throws TransactionException {
		Accounts fromAccount = null;

		if (useCache) {
			IMap<String, Accounts> accMap = instance.getMap("AccountMap");
			fromAccount = accMap.get(String.valueOf(accountID + ":" + groupID));
		} else {
			fromAccount = baseRepository.getAccountRepository().loadAccountsByID(accountID, groupID);
		}

		if (fromAccount == null) {
			logger.info("[Invalid AccountID [" + accountID + "]");
			throw new TransactionException(String.valueOf(Status.INVALID_ACCOUNT));
		}
		return fromAccount;
	}

	public List<Accounts> validateAccountByGroup(LoadAccountsByGroupsRequest req) {
		return baseRepository.getAccountRepository().loadAccountsByGroups(req);
	}

	public Accounts validateAccount(TransferTypes transferType, Members member, boolean source)
			throws TransactionException {
		if (source) {
			Accounts fromAccount = null;

			if (useCache) {
				IMap<String, Accounts> accMap = instance.getMap("AccountMap");
				fromAccount = accMap.get(String.valueOf(transferType.getFromAccounts() + ":" + member.getGroupID()));
			} else {
				fromAccount = baseRepository.getAccountRepository().loadAccountsByID(transferType.getFromAccounts(),
						member.getGroupID());
			}

			if (fromAccount == null) {
				logger.info("[Invalid Source AccountID [" + transferType.getFromAccounts() + "]  For TransferTypeID ["
						+ transferType.getId() + "]]");
				throw new TransactionException(String.valueOf(Status.INVALID_ACCOUNT));
			}
			return fromAccount;

		} else {
			Accounts toAccount = null;

			if (useCache) {
				IMap<String, Accounts> accMap = instance.getMap("AccountMap");
				toAccount = accMap.get(String.valueOf(transferType.getToAccounts() + ":" + member.getGroupID()));
			} else {
				toAccount = baseRepository.getAccountRepository().loadAccountsByID(transferType.getToAccounts(),
						member.getGroupID());
			}

			if (toAccount == null) {
				logger.info("[Invalid Destination AccountID [" + transferType.getToAccounts()
						+ "]  For TransferTypeID [" + transferType.getId() + "]]");
				throw new TransactionException(String.valueOf(Status.INVALID_DESTINATION_ACCOUNT));
			}
			return toAccount;
		}

	}

	public BigDecimal validateMonthlyLimit(Members member, Accounts account, BigDecimal totalPositiveFee,
			boolean source) throws TransactionException {
		BigDecimal limit = baseRepository.getAccountRepository().loadUpperCreditLimitBalance(member.getUsername(),
				account.getId());
		String marking = source == true ? "Source" : "Target";
		logger.info("[" + member.getUsername() + " (" + marking + ") Account Monthly Limit : " + limit + "/"
				+ account.getUpperCreditLimit() + "]");

		if (limit.add(totalPositiveFee).compareTo(account.getUpperCreditLimit()) == 1) {
			if (source) {
				logger.info("[Source Monthly Account has Over Limit]");
				throw new TransactionException(String.valueOf(Status.CREDIT_LIMIT_REACHED));
			} else {
				logger.info("[Destination Monthly Account has Over Limit]");
				throw new TransactionException(String.valueOf(Status.DESTINATION_CREDIT_LIMIT_REACHED));
			}
		}
		return limit;
	}

	public BigDecimal validateAccountBalance(Members member, Accounts account, BigDecimal finalAmount, boolean source)
			throws TransactionException {

		BigDecimal balance = BigDecimal.ZERO;
		BigDecimal accountBalance = baseRepository.getAccountRepository().loadBalanceInquiry(member.getUsername(),
				account.getId());
		BigDecimal reservedBalance = baseRepository.getAccountRepository().loadReservedAmount(member.getUsername(),
				account.getId());
		balance = accountBalance.add(reservedBalance);
		String marking = source == true ? " (-) " : " (+) ";

		logger.info("[" + member.getUsername() + " Account Balance : " + balance + marking + " Amount : " + finalAmount
				+ "]");
		logger.info(
				"[Lower Credit Limit : " + account.getLowerCreditLimit() + "/" + balance.subtract(finalAmount) + "]");

		if (source) {
			if ((balance.subtract(finalAmount)).compareTo(account.getLowerCreditLimit()) < 0) {
				logger.info("[Source Account has Insufficient Balance]");
				throw new TransactionException(String.valueOf(Status.INSUFFICIENT_BALANCE));
			}
		} else {
			logger.info("[Credit Limit : " + account.getCreditLimit() + "/" + balance.add(finalAmount) + "]");
			if ((balance.add(finalAmount)).compareTo(account.getCreditLimit()) > 0) {
				throw new TransactionException(String.valueOf(Status.CREDIT_LIMIT_REACHED));
			}
		}

		return balance;
	}

	public BigDecimal loadBalanceInquiry(String username, Integer accountID) {
		return baseRepository.getAccountRepository().loadBalanceInquiry(username, accountID);
	}

	public BigDecimal loadReservedAmount(String username, Integer accountID) {
		return baseRepository.getAccountRepository().loadReservedAmount(username, accountID);
	}

	public Integer countTotalTransaction(Integer memberID, Integer accountID, String fromDate, String toDate) {
		return baseRepository.getAccountRepository().countTotalTransaction(memberID, accountID, fromDate, toDate);
	}

	public Integer countTotalRecords() {
		return baseRepository.getAccountRepository().countTotalRecords();

	}

	public Integer countTotalAccounts() {
		return baseRepository.getAccountRepository().countTotalAccount();

	}

	public List<Transfers> loadTransferFromUsername(String username, Integer accountID, Integer currentPage,
			Integer pageSize, String fromDate, String toDate, String orderBy, String orderType) {
		return baseRepository.getAccountRepository().loadTransferFromUsername(username, accountID, currentPage,
				pageSize, fromDate, toDate, orderBy, orderType);

	}

	public boolean validateAccountCurrency(Accounts fromAccount, Accounts toAccount) throws TransactionException {
		if (fromAccount.getCurrency().getId() == toAccount.getCurrency().getId()) {
			return true;
		} else {
			throw new TransactionException(String.valueOf(Status.INVALID_CURRENCY));
		}
	}
}

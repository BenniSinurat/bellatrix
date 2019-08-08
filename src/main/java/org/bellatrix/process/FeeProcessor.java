package org.bellatrix.process;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.bellatrix.data.Accounts;
import org.bellatrix.data.ExternalMemberFields;
import org.bellatrix.data.FeeResult;
import org.bellatrix.data.Fees;
import org.bellatrix.data.Members;
import org.bellatrix.data.PaymentFields;
import org.bellatrix.data.Status;
import org.bellatrix.data.TransactionException;
import org.bellatrix.data.TransferTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

@Component
public class FeeProcessor {

	@Autowired
	private BaseRepository baseRepository;
	@Autowired
	private HazelcastInstance instance;
	@Autowired
	private AccountValidation accountValidation;
	@Value("${global.cache.config.enabled}")
	private boolean useCache;
	private Logger logger = Logger.getLogger(FeeProcessor.class);

	public FeeResult ProcessFee(TransferTypes transferType, Members fromMember, Members toMember, Accounts fromAccount,
			Accounts toAccount, BigDecimal amount) throws TransactionException {

		IMap<Integer, List<Fees>> feeMap = instance.getMap("FeeMap");
		IMap<String, BigDecimal> mapLock = instance.getMap("AccountLock");
		List<Fees> fees = null;

		if (useCache) {
			fees = feeMap.get(transferType.getId());
		} else {
			fees = baseRepository.getTransferTypeRepository().getFeeFromTransferTypeID(transferType.getId());
		}

		List<BigDecimal> positiveFees = new LinkedList<BigDecimal>();
		List<BigDecimal> negativeFees = new LinkedList<BigDecimal>();
		List<Fees> finalFees = new LinkedList<Fees>();

		if (!fees.isEmpty()) {
			for (int i = 0; i < fees.size(); i++) {
				BigDecimal fixed = BigDecimal.ZERO;
				BigDecimal percentage = BigDecimal.ZERO;

				logger.info(
						"[Processing Fee ID [" + fees.get(i).getId() + "] --> " + (i + 1) + " Of " + fees.size() + "]");
				/*
				 * Fee Date Validation
				 */
				if (fees.get(i).getStartDate() != null && fees.get(i).getStartDate().compareTo(new Date()) == 1) {
					logger.info("[Fee Initial DateTime Has not started yet]");
					continue;
				}

				if (fees.get(i).getEndDate() != null && fees.get(i).getEndDate().compareTo(new Date()) == -1) {
					logger.info("[Fee End DateTime Has Already Expired]");
					continue;
				}

				/*
				 * Fee Group Validation Parameter : feeID, groupID, destination
				 * (true for source, false for destination)
				 */
				if (!fees.get(i).isFromAllGroup() && !baseRepository.getTransferTypeRepository()
						.validateFeeByGroup(fees.get(i).getId(), fromMember.getGroupID(), true)) {
					logger.info("[Source Group ID [" + fromMember.getGroupID() + "] is not registered for FeeID ["
							+ fees.get(i).getId() + "]]");
					continue;
				}

				if (!fees.get(i).isToAllGroup() && !baseRepository.getTransferTypeRepository()
						.validateFeeByGroup(fees.get(i).getId(), toMember.getGroupID(), false)) {
					logger.info("[Destination Group ID " + toMember.getGroupID() + "] is not registered for FeeID ["
							+ fees.get(i).getId() + "]]");
					continue;
				}

				/*
				 * Fee Initial and maximum range amount validation
				 */
				if (fees.get(i).getInitialRangeAmount().compareTo(BigDecimal.ZERO) > 0
						&& fees.get(i).getInitialRangeAmount().compareTo(amount) >= 1) {
					logger.info("[Transaction Below Min Fee Limit : " + fees.get(i).getInitialRangeAmount() + "/"
							+ amount + "]");
					continue;
				}

				if (fees.get(i).getMaximumRangeAmount().compareTo(BigDecimal.ZERO) > 0
						&& fees.get(i).getMaximumRangeAmount().compareTo(amount) <= -1) {
					logger.info("[Transaction Above Max Fee Limit : " + fees.get(i).getMaximumRangeAmount() + "/"
							+ amount + "]");
					continue;
				}

				/*
				 * VALIDATE FEE ACCOUNT : 
				 * If value = 0 -> fill with Source memberID/accountID, 
				 * If value = -1 -> fill with destination memberID/accountID, 
				 * If value = -2 fill with FromMember ParentID, 
				 * If value = -3 fill with ToMember ParentID
				 */

				if (fees.get(i).getFromMemberID() == 0 || fees.get(i).getFromAccountID() == 0) {
					fees.get(i).setFromMemberID(fromMember.getId());
					fees.get(i).setFromAccountID(fromAccount.getId());
				}

				if (fees.get(i).getFromMemberID() == -1 || fees.get(i).getFromAccountID() == -1) {
					fees.get(i).setFromMemberID(toMember.getId());
					fees.get(i).setFromAccountID(toAccount.getId());
				}

				if (fees.get(i).getFromMemberID() == -2) {
					List<ExternalMemberFields> emv = fromMember.getExternalMembers();
					logger.info("[From Member Parent Size : (" + emv.size() + ")]");

					for (int e = 0; e < emv.size(); e++) {
						logger.info("[From Member ID (" + i + ") : " + emv.get(i).getId() + "]");
					}

					Collections.sort(emv, new Comparator<ExternalMemberFields>() {
						public int compare(ExternalMemberFields o1, ExternalMemberFields o2) {
							int x1 = ((ExternalMemberFields) o1).getId();
							int x2 = ((ExternalMemberFields) o2).getId();
							return x1 - x2;
						}
					});

					if (emv.get(0).getParentID() == fromMember.getId()) {
						continue;
					}

					logger.info("[From Member ParentID Selected : " + emv.get(0).getParentID() + "]");
					fees.get(i).setFromMemberID(emv.get(0).getParentID());
				}

				if (fees.get(i).getFromMemberID() == -3) {
					List<ExternalMemberFields> emv = toMember.getExternalMembers();
					logger.info("[To Member Parent Size : (" + emv.size() + ")]");

					for (int e = 0; e < emv.size(); e++) {
						logger.info("[To Member ID (" + i + ") : " + emv.get(i).getId() + "]");
					}

					Collections.sort(emv, new Comparator<ExternalMemberFields>() {
						public int compare(ExternalMemberFields o1, ExternalMemberFields o2) {
							int x1 = ((ExternalMemberFields) o1).getId();
							int x2 = ((ExternalMemberFields) o2).getId();
							return x1 - x2;
						}
					});

					if (emv.get(0).getParentID() == fromMember.getId()) {
						continue;
					}

					logger.info("[To Member ParentID Selected : " + emv.get(0).getParentID() + "]");
					fees.get(i).setFromMemberID(emv.get(0).getParentID());
				}

				if (fees.get(i).getToMemberID() == 0 || fees.get(i).getToAccountID() == 0) {
					fees.get(i).setToMemberID(fromMember.getId());
					fees.get(i).setToAccountID(fromAccount.getId());
				}

				if (fees.get(i).getToMemberID() == -1 || fees.get(i).getToAccountID() == -1) {
					fees.get(i).setToMemberID(toMember.getId());
					fees.get(i).setToAccountID(toAccount.getId());
				}

				if (fees.get(i).getToMemberID() == -2) {
					List<ExternalMemberFields> emv = fromMember.getExternalMembers();
					logger.info("[From Member Parent Size : (" + emv.size() + ")]");

					for (int e = 0; e < emv.size(); e++) {
						logger.info("[From Member ID (" + i + ") : " + emv.get(i).getId() + "]");
					}

					Collections.sort(emv, new Comparator<ExternalMemberFields>() {
						public int compare(ExternalMemberFields o1, ExternalMemberFields o2) {
							int x1 = ((ExternalMemberFields) o1).getId();
							int x2 = ((ExternalMemberFields) o2).getId();
							return x1 - x2;
						}
					});

					if (emv.get(0).getParentID() == toMember.getId()) {
						continue;
					}

					logger.info("[From Member ParentID Selected : " + emv.get(0).getParentID() + "]");
					fees.get(i).setToMemberID(emv.get(0).getParentID());
				}

				if (fees.get(i).getToMemberID() == -3) {
					List<ExternalMemberFields> emv = toMember.getExternalMembers();
					logger.info("[To Member Parent Size : (" + emv.size() + ")]");

					for (int e = 0; e < emv.size(); e++) {
						logger.info("[To Member ID (" + i + ") : " + emv.get(i).getId() + "]");
					}

					Collections.sort(emv, new Comparator<ExternalMemberFields>() {
						public int compare(ExternalMemberFields o1, ExternalMemberFields o2) {
							int x1 = ((ExternalMemberFields) o1).getId();
							int x2 = ((ExternalMemberFields) o2).getId();
							return x1 - x2;
						}
					});

					if (emv.get(0).getParentID() == toMember.getId()) {
						continue;
					}

					logger.info("[To Member ParentID Selected : " + emv.get(0).getParentID() + "]");
					fees.get(i).setToMemberID(emv.get(0).getParentID());
				}

				/*
				 * Fee calculation
				 */

				if (fees.get(i).getFixedAmount().compareTo(BigDecimal.ZERO) == 1) {
					fixed = fees.get(i).getFixedAmount();
				}

				if (fees.get(i).getPercentageValue().compareTo(BigDecimal.ZERO) == 1) {
					percentage = amount.multiply(fees.get(i).getPercentageValue()).divide(new BigDecimal(100));
				}

				if (fixed.compareTo(percentage) == 0 || fixed.compareTo(percentage) == 1) {
					if (fees.get(i).isDeductAmount()) {
						logger.info("[Substract : " + fixed + " From total Amount From Source]");
						negativeFees.add(fixed.negate());
					} else {
						logger.info("[Add : " + fixed + " From total Amount From Source]");
						positiveFees.add(fixed);
					}
					fees.get(i).setFeeAmount(fixed);
				} else {
					if (fees.get(i).isDeductAmount()) {
						logger.info("[Substract : " + fees.get(i).getPercentageValue()
								+ "% From total Amount From Source]");
						negativeFees.add(percentage.negate());
					} else {
						logger.info("[Add : " + fees.get(i).getPercentageValue() + "% From total Amount From Source]");
						positiveFees.add(percentage);
					}
					fees.get(i).setFeeAmount(percentage);
				}

				if (fromMember.getId().compareTo(fees.get(i).getFromMemberID()) != 0
						&& toMember.getId().compareTo(fees.get(i).getFromMemberID()) != 0) {

					/*
					 * From Fee Member Validation
					 */
					Members fromFeeMember = baseRepository.getMembersRepository().findOneMembers("id",
							fees.get(i).getFromMemberID());

					if (fromFeeMember == null) {
						logger.info("[Source Fee MemberID Not Found [" + fees.get(i).getFromMemberID() + "]]");
						throw new TransactionException(String.valueOf(Status.MEMBER_NOT_FOUND));
					}

					/*
					 * From Fee Account Validation
					 */
					Accounts fromFeeAccount = baseRepository.getAccountRepository()
							.loadAccountsByID(fees.get(i).getFromAccountID(), fromFeeMember.getGroupID());
					if (fromFeeAccount == null) {
						logger.info("[Invalid Source Fee AccountID [" + fees.get(i).getFromAccountID() + "]]");
						throw new TransactionException(String.valueOf(Status.INVALID_ACCOUNT));
					}

					fees.get(i).setFromMember(fromFeeMember);

					/*
					 * Lock and check upper credit limit and balance here ! (if
					 * account != SystemAccount)
					 */

					if (fromFeeAccount.isSystemAccount() == false) {

						logger.info("[Trying to Lock Fee Source . . . ]");
						if (mapLock.isLocked(
								fees.get(i).getFromMember().getUsername() + fees.get(i).getFromAccountID()) == false) {
							logger.info("[LOCK Source : " + fees.get(i).getFromMember().getUsername() + "/"
									+ +fees.get(i).getFromAccountID() + "]");
							mapLock.lock(fees.get(i).getFromMember().getUsername() + fees.get(i).getFromAccountID(),
									80000, TimeUnit.MILLISECONDS);
						}

						accountValidation.validateMonthlyLimit(fromFeeMember, fromFeeAccount,
								fees.get(i).getFeeAmount(), true);

						accountValidation.validateAccountBalance(fromFeeMember, fromAccount, fees.get(i).getFeeAmount(),
								true);

					}

				} else {

					fees.get(i).setFromMember(fromMember);
				}

				if (toMember.getId().compareTo(fees.get(i).getToMemberID()) != 0
						&& fromMember.getId().compareTo(fees.get(i).getToMemberID()) != 0) {

					/*
					 * To Fee Member Validation
					 */

					Members toFeeMember = baseRepository.getMembersRepository().findOneMembers("id",
							fees.get(i).getToMemberID());

					if (toFeeMember == null) {
						logger.info("[Destination Fee MemberID Not Found [" + fees.get(i).getToMemberID() + "]]");
						throw new TransactionException(String.valueOf(Status.DESTINATION_MEMBER_NOT_FOUND));
					}

					/*
					 * To Fee Account Validation
					 */

					Accounts toFeeAccount = baseRepository.getAccountRepository()
							.loadAccountsByID(fees.get(i).getToAccountID(), toFeeMember.getGroupID());
					if (toFeeAccount == null) {
						logger.info("[Invalid Destination Fee AccountID [" + fees.get(i).getToAccountID() + "]]");
						throw new TransactionException(String.valueOf(Status.INVALID_DESTINATION_ACCOUNT));

					}

					fees.get(i).setToMember(toFeeMember);

					/*
					 * Lock and check upper credit limit here ! (if account !=
					 * SystemAccount)
					 */

					if (toFeeAccount.isSystemAccount() == false) {

						logger.info("[Trying to Lock Fee Target . . . ]");
						if (mapLock.isLocked(
								fees.get(i).getToMember().getUsername() + fees.get(i).getToAccountID()) == false) {
							logger.info("[LOCK Target : " + fees.get(i).getToMember().getUsername() + "/"
									+ fees.get(i).getToAccountID() + "]");
							mapLock.lock(fees.get(i).getToMember().getUsername() + fees.get(i).getToAccountID(), 80000,
									TimeUnit.MILLISECONDS);
						}

						accountValidation.validateMonthlyLimit(toFeeMember, toFeeAccount, fees.get(i).getFeeAmount(),
								false);

						accountValidation.validateAccountBalance(toFeeMember, toFeeAccount, fees.get(i).getFeeAmount(),
								false);
					}

				} else {

					fees.get(i).setToMember(toMember);

				}

				String clusterid = System.getProperty("mule.clusterId") != null ? System.getProperty("mule.clusterId")
						: "00";
				String feeTrxNo = Utils.GetDate("yyyyMMddkkmmssSSS") + clusterid + Utils.GenerateTransactionNumber();
				fees.get(i).setTransactionNumber(feeTrxNo);
				finalFees.add(fees.get(i));
			}
		}

		logger.info("[Transaction Amount From Request: " + amount + "]");
		BigDecimal totalPositiveFee = amount;
		for (BigDecimal b : positiveFees) {
			totalPositiveFee = totalPositiveFee.add(b);
			logger.info("[Adding Positive fee(s) : " + b + " = " + totalPositiveFee + "]");
		}

		BigDecimal totalNegativeFee = amount;
		for (BigDecimal b : negativeFees) {
			totalNegativeFee = totalNegativeFee.add(b);
			logger.info("[Adding Negative fee(s) : " + b + " = " + totalNegativeFee + "]");
		}

		BigDecimal totalFee = totalPositiveFee.subtract(totalNegativeFee);

		if (totalNegativeFee.compareTo(BigDecimal.ZERO) < 0) {
			logger.warn("[NEGATIVE Total amount is NOT Allowed]");
			throw new TransactionException("NEGATIVE_AMOUNT");
		}

		FeeResult fr = new FeeResult();
		fr.setFinalAmount(totalPositiveFee);
		fr.setTransactionAmount(totalNegativeFee);
		fr.setTotalFees(totalFee);
		fr.setListTotalFees(finalFees);
		logger.info(fr.toString());
		return fr;

	}

	public FeeResult CalculateFee(TransferTypes transferType, Members fromMember, Members toMember,
			Accounts fromAccount, Accounts toAccount, BigDecimal amount) throws TransactionException {

		List<Fees> fees = baseRepository.getTransferTypeRepository().getFeeFromTransferTypeID(transferType.getId());

		List<BigDecimal> positiveFees = new LinkedList<BigDecimal>();
		List<BigDecimal> negativeFees = new LinkedList<BigDecimal>();
		List<Fees> finalFees = new LinkedList<Fees>();

		if (!fees.isEmpty()) {
			for (int i = 0; i < fees.size(); i++) {
				BigDecimal fixed = BigDecimal.ZERO;
				BigDecimal percentage = BigDecimal.ZERO;

				logger.info(
						"[Processing Fee ID [" + fees.get(i).getId() + "] --> " + (i + 1) + " Of " + fees.size() + "]");
				/*
				 * Fee Date Validation
				 */
				if (fees.get(i).getStartDate() != null && fees.get(i).getStartDate().compareTo(new Date()) == 1) {
					logger.info("[Fee Initial DateTime Has not started yet]");
					continue;
				}

				if (fees.get(i).getEndDate() != null && fees.get(i).getEndDate().compareTo(new Date()) == -1) {
					logger.info("[Fee End DateTime Has Already Expired]");
					continue;
				}

				/*
				 * Fee Group Validation Parameter : feeID, groupID, destination
				 * (true for source, false for destination)
				 */
				if (!fees.get(i).isFromAllGroup() && !baseRepository.getTransferTypeRepository()
						.validateFeeByGroup(fees.get(i).getId(), fromMember.getGroupID(), true)) {
					logger.info("[Source Group ID [" + fromMember.getGroupID() + "] is not registered for FeeID ["
							+ fees.get(i).getId() + "]]");
					continue;
				}

				if (!fees.get(i).isToAllGroup() && !baseRepository.getTransferTypeRepository()
						.validateFeeByGroup(fees.get(i).getId(), toMember.getGroupID(), false)) {
					logger.info("[Destination Group ID " + toMember.getGroupID() + "] is not registered for FeeID ["
							+ fees.get(i).getId() + "]]");
					continue;
				}

				/*
				 * Fee Initial and maximum range amount validation
				 */
				if (fees.get(i).getInitialRangeAmount().compareTo(BigDecimal.ZERO) > 0
						&& fees.get(i).getInitialRangeAmount().compareTo(amount) >= 1) {
					logger.info("[Transaction Below Min Fee Limit : " + fees.get(i).getInitialRangeAmount() + "/"
							+ amount + "]");
					continue;
				}

				if (fees.get(i).getMaximumRangeAmount().compareTo(BigDecimal.ZERO) > 0
						&& fees.get(i).getMaximumRangeAmount().compareTo(amount) <= -1) {
					logger.info("[Transaction Above Max Fee Limit : " + fees.get(i).getMaximumRangeAmount() + "/"
							+ amount + "]");
					continue;
				}

				if (fees.get(i).getFromMemberID() == -2) {
					List<ExternalMemberFields> emv = fromMember.getExternalMembers();
					logger.info("[From Member Parent Size : (" + emv.size() + ")]");

					for (int e = 0; e < emv.size(); e++) {
						logger.info("[From Member ID (" + i + ") : " + emv.get(i).getId() + "]");
					}

					Collections.sort(emv, new Comparator<ExternalMemberFields>() {
						public int compare(ExternalMemberFields o1, ExternalMemberFields o2) {
							int x1 = ((ExternalMemberFields) o1).getId();
							int x2 = ((ExternalMemberFields) o2).getId();
							return x1 - x2;
						}
					});

					if (emv.get(0).getParentID() == fromMember.getId()) {
						continue;
					}

					logger.info("[From Member ParentID Selected : " + emv.get(0).getParentID() + "]");
					fees.get(i).setFromMemberID(emv.get(0).getParentID());
				}

				if (fees.get(i).getFromMemberID() == -3) {
					List<ExternalMemberFields> emv = toMember.getExternalMembers();
					logger.info("[To Member Parent Size : (" + emv.size() + ")]");

					for (int e = 0; e < emv.size(); e++) {
						logger.info("[To Member ID (" + i + ") : " + emv.get(i).getId() + "]");
					}

					Collections.sort(emv, new Comparator<ExternalMemberFields>() {
						public int compare(ExternalMemberFields o1, ExternalMemberFields o2) {
							int x1 = ((ExternalMemberFields) o1).getId();
							int x2 = ((ExternalMemberFields) o2).getId();
							return x1 - x2;
						}
					});

					if (emv.get(0).getParentID() == fromMember.getId()) {
						continue;
					}

					logger.info("[To Member ParentID Selected : " + emv.get(0).getParentID() + "]");
					fees.get(i).setFromMemberID(emv.get(0).getParentID());
				}

				if (fees.get(i).getToMemberID() == -2) {
					List<ExternalMemberFields> emv = fromMember.getExternalMembers();
					logger.info("[From Member Parent Size : (" + emv.size() + ")]");

					for (int e = 0; e < emv.size(); e++) {
						logger.info("[From Member ID (" + i + ") : " + emv.get(i).getId() + "]");
					}

					Collections.sort(emv, new Comparator<ExternalMemberFields>() {
						public int compare(ExternalMemberFields o1, ExternalMemberFields o2) {
							int x1 = ((ExternalMemberFields) o1).getId();
							int x2 = ((ExternalMemberFields) o2).getId();
							return x1 - x2;
						}
					});

					if (emv.get(0).getParentID() == toMember.getId()) {
						continue;
					}

				}

				if (fees.get(i).getToMemberID() == -3) {
					List<ExternalMemberFields> emv = toMember.getExternalMembers();
					logger.info("[To Member Parent Size : (" + emv.size() + ")]");

					for (int e = 0; e < emv.size(); e++) {
						logger.info("[To Member ID (" + i + ") : " + emv.get(i).getId() + "]");
					}

					Collections.sort(emv, new Comparator<ExternalMemberFields>() {
						public int compare(ExternalMemberFields o1, ExternalMemberFields o2) {
							int x1 = ((ExternalMemberFields) o1).getId();
							int x2 = ((ExternalMemberFields) o2).getId();
							return x1 - x2;
						}
					});

					if (emv.get(0).getParentID() == toMember.getId()) {
						continue;
					}

				}

				/*
				 * Fee calculation
				 */

				if (fees.get(i).getFixedAmount().compareTo(BigDecimal.ZERO) == 1) {
					logger.info("[Fixed amount Fee : " + fees.get(i).getFixedAmount() + "]");
					fixed = fees.get(i).getFixedAmount();
				}

				if (fees.get(i).getPercentageValue().compareTo(BigDecimal.ZERO) == 1) {
					logger.info("[Percentage Fee : " + fees.get(i).getPercentageValue() + "]");
					percentage = amount.multiply(fees.get(i).getPercentageValue()).divide(new BigDecimal(100));
				}

				if (fixed.compareTo(percentage) == 0 || fixed.compareTo(percentage) == 1) {
					if (fees.get(i).isDeductAmount()) {
						logger.info("[Substract : " + fixed + " From total Amount From Source]");
						negativeFees.add(fixed.negate());
					} else {
						logger.info("[Add : " + fixed + " From total Amount From Source]");
						positiveFees.add(fixed);
					}
					fees.get(i).setFeeAmount(fixed);
				} else {
					if (fees.get(i).isDeductAmount()) {
						logger.info("[Substract : " + percentage + " From total Amount From Source]");
						negativeFees.add(percentage.negate());
					} else {
						logger.info("[Add : " + percentage + " From total Amount From Source]");
						positiveFees.add(percentage);
					}
					fees.get(i).setFeeAmount(percentage);
				}

				String feeTrxNo = Utils.GetDate("yyyyMMddkkmmssSSS") + Utils.GenerateTransactionNumber();
				fees.get(i).setTransactionNumber(feeTrxNo);
				finalFees.add(fees.get(i));

			}
		}

		logger.info("[Transaction Amount From Request : " + amount + "]");
		BigDecimal totalPositiveFee = amount;
		for (BigDecimal b : positiveFees) {
			totalPositiveFee = totalPositiveFee.add(b);
			logger.info("[Adding Positive fee(s) : " + b + " = " + totalPositiveFee + "]");
		}

		BigDecimal totalNegativeFee = amount;
		for (BigDecimal b : negativeFees) {
			totalNegativeFee = totalNegativeFee.add(b);
			logger.info("[Adding Negative fee(s) : " + b + " = " + totalNegativeFee + "]");
		}

		BigDecimal totalFee = totalPositiveFee.subtract(totalNegativeFee);

		if (totalNegativeFee.compareTo(BigDecimal.ZERO) < 0) {
			logger.warn("[NEGATIVE Total amount is NOT Allowed]");
			throw new TransactionException("NEGATIVE_AMOUNT");
		}

		FeeResult fr = new FeeResult();
		/*if(fees.get(0).isDeductAmount()) {
			fr.setFinalAmount(totalNegativeFee);
		}else {
			fr.setFinalAmount(totalPositiveFee);
		}*/
		fr.setFinalAmount(totalPositiveFee);
		fr.setTransactionAmount(totalNegativeFee);
		fr.setTotalFees(totalFee);
		fr.setListTotalFees(finalFees);
		logger.info(fr.toString());
		return fr;
	}

}

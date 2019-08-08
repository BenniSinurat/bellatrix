package org.bellatrix.process;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.bellatrix.data.Status;
import org.bellatrix.data.TransactionException;
import org.bellatrix.data.TransferTypeKey;
import org.bellatrix.data.TransferTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

@Component
public class TransferTypeValidation {

	@Autowired
	private BaseRepository baseRepository;
	@Autowired
	private HazelcastInstance instance;
	@Value("${global.cache.config.enabled}")
	private boolean useCache;
	private Logger logger = Logger.getLogger(TransferTypeValidation.class);

	public TransferTypes validateTransferType(Integer transferTypeID) throws TransactionException {
		TransferTypes transferType = baseRepository.getTransferTypeRepository().findTransferTypeByID(transferTypeID);
		if (transferType == null) {
			logger.info("[Invalid Transfer Type ID [" + transferTypeID + "]]");
			throw new TransactionException(String.valueOf(Status.INVALID_TRANSFER_TYPE));
		}
		return transferType;
	}

	public TransferTypes validateTransferType(Integer transferTypeID, Integer groupID, Integer fromMemberID)
			throws TransactionException {
		TransferTypes transferType = null;

		if (useCache) {
			IMap<String, TransferTypes> trxTypeMap = instance.getMap("TrxTypeMap");
			transferType = trxTypeMap.get(new TransferTypeKey(transferTypeID, groupID));
		} else {
			transferType = baseRepository.getTransferTypeRepository().findTransferTypeByGroupID(transferTypeID,
					groupID);
		}

		if (transferType == null) {
			logger.info("[Invalid Transfer Type ID [" + transferTypeID + "]]");
			throw new TransactionException(String.valueOf(Status.INVALID_TRANSFER_TYPE));
		}

		if (baseRepository.getTransferTypeRepository().validateBlockedTransferType(fromMemberID, transferTypeID)) {
			throw new TransactionException(String.valueOf(Status.TRANSACTION_BLOCKED));
		}

		return transferType;

	}

	public TransferTypes validateTransferType(Integer transferTypeID, Integer groupID, BigDecimal amount,
			Integer fromMemberID) throws TransactionException {
		TransferTypes transferType = null;

		if (useCache) {
			IMap<String, TransferTypes> trxTypeMap = instance.getMap("TrxTypeMap");
			transferType = trxTypeMap.get(new TransferTypeKey(transferTypeID, groupID));
		} else {
			transferType = baseRepository.getTransferTypeRepository().findTransferTypeByGroupID(transferTypeID,
					groupID);
		}

		if (transferType == null) {
			throw new TransactionException(String.valueOf(Status.INVALID_TRANSFER_TYPE));
		}

		if (baseRepository.getTransferTypeRepository().validateBlockedTransferType(fromMemberID, transferTypeID)) {
			throw new TransactionException(String.valueOf(Status.TRANSACTION_BLOCKED));
		}

		if (transferType.getMaxCount() != 0) {
			Integer maxCount = baseRepository.getTransferTypeRepository().countMaxUsageTransferTypes(transferTypeID,
					fromMemberID);
			if (maxCount >= transferType.getMaxCount()) {
				throw new TransactionException(String.valueOf(Status.TRANSACTION_LIMIT_REACHED));
			}
		}

		/*
		 * Validate TransferType Amount Limit
		 */
		if (transferType.getMinAmount().compareTo(BigDecimal.ZERO) != 0
				&& transferType.getMinAmount().compareTo(amount) == 1) {
			throw new TransactionException(String.valueOf(Status.TRANSACTION_AMOUNT_BELOW_LIMIT));
		}

		if (transferType.getMaxAmount().compareTo(BigDecimal.ZERO) != 0
				&& transferType.getMaxAmount().compareTo(amount) == -1) {
			throw new TransactionException(String.valueOf(Status.TRANSACTION_AMOUNT_ABOVE_LIMIT));
		}

		return transferType;
	}

	public TransferTypes validateTransferType(Integer transferTypeID, BigDecimal amount) throws TransactionException {
		TransferTypes transferType = null;

		if (useCache) {
			IMap<String, TransferTypes> trxTypeMap = instance.getMap("TrxTypeMap");
			transferType = trxTypeMap.get(new TransferTypeKey(transferTypeID));
		} else {
			transferType = baseRepository.getTransferTypeRepository().findTransferTypeByID(transferTypeID);
		}

		if (transferType == null) {
			throw new TransactionException(String.valueOf(Status.INVALID_TRANSFER_TYPE));
		}

		/*
		 * Validate TransferType Amount Limit
		 */
		if (transferType.getMinAmount().compareTo(BigDecimal.ZERO) != 0
				&& transferType.getMinAmount().compareTo(amount) == 1) {
			throw new TransactionException(String.valueOf(Status.TRANSACTION_AMOUNT_BELOW_LIMIT));
		}

		if (transferType.getMaxAmount().compareTo(BigDecimal.ZERO) != 0
				&& transferType.getMaxAmount().compareTo(amount) == -1) {
			throw new TransactionException(String.valueOf(Status.TRANSACTION_AMOUNT_ABOVE_LIMIT));
		}

		return transferType;
	}

}

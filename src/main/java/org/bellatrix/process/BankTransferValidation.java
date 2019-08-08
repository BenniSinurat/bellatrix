package org.bellatrix.process;

import org.bellatrix.data.BankTransfers;
import org.bellatrix.data.Status;
import org.bellatrix.data.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BankTransferValidation {

	@Autowired
	private BaseRepository baseRepository;

	public BankTransfers validateBank(Integer id, Integer groupID) throws TransactionException {
		BankTransfers bank = baseRepository.getInterBankRepository().loadBanksFromID(id, groupID);
		if (bank == null) {
			throw new TransactionException(String.valueOf(Status.BANK_NOT_FOUND));
		}
		return bank;
	}

	public String validateAccountNo(Integer bankID, Integer memberID, String accountNo) throws TransactionException {
		String name = baseRepository.getInterBankRepository().validateMemberBankAccountNo(bankID, memberID, accountNo);
		if (name == null) {
			throw new TransactionException(String.valueOf(Status.ACCOUNT_NOT_FOUND));
		}
		return name;
	}

}

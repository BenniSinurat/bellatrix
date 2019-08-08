package org.bellatrix.process;

import org.bellatrix.data.Status;
import org.bellatrix.data.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TraceNumberValidation {

	@Autowired
	private BaseRepository baseRepository;

	public boolean validateTraceNumber(Integer wsID, String traceNumber) throws TransactionException {
		if (!baseRepository.getTransferRepository().validateTransfersByTraceNumber(wsID, traceNumber)) {
			return true;
		}
		throw new TransactionException(String.valueOf(Status.DUPLICATE_TRANSACTION));

	}
}

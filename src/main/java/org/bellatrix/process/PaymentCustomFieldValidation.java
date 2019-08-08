package org.bellatrix.process;

import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import org.bellatrix.data.PaymentCustomFields;
import org.bellatrix.data.PaymentFields;
import org.bellatrix.data.Status;
import org.bellatrix.data.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentCustomFieldValidation {

	@Autowired
	private BaseRepository baseRepository;
	private Logger logger = Logger.getLogger(PaymentCustomFieldValidation.class);

	public void validatePaymentCustomField(Integer transferTypeID, List<PaymentFields> paymentField)
			throws TransactionException {
		List<PaymentCustomFields> fields = baseRepository.getCustomFieldRepository()
				.loadPaymentCustomFieldByTransferType(transferTypeID);
		List<String> cfRef = new LinkedList<String>();
		for (int i = 0; i < fields.size(); i++) {
			cfRef.add(fields.get(i).getInternalName());
		}
		List<String> cfReq = new LinkedList<String>();
		for (int i = 0; i < paymentField.size(); i++) {
			cfReq.add(paymentField.get(i).getInternalName());
		}
		cfReq.removeAll(cfRef);
		if (cfReq.size() != 0) {
			logger.info("[Invalid Payment Custom Field Count : (" + cfReq.size() + ")]");
			throw new TransactionException(String.valueOf(Status.INVALID_PARAMETER));
		}
	}

}

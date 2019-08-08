package org.bellatrix.process;

import org.apache.log4j.Logger;
import org.bellatrix.services.PaymentDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.MapEvent;

@Component
public class RequestPaymentNotificationListener implements EntryListener<String, PaymentDetails> {

	@Autowired
	private BaseRepository baseRepository;
	private Logger logger = Logger.getLogger(RequestPaymentNotificationListener.class);

	@Override
	public void entryAdded(EntryEvent<String, PaymentDetails> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void entryUpdated(EntryEvent<String, PaymentDetails> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void entryRemoved(EntryEvent<String, PaymentDetails> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void entryEvicted(EntryEvent<String, PaymentDetails> event) {
		logger.info("[Expired Request Payment Key : " + event.getKey() + ", TrxNo : "
				+ event.getOldValue().getTransactionNumber() + "]");
		baseRepository.getTransferRepository().deletePendingTransfers(event.getOldValue().getTransactionNumber());
	}

	@Override
	public void mapCleared(MapEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mapEvicted(MapEvent arg0) {
		// TODO Auto-generated method stub

	}

}

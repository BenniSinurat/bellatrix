package org.bellatrix.process;

import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bellatrix.data.RegisterVADoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.MapEvent;
import com.hazelcast.core.MapStore;

@Component
public class RegisterVAListener implements EntryListener<String, RegisterVADoc>, MapStore<String, RegisterVADoc> {
	@Autowired
	private BaseRepository baseRepository;
	private Logger logger = Logger.getLogger(RegisterVAListener.class);

	@Override
	public void entryAdded(EntryEvent<String, RegisterVADoc> event) {
		logger.info("[Create Request Billing VA Payment Code : " + event.getKey() + "]");
		baseRepository.getPersistenceRepository().create(event.getOldValue());
		baseRepository.getVirtualAccountRepository().registerBillingVA(event.getOldValue().getEvent().getEventID(), event.getOldValue());
	}

	@Override
	public void entryUpdated(EntryEvent<String, RegisterVADoc> arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void entryRemoved(EntryEvent<String, RegisterVADoc> event) {
		logger.info("[Delete Request Billing VA Payment Code : " + event.getKey() + "]");
		baseRepository.getPersistenceRepository().delete(new Query(Criteria.where("_id").is(event.getKey())),
				RegisterVADoc.class);
		logger.info("[Update Status Deleted Request Billing VA Payment Code : " + event.getKey() + "]");
		baseRepository.getVirtualAccountRepository().deleteVA(event.getKey());
	}

	@Override
	public void entryEvicted(EntryEvent<String, RegisterVADoc> event) {
		logger.info("[Expired Request Billing VA Key : " + event.getKey() + ", Payment Code : "
				+ event.getOldValue().getId() + "]");
		baseRepository.getVirtualAccountRepository().updateStatusBilling(event.getOldValue().getId());;
	}

	@Override
	public void mapCleared(MapEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mapEvicted(MapEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public RegisterVADoc load(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, RegisterVADoc> loadAll(Collection<String> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<String> loadAllKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(String key) {
		logger.info("[Delete Request Billing VA Payment Code : " + key + "]");
		baseRepository.getPersistenceRepository().delete(new Query(Criteria.where("_id").is(key)),
				RegisterVADoc.class);
		logger.info("[Update Status Deleted Request Billing VA Payment Code : " + key + "]");
		baseRepository.getVirtualAccountRepository().deleteVA(key);
	}

	@Override
	public void deleteAll(Collection<String> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void store(String key, RegisterVADoc arg1) {
		logger.info("[Create Request Billing VA Payment Code : " + key + "]");
		baseRepository.getPersistenceRepository().create(arg1);
	}

	@Override
	public void storeAll(Map<String, RegisterVADoc> arg0) {
		// TODO Auto-generated method stub
		
	}

}

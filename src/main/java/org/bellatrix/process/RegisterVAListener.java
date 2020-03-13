package org.bellatrix.process;

import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bellatrix.data.RegisterVADoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import com.hazelcast.core.MapStore;

@Component
public class RegisterVAListener implements MapStore<String, RegisterVADoc> {
	@Autowired
	private BaseRepository baseRepository;
	private Logger logger = Logger.getLogger(RegisterVAListener.class);

	@Override
	public RegisterVADoc load(String key) {
		try {
			RegisterVADoc rv = baseRepository.getPersistenceRepository()
					.retrieve(new Query(Criteria.where("_id").is(key)), RegisterVADoc.class);
			return rv;
		} catch (NullPointerException e) {
			return null;
		}
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
		baseRepository.getPersistenceRepository().delete(new Query(Criteria.where("_id").is(key)), RegisterVADoc.class);
		logger.info("[Update Status Deleted Request Billing VA Payment Code : " + key + "]");
		baseRepository.getVirtualAccountRepository().deleteVA(key);
	}

	@Override
	public void deleteAll(Collection<String> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void store(String key, RegisterVADoc va) {
		logger.info("[Create Request Billing VA Payment Code : " + key + "]");
		baseRepository.getPersistenceRepository().create(va);
		baseRepository.getVirtualAccountRepository().registerBillingVA(va.getEvent().getEventID(), va);
	}

	@Override
	public void storeAll(Map<String, RegisterVADoc> arg0) {
		// TODO Auto-generated method stub

	}

}

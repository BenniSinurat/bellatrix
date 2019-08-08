package org.bellatrix.process;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

import org.bellatrix.data.GeneratePaymentDoc;
import org.bellatrix.services.GeneratePaymentTicketRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.hazelcast.core.MapStore;

public class GenerateTicketCacheImpl implements MapStore<String, GeneratePaymentTicketRequest> {

	@Autowired
	private BaseRepository baseRepository;
	private Long expiredAtMinute;

	@Override
	public GeneratePaymentTicketRequest load(String key) {
		try {
			GeneratePaymentDoc doc = baseRepository.getPersistenceRepository()
					.retrieve(new Query(Criteria.where("_id").is(key)), GeneratePaymentDoc.class);
			return doc.getContent();
		} catch (NullPointerException ex) {
			return null;
		}
	}

	@Override
	public Map<String, GeneratePaymentTicketRequest> loadAll(Collection<String> arg0) {
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
		baseRepository.getPersistenceRepository().delete(new Query(Criteria.where("_id").is(key)),
				GeneratePaymentDoc.class);
	}

	@Override
	public void deleteAll(Collection<String> arg0) {
		baseRepository.getPersistenceRepository().flush("GenerateTicket");
	}

	@Override
	public void store(String key, GeneratePaymentTicketRequest req) {
		GeneratePaymentDoc doc = new GeneratePaymentDoc();
		LocalDateTime timePoint = LocalDateTime.now();
		timePoint = timePoint.plusMinutes(getExpiredAtMinute());
		doc.setExpiredAt(timePoint);
		doc.setContent(req);
		doc.setId(key);
		baseRepository.getPersistenceRepository().create(doc);
	}

	@Override
	public void storeAll(Map<String, GeneratePaymentTicketRequest> arg0) {
		// TODO Auto-generated method stub

	}

	public Long getExpiredAtMinute() {
		return expiredAtMinute;
	}

	public void setExpiredAtMinute(Long expiredAtMinute) {
		this.expiredAtMinute = expiredAtMinute;
	}

}

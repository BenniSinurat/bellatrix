package org.bellatrix.process;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import org.bellatrix.data.GenerateTopupDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.hazelcast.core.MapStore;

public class GenerateTopupCacheImpl implements MapStore<String, GenerateTopupDoc> {

	@Autowired
	private BaseRepository baseRepository;
	private Long expiredAtMinute;

	@Override
	public GenerateTopupDoc load(String key) {
		GenerateTopupDoc doc = baseRepository.getPersistenceRepository()
				.retrieve(new Query(Criteria.where("_id").is(key)), GenerateTopupDoc.class);
		return doc;
	}

	@Override
	public Map<String, GenerateTopupDoc> loadAll(Collection<String> arg0) {
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
				GenerateTopupDoc.class);
	}

	@Override
	public void deleteAll(Collection<String> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void store(String arg0, GenerateTopupDoc doc) {
		LocalDateTime timePoint = LocalDateTime.now();
		timePoint = timePoint.plusMinutes(expiredAtMinute);
		doc.setExpiredAt(timePoint);
		baseRepository.getPersistenceRepository().create(doc);
	}

	@Override
	public void storeAll(Map<String, GenerateTopupDoc> arg0) {
		// TODO Auto-generated method stub

	}

	public Long getExpiredAtMinute() {
		return expiredAtMinute;
	}

	public void setExpiredAtMinute(Long expiredAtMinute) {
		this.expiredAtMinute = expiredAtMinute;
	}

}

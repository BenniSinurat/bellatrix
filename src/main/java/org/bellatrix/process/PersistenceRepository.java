package org.bellatrix.process;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public class PersistenceRepository {

	@Autowired
	private MongoTemplate mongoTemplate;

	public <T> Long count(Query query, Class<T> entityClass) {
		return getMongoTemplate().count(query, entityClass);
	}

	public <T> void create(T entity) {
		getMongoTemplate().insert(entity);
	}

	public <T> T retrieve(Query query, Class<T> entityClass) {
		return getMongoTemplate().findOne(query, entityClass);
	}

	public <T> List<T> loadAll(Query query, Class<T> entityClass) {
		return getMongoTemplate().find(query, entityClass);
	}

	public <T> void update(T req) {
		getMongoTemplate().save(req);
	}

	public <T> void delete(Query query, Class<T> entityClass) {
		getMongoTemplate().remove(query, entityClass);
	}

	public <T> void flush(String collectionName) {
		getMongoTemplate().dropCollection(collectionName);
	}

	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

}

package org.bellatrix.process;

import org.bellatrix.data.Status;
import org.bellatrix.data.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

@Component
public class GroupPermissionValidation {

	@Autowired
	private HazelcastInstance instance;
	@Value("${global.cache.config.enabled}")
	private boolean useCache;
	@Autowired
	private BaseRepository baseRepository;

	public void validateGroupPermission(Integer wsID, Integer groupID) throws TransactionException {
		boolean authenticate = false;

		if (useCache) {
			IMap<String, Boolean> gpMap = instance.getMap("GroupPermissionMap");
			String gpID = String.valueOf(wsID) + ":" + String.valueOf(groupID);
			authenticate = gpMap.get(gpID);
		} else {
			authenticate = baseRepository.getWebServicesRepository().validateGroupAccessToWebService(wsID, groupID);
		}
	
		if (!authenticate) {
			throw new TransactionException(String.valueOf(Status.SERVICE_NOT_ALLOWED));
		}
	}

}

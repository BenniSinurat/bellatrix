package org.bellatrix.process;

import java.util.Collection;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import com.hazelcast.core.MapStore;

public class GroupPermissionCacheImpl implements MapStore<String, Boolean> {

	@Autowired
	private BaseRepository baseRepository;

	@Override
	public Boolean load(String arg0) {
		String split[] = arg0.split(":");
		Integer wsID = Integer.valueOf(split[0]);
		Integer groupID = Integer.valueOf(split[1]);
		return baseRepository.getWebServicesRepository().validateGroupAccessToWebService(wsID, groupID);
	}

	@Override
	public Map<String, Boolean> loadAll(Collection<String> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<String> loadAllKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll(Collection<String> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void store(String arg0, Boolean arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void storeAll(Map<String, Boolean> arg0) {
		// TODO Auto-generated method stub

	}

}

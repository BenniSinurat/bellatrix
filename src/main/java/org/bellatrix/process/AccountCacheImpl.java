package org.bellatrix.process;

import java.util.Collection;
import java.util.Map;

import org.bellatrix.data.Accounts;
import org.springframework.beans.factory.annotation.Autowired;
import com.hazelcast.core.MapStore;

public class AccountCacheImpl implements MapStore<String, Accounts> {

	@Autowired
	private BaseRepository baseRepository;

	@Override
	public Accounts load(String arg0) {
		String split[] = arg0.split(":");
		return baseRepository.getAccountRepository().loadAccountsByID(Integer.valueOf(split[0]),
				Integer.valueOf(split[1]));
	}

	@Override
	public Map<String, Accounts> loadAll(Collection<String> arg0) {
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
	public void store(String arg0, Accounts arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void storeAll(Map<String, Accounts> arg0) {
		// TODO Auto-generated method stub

	}

}

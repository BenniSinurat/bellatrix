package org.bellatrix.process;

import java.util.Collection;
import java.util.Map;
import org.bellatrix.data.WebServices;
import org.springframework.beans.factory.annotation.Autowired;

import com.hazelcast.core.MapStore;

public class WebserviceCacheImpl implements MapStore<String, WebServices> {

	@Autowired
	private JWTAuthCodecFilter authFilter;

	@Override
	public WebServices load(String arg0) {
		return authFilter.Authenticate(arg0);
	}

	@Override
	public Map<String, WebServices> loadAll(Collection<String> arg0) {
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
	public void store(String arg0, WebServices arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void storeAll(Map<String, WebServices> arg0) {
		// TODO Auto-generated method stub

	}

}

package org.bellatrix.process;

import org.bellatrix.data.Status;
import org.bellatrix.data.TransactionException;
import org.bellatrix.data.WebServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

@Component
public class WebserviceValidation {
	@Autowired
	private HazelcastInstance instance;
	@Value("${global.cache.config.enabled}")
	private boolean useCache;
	@Autowired
	private JWTAuthCodecFilter authFilter;

	public WebServices validateWebservice(String token) throws TransactionException {
		WebServices ws = null;
		if (useCache) {
			IMap<String, WebServices> wsMap = instance.getMap("WebServiceMap");
			ws = wsMap.get(token);
		} else {
			ws = authFilter.Authenticate(token);
		}
		if (ws == null) {
			throw new TransactionException(String.valueOf(Status.UNAUTHORIZED_ACCESS));
		}
		return ws;
	}

}

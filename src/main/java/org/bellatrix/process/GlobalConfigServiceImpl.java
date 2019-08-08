package org.bellatrix.process;

import java.util.List;
import javax.xml.ws.Holder;
import org.bellatrix.data.GlobalConfigs;
import org.bellatrix.data.Header;
import org.bellatrix.data.Status;
import org.bellatrix.data.StatusBuilder;
import org.bellatrix.data.TransactionException;
import org.bellatrix.services.GlobalConfig;
import org.bellatrix.services.GlobalConfigResponse;
import org.springframework.beans.factory.annotation.Autowired;


public class GlobalConfigServiceImpl implements GlobalConfig {

	@Autowired
	private WebserviceValidation webserviceValidation;
	@Autowired
	private BaseRepository baseRepository;

	@Override
	public GlobalConfigResponse loadGlobalConfig(Holder<Header> headerParam) throws TransactionException {
		GlobalConfigResponse gcr = new GlobalConfigResponse();
		webserviceValidation.validateWebservice(headerParam.value.getToken());
		List<GlobalConfigs> gc = baseRepository.getGlobalConfigRepository().loadGlobalConfig();
		gcr.setGlobalConfig(gc);
		gcr.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
		return gcr;
	}

}

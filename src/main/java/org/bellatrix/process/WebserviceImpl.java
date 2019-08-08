package org.bellatrix.process;

import org.bellatrix.auth.JWTProcessor;
import org.bellatrix.data.WebServices;
import org.bellatrix.services.AuthRequest;
import org.bellatrix.services.AuthResponse;
import org.bellatrix.services.LoadPermissionByWebServicesRequest;
import org.bellatrix.services.LoadPermissionByWebServicesResponse;
import org.bellatrix.services.LoadWSByIDRequest;
import org.bellatrix.services.LoadWSByIDResponse;
import org.bellatrix.services.LoadWSRequest;
import org.bellatrix.services.LoadWSResponse;
import org.bellatrix.services.Webservice;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import javax.xml.ws.Holder;
import org.bellatrix.data.Header;
import org.bellatrix.data.Status;
import org.bellatrix.data.StatusBuilder;
import org.bellatrix.data.TransactionException;
import org.bellatrix.data.WebServicePermission;

public class WebserviceImpl implements Webservice {

	@Autowired
	private BaseRepository baseRepository;
	@Autowired
	private JWTProcessor jwt;
	@Autowired
	private WebserviceValidation webserviceValidation;

	@Override
	public AuthResponse getWebServicesToken(AuthRequest req) {
		AuthResponse ar = new AuthResponse();
		WebServices ws = baseRepository.getWebServicesRepository().validateWebService(req.getUsername(),
				req.getPassword());
		if (ws != null) {
			String token = jwt.createJWTHMAC256(ws.getUsername(), ws.getHash());
			ar.setToken(token);
		}
		return ar;
	}

	@Override
	public LoadWSResponse loadAllWebService(Holder<Header> headerParam, LoadWSRequest req) {
		LoadWSResponse loadWebService = new LoadWSResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			List<WebServices> webservices = baseRepository.getWebServicesRepository()
					.findAllWebServices(req.getCurrentPage(), req.getPageSize());
			Integer totalRecords = baseRepository.getWebServicesRepository().countTotalWebServices();
			loadWebService.setTotalRecords(totalRecords);
			loadWebService.setWebServices(webservices);
			loadWebService.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return loadWebService;
		} catch (TransactionException e) {
			loadWebService.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return loadWebService;
		}
	}

	@Override
	public LoadWSByIDResponse loadWebServiceByID(Holder<Header> headerParam, LoadWSByIDRequest req) {
		LoadWSByIDResponse loadwsbyidres = new LoadWSByIDResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			WebServices ws = baseRepository.getWebServicesRepository().findWebServicesByID(req.getId());
			loadwsbyidres.setWebServices(ws);
			loadwsbyidres.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return loadwsbyidres;
		} catch (Exception e) {
			loadwsbyidres.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return loadwsbyidres;
		}
	}

	@Override
	public void createWebService(Holder<Header> headerParam, WebServices req) throws Exception {
		webserviceValidation.validateWebservice(headerParam.value.getToken());
		baseRepository.getWebServicesRepository().createWebService(req);
	}
	
	@Override
	public void createWebServicePermission(Holder<Header> headerParam, LoadPermissionByWebServicesRequest req) throws Exception{
		webserviceValidation.validateWebservice(headerParam.value.getToken());
		baseRepository.getWebServicesRepository().createWebServicePermission(req.getWebServiceID(), req.getGroupID());
	}
	
	@Override
	public void updateWebServicePermission(Holder<Header> headerParam, LoadPermissionByWebServicesRequest req) throws Exception{
		webserviceValidation.validateWebservice(headerParam.value.getToken());
		baseRepository.getWebServicesRepository().updatePermissionWebservice(req);
	}
	
	@Override
	public void deleteWebServicePermission(Holder<Header> headerParam, LoadWSByIDRequest req) throws Exception{
		webserviceValidation.validateWebservice(headerParam.value.getToken());
		baseRepository.getWebServicesRepository().deletePermissionWebservice(req);
	}
	
	@Override
	public LoadPermissionByWebServicesResponse loadWebServicePermission(Holder<Header> headerParam, LoadPermissionByWebServicesRequest req) {
		LoadPermissionByWebServicesResponse lpw = new LoadPermissionByWebServicesResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			List<WebServicePermission> webservicePermission = baseRepository.getWebServicesRepository().listPermissionByWebservice(req.getWebServiceID());
			Integer totalRecords = baseRepository.getWebServicesRepository().countTotalWebServicePermission(req.getWebServiceID());
			lpw.setTotalRecords(totalRecords);
			lpw.setWebServicePermissions(webservicePermission);
			lpw.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return lpw;
		}catch (Exception e) {
			lpw.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return lpw;
		}
	}
}

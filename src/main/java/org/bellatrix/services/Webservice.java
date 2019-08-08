package org.bellatrix.services;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Holder;

import org.bellatrix.data.Header;
import org.bellatrix.data.WebServices;

@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.BARE)
@WebService
public interface Webservice {

	@WebMethod(action = "getWebServicesToken")
	public AuthResponse getWebServicesToken(@WebParam AuthRequest req);

	@WebMethod(action = "loadAllWebServices")
	public LoadWSResponse loadAllWebService(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadWSRequest req);

	@WebMethod(action = "loadWebServiceByID")
	public LoadWSByIDResponse loadWebServiceByID(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam, @WebParam LoadWSByIDRequest req);

	@WebMethod(action = "createWebService")
	public void createWebService(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam WebServices req) throws Exception;

	@WebMethod(action = "createWebServicePermission")
	public void createWebServicePermission(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadPermissionByWebServicesRequest req) throws Exception;

	@WebMethod(action = "updateWebServicePermission")
	public void updateWebServicePermission(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadPermissionByWebServicesRequest req) throws Exception;

	@WebMethod(action = "deleteWebServicePermission")
	public void deleteWebServicePermission(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadWSByIDRequest req) throws Exception;

	@WebMethod(action = "listWebServicePermission")
	public LoadPermissionByWebServicesResponse loadWebServicePermission(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadPermissionByWebServicesRequest req);

}

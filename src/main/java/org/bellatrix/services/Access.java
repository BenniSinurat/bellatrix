package org.bellatrix.services;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Holder;

import org.bellatrix.data.Header;

@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.BARE)
@WebService
public interface Access {

	@WebMethod(action = "createCredential")
	public void createCredential(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam CreateCredentialRequest req) throws Exception;

	@WebMethod(action = "validateCredential")
	public ValidateCredentialResponse validateCredential(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam ValidateCredentialRequest req);

	@WebMethod(action = "getCredential")
	public CredentialResponse getCredential(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam CredentialRequest req) throws Exception;

	@WebMethod(action = "credentialStatus")
	public CredentialStatusResponse credentialStatus(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam CredentialStatusRequest req);

	@WebMethod(action = "resetCredential")
	public ResetCredentialResponse resetCredential(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam ResetCredentialRequest req);

	@WebMethod(action = "unblockCredential")
	public void unblockCredential(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam UnblockCredentialRequest req) throws Exception;

	@WebMethod(action = "changeCredential")
	public void changeCredential(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam ChangeCredentialRequest req) throws Exception;

	@WebMethod(action = "loadAccessType")
	public LoadAccessTypeResponse loadAccessType(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadAccessTypeRequest req) throws Exception;

	@WebMethod(action = "updateAccessType")
	public void updateAccessType(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam AccessTypeRequest req) throws Exception;

	@WebMethod(action = "createAccessType")
	public void createAccessType(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam AccessTypeRequest req) throws Exception;

}

package org.bellatrix.services;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Holder;

import org.bellatrix.data.Header;
import org.bellatrix.data.TransactionException;

@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.BARE)
@WebService
public interface GlobalConfig {

	@WebMethod(action = "loadGlobalConfig")
	public GlobalConfigResponse loadGlobalConfig(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam) throws TransactionException;

}
package org.bellatrix.services;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Holder;
import org.bellatrix.data.Header;

@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.BARE)
@WebService
public interface CustomField {

	@WebMethod(action = "loadMemberCustomFieldsByID")
	public LoadMemberCustomFieldsResponse loadMemberCustomFieldsByID(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadCustomFieldsByIDRequest req);

	@WebMethod(action = "loadMemberCustomFieldsByGroup")
	public LoadMemberCustomFieldsResponse loadMemberCustomFieldsByGroup(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadCustomFieldsByGroupIDRequest req);

	@WebMethod(action = "loadMemberCustomFieldsByInternalName")
	public LoadMemberCustomFieldsResponse loadMemberCustomFieldsByInternalName(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadCustomFieldsByInternalNameRequest req);

	@WebMethod(action = "createMemberCustomFields")
	public void createMemberCustomFields(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam CreateMemberCustomFieldsRequest req) throws Exception;

	@WebMethod(action = "createPaymentCustomFields")
	public void createPaymentCustomFields(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam CreatePaymentCustomFieldsRequest req) throws Exception;

}

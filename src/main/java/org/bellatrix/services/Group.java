package org.bellatrix.services;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Holder;

import org.bellatrix.data.Header;

@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.BARE)
@WebService
public interface Group {

	@WebMethod(action = "loadAllGroups")
	public LoadGroupsResponse loadAllGroups(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadGroupsRequest req);

	@WebMethod(action = "loadGroupsByID")
	public LoadGroupsByIDResponse loadGroupsByID(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadGroupsByIDRequest req);

	@WebMethod(action = "createGroups")
	public void createGroups(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam CreateGroupsRequest req) throws Exception;

}

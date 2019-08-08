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
public interface TransferType {
	
	@WebMethod(action = "loadAllTransferTypes")
	public LoadTransferTypesResponse loadAllTransferTypes(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadTransferTypesRequest req) throws TransactionException;
	
	@WebMethod(action = "loadTransferTypesByID")
	public LoadTransferTypesByIDResponse loadTransferTypesByID(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadTransferTypesByIDRequest req);

	@WebMethod(action = "loadTransferTypesByAccountID")
	public LoadTransferTypesByAccountIDResponse loadTransferTypesByAccountID(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadTransferTypesByAccountIDRequest req);

	@WebMethod(action = "loadTransferTypesByUsername")
	public LoadTransferTypesByUsernameResponse loadTransferTypesByUsername(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadTransferTypesByUsernameRequest req);

	@WebMethod(action = "createTransferTypes")
	public void createTransferTypes(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam TransferTypeRequest req) throws TransactionException;

	@WebMethod(action = "updateTransferTypes")
	public void updateTransferTypes(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam TransferTypeRequest req) throws TransactionException;
	
	@WebMethod(action = "loadPermissionsByTransferType")
	public LoadPermissionByTransferTypesResponse loadPermissionsByTransferType(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadPermissionByTransferTypesRequest req);

	@WebMethod(action = "createTransferTypePermissions")
	public void createTransferTypePermissions(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam TransferTypePermissionRequest req) throws TransactionException;
	
	@WebMethod(action = "updateTransferTypePermission")
	public void updateTransferTypePermission(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadPermissionByTransferTypesRequest req) throws TransactionException;

	@WebMethod(action = "deleteTransferTypePermissions")
	public void deleteTransferTypePermissions(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadPermissionByTransferTypesRequest req) throws TransactionException;
	
	@WebMethod(action = "loadFeesByTransferType")
	public LoadFeesByTransferTypeResponse loadFeesByTransferType(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadFeesByTransferTypeRequest req);

	@WebMethod(action = "createFees")
	public void createFees(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam FeeRequest req) throws TransactionException;

	@WebMethod(action = "updateFees")
	public void updateFees(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam FeeRequest req) throws TransactionException;
	
	@WebMethod(action = "deleteFees")
	public void deleteFees(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam FeeRequest req) throws TransactionException;

}
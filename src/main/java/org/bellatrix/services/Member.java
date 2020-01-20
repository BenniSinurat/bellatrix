package org.bellatrix.services;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Holder;

import org.bellatrix.data.Header;

@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.BARE)
@WebService
public interface Member {

	@WebMethod(action = "loadAllMembers")
	public LoadMembersResponse loadAllMembers(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadMembersRequest req);

	@WebMethod(action = "loadMembersByID")
	public LoadMembersResponse loadMembersByID(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadMembersByIDRequest req);

	@WebMethod(action = "loadMembersByUsername")
	public LoadMembersResponse loadMembersByUsername(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadMembersByUsernameRequest req);

	@WebMethod(action = "loadMembersByExternalID")
	public LoadMembersResponse loadMembersByExternalID(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadMembersByExternalIDRequest req);

	@WebMethod(action = "loadMembersByGroupID")
	public LoadMembersResponse loadMembersByGroupID(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadMembersByGroupIDRequest req);

	@WebMethod(action = "registerMembers")
	public void registerMembers(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam RegisterMemberRequest req) throws Exception;

	@WebMethod(action = "updateMembers")
	public void updateMembers(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam UpdateMemberRequest req) throws Exception;

	@WebMethod(action = "registerExternalMembers")
	public void registerExternalMembers(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam SubscribeMemberRequest req) throws Exception;

	@WebMethod(action = "unregisterExternalMembers")
	public void unregisterExternalMembers(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam SubscribeMemberRequest req) throws Exception;

	@WebMethod(action = "membersKYCRequest")
	public void membersKYCRequest(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam MemberKYCRequest req) throws Exception;

	@WebMethod(action = "confirmKYCRequest")
	public ConfirmKYCResponse confirmKYCRequest(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam, @WebParam ConfirmKYCRequest req)
			throws Exception;

	@WebMethod(action = "loadKYCRequest")
	public LoadKYCResponse loadKYCRequest(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadKYCRequest req) throws Exception;

	@WebMethod(action = "validateKYCRequest")
	public ValidateKYCResponse validateKYCRequest(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam, @WebParam ValidateKYCRequest req)
			throws Exception;

	@WebMethod(action = "registerMerchants")
	public void registerMerchants(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam RegisterMerchantRequest req) throws Exception;

	@WebMethod(action = "loadMerchantByUsername")
	public LoadMerchantByUsernameResponse loadMerchantsByUsername(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadMerchantByUsernameRequest req);

	@WebMethod(action = "updateMerchants")
	public void updateMerchants(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam UpdateMerchantRequest req) throws Exception;

	@WebMethod(action = "loadMerchantRequest")
	public LoadMerchantResponse loadMerchantRequest(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadMerchantRequest req) throws Exception;

	@WebMethod(action = "confirmMerchantRequest")
	public ConfirmMerchantResponse confirmMerchantRequest(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam, @WebParam ConfirmMerchantRequest req)
			throws Exception;
	
	@WebMethod(action = "loadMerchantCategory")
	public LoadMerchantCategoryResponse loadMerchantCategory(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadMerchantCategoryRequest req) throws Exception;
	
	@WebMethod(action = "loadMerchantSubCategory")
	public LoadMerchantSubCategoryResponse loadMerchantSubCategory(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadMerchantSubCategoryRequest req) throws Exception;
	
	@WebMethod(action = "loadMerchantBusinessScale")
	public LoadMerchantBusinessScaleResponse loadMerchantBusinessScale(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadMerchantBusinessScaleRequest req) throws Exception;
}

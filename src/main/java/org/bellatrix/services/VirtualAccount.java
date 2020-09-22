package org.bellatrix.services;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Holder;
import org.bellatrix.data.Header;
import org.bellatrix.data.ReportBillingRequest;
import org.bellatrix.data.ReportBillingResponse;

@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.BARE)
@WebService
public interface VirtualAccount {

	@WebMethod(action = "loadBankVA")
	public VABankResponse loadBankVA(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam VABankRequest req);

	@WebMethod(action = "registerBankVA")
	public VARegisterBankResponse registerBankVA(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam VARegisterBankRequest req);

	@WebMethod(action = "registerVA")
	public VARegisterResponse registerVA(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam VARegisterRequest req);

	@WebMethod(action = "updateVA")
	public VAUpdateResponse updateVA(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam VAUpdateRequest req);

	@WebMethod(action = "deleteVA")
	public void deleteVA(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam VADeleteRequest req) throws Exception;

	@WebMethod(action = "inquiryVA")
	public VAInquiryResponse inquiryVA(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam VAInquiryRequest req);

	@WebMethod(action = "paymentVA")
	public VAPaymentResponse paymentVA(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam VAPaymentRequest req);

	@WebMethod(action = "loadVAByMember")
	public LoadVAByMemberResponse loadVAByMember(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadVAByMemberRequest req);

	@WebMethod(action = "loadVAByID")
	public LoadVAByIDResponse loadVAByID(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadVAByIDRequest req);

	@WebMethod(action = "loadVAEvent")
	public LoadVAEventResponse loadVAEvent(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadVAEventRequest req);

	@WebMethod(action = "loadVAEventByID")
	public LoadVAEventResponse loadVAEventByID(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadVAEventRequest req);

	@WebMethod(action = "createVAEvent")
	public CreateVAEventResponse createVAEvent(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam CreateVAEventRequest req) throws Exception;

	@WebMethod(action = "deleteVAEvent")
	public void deleteVAEvent(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam DeleteVAEventRequest req) throws Exception;

	@WebMethod(action = "updateStatusVA")
	void updateBillingStatus(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam UpdateBillingStatusRequest req) throws Exception;

	@WebMethod(action = "listBankVA")
	public VABankResponse listBankVA(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam VABankRequest req);

	@WebMethod(action = "reportBilling")
	public ReportBillingResponse reportBilling(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam ReportBillingRequest req) throws Exception;

	@WebMethod(action = "loadVAByEvent")
	public LoadVAByEventResponse loadVAByEvent(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadVAByEventRequest req);

	@WebMethod(action = "loadVAMemberByStatus")
	public LoadVAStatusByMemberResponse loadVAMemberByStatus(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadVAStatusByMemberRequest req);

	@WebMethod(action = "createEventStatus")
	public CreateEventStatusResponse createEventStatus(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam CreateEventStatusRequest req);
}
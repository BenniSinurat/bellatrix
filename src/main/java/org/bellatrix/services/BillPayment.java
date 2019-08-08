package org.bellatrix.services;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Holder;

import org.bellatrix.data.Header;

@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.BARE)
@WebService

public interface BillPayment {

	@WebMethod(action = "loadBillerList")
	public BillerListResponse loadBillerList(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam)
			throws Exception;

	@WebMethod(action = "loadBillersFromUsername")
	public BillerListResponse loadBillersFromUsername(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam BillerDetailsRequest req) throws Exception;

	@WebMethod(action = "loadBillersFromID")
	public BillerListResponse loadBillersFromID(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam BillerDetailsRequest req) throws Exception;

	@WebMethod(action = "registerBiller")
	public void registerBiller(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam BillerRegisterRequest req) throws Exception;
	
	@WebMethod(action = "loadPaymentChannel")
	public PaymentChannelResponse loadPaymentChannel(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam)
			throws Exception;


	@WebMethod(action = "inquiryBilling")
	public BillInquiryResponse inquiryBilling(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam BillInquiryRequest req) throws Exception;

	@WebMethod(action = "paymentBilling")
	public BillPaymentResponse paymentBilling(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam BillPaymentRequest req) throws Exception;

}

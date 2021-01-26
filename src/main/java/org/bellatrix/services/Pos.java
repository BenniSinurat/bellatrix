package org.bellatrix.services;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Holder;

import org.bellatrix.data.Header;

@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.BARE)
@WebService
public interface Pos {

	@WebMethod(action = "registerPOS")
	public void registerPOS(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam RegisterPOSRequest req) throws Exception;

	@WebMethod(action = "updatePOS")
	public void updatePOS(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam UpdatePOSRequest req) throws Exception;

	@WebMethod(action = "deletePOS")
	public void deletePOS(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam DeletePOSRequest req) throws Exception;

	@WebMethod(action = "loadTerminalByUsername")
	public TerminalInquiryResponse loadTerminalByUsername(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadTerminalByUsernameRequest req) throws Exception;

	@WebMethod(action = "loadTerminalByID")
	public TerminalInquiryResponse loadTerminalByID(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadTerminalByIDRequest req) throws Exception;

	@WebMethod(action = "posGenerateInvoice")
	public PosCreateInvoiceResponse posGenerateInvoice(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam PosCreateInvoiceRequest req) throws Exception;

	@WebMethod(action = "posInquiry")
	public PosInquiryResponse posInquiry(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam PosInquiryRequest req) throws Exception;

	@WebMethod(action = "posPayment")
	public PosPaymentResponse posPayment(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam PosPaymentRequest req) throws Exception;

	@WebMethod(action = "doPaymentQRIS")
	public PaymentResponse doPaymentQRIS(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam PaymentRequest req);

}

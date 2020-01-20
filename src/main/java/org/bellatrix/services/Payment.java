package org.bellatrix.services;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Holder;
import org.bellatrix.data.Header;

@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.BARE)
@WebService
public interface Payment {

	@WebMethod(action = "doPayment")
	public PaymentResponse doPayment(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam PaymentRequest req);

	@WebMethod(action = "doInquiry")
	public InquiryResponse doInquiry(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam InquiryRequest req);

	@WebMethod(action = "requestPaymentConfirmation")
	public RequestPaymentConfirmationResponse requestPaymentConfirmation(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam, @WebParam PaymentRequest req);

	@WebMethod(action = "confirmPayment")
	public PaymentResponse confirmPayment(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam ConfirmPaymentRequest req);

	@WebMethod(action = "generatePaymentTicket")
	public GeneratePaymentTicketResponse generatePaymentTicket(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam GeneratePaymentTicketRequest req);

	@WebMethod(action = "validatePaymentTicket")
	public ValidatePaymentTicketResponse validatePaymentTicket(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam ValidatePaymentTicketRequest req);

	@WebMethod(action = "confirmPaymentTicket")
	public PaymentResponse confirmPaymentTicket(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam ConfirmPaymentTicketRequest req);

	@WebMethod(action = "agentCashoutConfirmation")
	public AgentCashoutResponse agentCashoutConfirmation(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam AgentCashoutRequest req);

	@WebMethod(action = "confirmAgentCashout")
	public ConfirmAgentCashoutResponse confirmAgentCashout(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam ConfirmAgentCashoutRequest req);

	@WebMethod(action = "transactionStatus")
	public TransactionStatusResponse transactionStatus(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam TransactionStatusRequest req);

	@WebMethod(action = "reversePayment")
	public ReversalResponse reversePayment(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam ReversalRequest req);
	
	@WebMethod(action = "merchantRequestPayment")
	public RequestPaymentConfirmationResponse merchantRequestPayment(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam, @WebParam PaymentRequest req);
	
	@WebMethod(action = "merchantConfirmPayment")
	public PaymentResponse merchantConfirmPayment(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam ConfirmPaymentRequest req);

}

package org.bellatrix.services;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Holder;

import org.bellatrix.data.Header;

@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.BARE)
@WebService
public interface InterBank {

	@WebMethod(action = "topupRequest")
	public TopupResponse topupRequest(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam TopupRequest req) throws Exception;

	@WebMethod(action = "topupInquiry")
	public TopupResponse topupInquiry(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam TopupParamRequest req) throws Exception;

	@WebMethod(action = "topupPayment")
	public TopupResponse topupPayment(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam TopupParamRequest req) throws Exception;

	@WebMethod(action = "topupReversal")
	public TopupResponse topupReversal(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam TopupParamRequest req) throws Exception;

	@WebMethod(action = "loadBankTransfer")
	public LoadBankTransferResponse loadBankTransfer(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadBankTransferRequest req) throws Exception;

	@WebMethod(action = "bankAccountTransferInquiry")
	public BankAccountTransferResponse bankAccountTransferInquiry(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam BankAccountTransferRequest req) throws Exception;

	@WebMethod(action = "bankAccountTransferPayment")
	public BankAccountTransferResponse bankAccountTransferPayment(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam BankAccountTransferRequest req) throws Exception;

	@WebMethod(action = "bankAccountTransferPaymentConfirmation")
	public void bankAccountTransferPaymentConfirmation(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam BankAccountTransferPaymentConfirmation req) throws Exception;

	@WebMethod(action = "loadAccountTransfer")
	public LoadAccountTransferResponse loadAccountTransfer(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadAccountTransferRequest req) throws Exception;

	@WebMethod(action = "registerAccountTransfer")
	public void registerAccountTransfer(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam RegisterAccountTransferRequest req) throws Exception;

	@WebMethod(action = "removeAccountTransfer")
	public void removeAccountTransfer(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadAccountTransferRequest req) throws Exception;
	
	@WebMethod(action = "settlementTransferInquiry")
	public SettlementTransferResponse settlementTransferInquiry(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam SettlementTransferRequest req) throws Exception;
	
	@WebMethod(action = "settlementTransferPayment")
	public SettlementTransferResponse settlementTransferPayment(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam SettlementTransferRequest req) throws Exception;

}

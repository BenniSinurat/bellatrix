package org.bellatrix.services;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Holder;

import org.bellatrix.data.Header;
import org.bellatrix.data.UnreadMessage;
import org.bellatrix.data.MessageRequest;
import org.bellatrix.data.SendMessageRequest;

@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.BARE)
@WebService
public interface Message {

	@WebMethod(action = "countUnreadMessage")
	public UnreadMessage countUnreadMessage(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam LoadMessageByUsernameRequest req);

	@WebMethod(action = "loadMessageByUsername")
	public LoadMessageResponse loadMessageByUsername(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam, @WebParam LoadMessageByUsernameRequest req);

	@WebMethod(action = "loadMessageByID")
	public LoadMessageResponse loadMessageByID(
			@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam, @WebParam LoadMessageByIDRequest req);

	@WebMethod(action = "flagMessageReadByID")
	public void flagMessageReadByID(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam MessageRequest req) throws Exception;

	@WebMethod(action = "sendMessage")
	public void sendMessage(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam SendMessageRequest req) throws Exception;

	@WebMethod(action = "deleteMessage")
	public void deleteMessage(@WebParam(header = true, name = "headerAuth") Holder<Header> headerParam,
			@WebParam MessageRequest req) throws Exception;

}

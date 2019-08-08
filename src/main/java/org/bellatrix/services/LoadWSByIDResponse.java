package org.bellatrix.services;

import org.bellatrix.data.WebServices;
import org.bellatrix.data.ResponseStatus;

public class LoadWSByIDResponse {
	private WebServices webServices;
	private ResponseStatus status;
	
	
	public WebServices getWebServices() {
		return webServices;
	}
	public void setWebServices(WebServices webServices) {
		this.webServices = webServices;
	}
	public ResponseStatus getStatus() {
		return status;
	}
	public void setStatus(ResponseStatus status) {
		this.status = status;
	}
	
	

}

package org.bellatrix.services;

import java.util.List;

import org.bellatrix.data.BillerService;
import org.bellatrix.data.ResponseStatus;

public class BillerDetailsResponse {

	private String billerName;
	private Integer billerID;
	private List<BillerService> billerService;
	private ResponseStatus status;

	public String getBillerName() {
		return billerName;
	}

	public void setBillerName(String billerName) {
		this.billerName = billerName;
	}

	public Integer getBillerID() {
		return billerID;
	}

	public void setBillerID(Integer billerID) {
		this.billerID = billerID;
	}

	public List<BillerService> getBillerService() {
		return billerService;
	}

	public void setBillerService(List<BillerService> billerService) {
		this.billerService = billerService;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

}

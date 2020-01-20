package org.bellatrix.data;

import java.io.Serializable;

public class ReportBillingRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7298223421744230693L;
	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}

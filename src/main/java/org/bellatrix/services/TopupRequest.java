package org.bellatrix.services;

import java.io.Serializable;

public class TopupRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2696898432144400564L;
	private Integer channelID;
	private String originator;
	private String username;

	public Integer getChannelID() {
		return channelID;
	}

	public void setChannelID(Integer channelID) {
		this.channelID = channelID;
	}

	public String getOriginator() {
		return originator;
	}

	public void setOriginator(String originator) {
		this.originator = originator;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}

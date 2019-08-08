package org.bellatrix.services;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CreateVAEventRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2342743744089292850L;
	private String username;
	private String eventName;
	private String description;
	private String callbackURL;
	private BigDecimal amount;
	private Date expiredDateTime;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getExpiredDateTime() {
		return expiredDateTime;
	}

	public void setExpiredDateTime(Date expiredDateTime) {
		this.expiredDateTime = expiredDateTime;
	}

	public String getCallbackURL() {
		return callbackURL;
	}

	public void setCallbackURL(String callbackURL) {
		this.callbackURL = callbackURL;
	}

}

package org.bellatrix.data;

import java.math.BigDecimal;
import java.util.Date;

public class VAEvent {
	private String eventID;
	private String eventName;
	private String description;
	private BigDecimal amount;
	private String username;
	private String formattedAmount;
	private String ticketID;
	private Date expiredAt;
	private String formattedExpiredAt;

	public String getEventID() {
		return eventID;
	}

	public void setEventID(String eventID) {
		this.eventID = eventID;
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

	public String getTicketID() {
		return ticketID;
	}

	public void setTicketID(String ticketID) {
		this.ticketID = ticketID;
	}

	public Date getExpiredAt() {
		return expiredAt;
	}

	public void setExpiredAt(Date expiredAt) {
		this.expiredAt = expiredAt;
	}

	public String getFormattedAmount() {
		return formattedAmount;
	}

	public void setFormattedAmount(String formattedAmount) {
		this.formattedAmount = formattedAmount;
	}

	public String getFormattedExpiredAt() {
		return formattedExpiredAt;
	}

	public void setFormattedExpiredAt(String formattedExpiredAt) {
		this.formattedExpiredAt = formattedExpiredAt;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}

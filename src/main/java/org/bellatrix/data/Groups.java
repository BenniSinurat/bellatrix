package org.bellatrix.data;

import java.util.Date;

public class Groups {

	private Integer id;
	private Integer notificationID;
	private Date createdDate;
	private String formattedCreatedDate;
	private String name;
	private String description;
	private int pinLength;
	private int maxPinTries;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getPinLength() {
		return pinLength;
	}

	public void setPinLength(int pinLength) {
		this.pinLength = pinLength;
	}

	public int getMaxPinTries() {
		return maxPinTries;
	}

	public void setMaxPinTries(int maxPinTries) {
		this.maxPinTries = maxPinTries;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNotificationID() {
		return notificationID;
	}

	public void setNotificationID(Integer notificationID) {
		this.notificationID = notificationID;
	}

	public String getFormattedCreatedDate() {
		return formattedCreatedDate;
	}

	public void setFormattedCreatedDate(String formattedCreatedDate) {
		this.formattedCreatedDate = formattedCreatedDate;
	}

}

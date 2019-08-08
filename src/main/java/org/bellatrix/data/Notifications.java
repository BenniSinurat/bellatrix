package org.bellatrix.data;

import java.io.Serializable;

public class Notifications implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6239066955480063888L;
	private Integer id;
	private Integer transferTypeID;
	private String name;
	private String notificationType;
	private String moduleURL;
	private boolean enabled;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTransferTypeID() {
		return transferTypeID;
	}

	public void setTransferTypeID(Integer transferTypeID) {
		this.transferTypeID = transferTypeID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public String getModuleURL() {
		return moduleURL;
	}

	public void setModuleURL(String moduleURL) {
		this.moduleURL = moduleURL;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}

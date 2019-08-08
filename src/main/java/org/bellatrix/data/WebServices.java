package org.bellatrix.data;

import java.io.Serializable;

public class WebServices implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8466301975248420787L;
	private Integer id;
	private String username;
	private String name;
	private String password;
	private String hash;
	private boolean active;
	private boolean secureTransaction;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSecureTransaction() {
		return secureTransaction;
	}

	public void setSecureTransaction(boolean secureTransaction) {
		this.secureTransaction = secureTransaction;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

}

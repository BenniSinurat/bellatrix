package org.bellatrix.data;

import java.io.Serializable;

public class MerchantCategory implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3577907232191001847L;
	private Integer id;
	private String name;
	private String code;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}

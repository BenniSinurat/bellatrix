package org.bellatrix.data;

import java.io.Serializable;

public class MerchantBusinessScale implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2741967280466033836L;
	private Integer id;
	private String scale;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

}

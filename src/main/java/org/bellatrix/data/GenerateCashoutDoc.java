package org.bellatrix.data;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.bellatrix.services.AgentCashoutRequest;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "GenerateTopup")
public class GenerateCashoutDoc implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6794027859046686783L;
	@Id
	private String id;
	@Indexed(expireAfterSeconds = 0)
	private LocalDateTime expiredAt;
	private AgentCashoutRequest request;

	public LocalDateTime getExpiredAt() {
		return expiredAt;
	}

	public void setExpiredAt(LocalDateTime expiredAt) {
		this.expiredAt = expiredAt;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public AgentCashoutRequest getRequest() {
		return request;
	}

	public void setRequest(AgentCashoutRequest request) {
		this.request = request;
	}
}

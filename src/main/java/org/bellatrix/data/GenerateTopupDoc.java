package org.bellatrix.data;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.bellatrix.services.TopupRequest;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "GenerateTopup")
public class GenerateTopupDoc implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6794027859046686783L;
	@Id
	private String id;
	@Indexed(expireAfterSeconds = 0)
	private LocalDateTime expiredAt;
	private TopupRequest request;
	private String referenceNumber;
	private Members member;

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

	public TopupRequest getRequest() {
		return request;
	}

	public void setRequest(TopupRequest request) {
		this.request = request;
	}

	public Members getMember() {
		return member;
	}

	public void setMember(Members member) {
		this.member = member;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
}

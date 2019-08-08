package org.bellatrix.data;

import java.time.LocalDateTime;

import org.bellatrix.services.GeneratePaymentTicketRequest;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "GenerateTicket")
public class GeneratePaymentDoc {

	@Id
	private String id;
	@Indexed(expireAfterSeconds = 0)
	private LocalDateTime expiredAt;
	private GeneratePaymentTicketRequest content;

	public LocalDateTime getExpiredAt() {
		return expiredAt;
	}

	public void setExpiredAt(LocalDateTime expiredAt) {
		this.expiredAt = expiredAt;
	}

	public GeneratePaymentTicketRequest getContent() {
		return content;
	}

	public void setContent(GeneratePaymentTicketRequest content) {
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}

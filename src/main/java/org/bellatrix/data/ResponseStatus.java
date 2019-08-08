package org.bellatrix.data;

public class ResponseStatus {

	private String responseCode;
	private String message;
	private String description;

	public ResponseStatus() {

	}

	public ResponseStatus(String rc, String msg, String desc) {
		this.setResponseCode(rc);
		this.setMessage(msg);
		this.setDescription(desc);
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}

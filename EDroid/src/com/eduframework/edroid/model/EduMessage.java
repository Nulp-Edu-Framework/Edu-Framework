package com.eduframework.edroid.model;

public class EduMessage {
	
	public static final String MESSAGE_TYPE = "eduMessage";
	
	private Boolean isAuthorized;
	private String messageType;

	public EduMessage(Boolean isAuthorized) {
		this.isAuthorized = isAuthorized;
		this.messageType = MESSAGE_TYPE;
	}
	
	public EduMessage(Boolean isAuthorized, String messageType) {
		this.isAuthorized = isAuthorized;
		this.messageType = messageType;
	}

	public Boolean getIsAuthorized() {
		return isAuthorized;
	}

	public void setIsAuthorized(Boolean isAuthorized) {
		this.isAuthorized = isAuthorized;
	}

	public String getMessageType() {
		return messageType;
	}

}


package com.eduframework.edroid.model;

public class PresentationEventMessage extends EduMessage {

	public static final String MESSAGE_TYPE = "presentationEventMessage";

	private String preDirection;

	public PresentationEventMessage() {
		super(true, MESSAGE_TYPE);
		this.preDirection = null;

	}

	public PresentationEventMessage(String preDirection) {
		super(true, MESSAGE_TYPE);
		this.preDirection = preDirection;
	}

	public String getPreDirection() {
		return preDirection;
	}

}

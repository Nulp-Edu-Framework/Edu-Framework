package com.nulp.eduframework.model;

public class PresentationEventMessage extends EduMessage {

	public static final String MESSAGE_TYPE = "presentationEventMessage";

	private String preDirection;

	public PresentationEventMessage(String preDirection) {
		super(true,MESSAGE_TYPE);
		this.preDirection = preDirection;
	}

	public String getPreDirection() {
		return preDirection;
	}

}

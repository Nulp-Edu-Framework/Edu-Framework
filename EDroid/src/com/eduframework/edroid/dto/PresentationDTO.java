package com.eduframework.edroid.dto;

import com.eduframework.edroid.model.PresentationEventMessage;
import com.eduframework.edroid.model.PresentationStatusMessage;

public class PresentationDTO {

	private String messageType;
	private PresentationEventMessage presentationEventMessage;
	private PresentationStatusMessage presentationStatusMessage;

	public PresentationDTO() {
		messageType = null;
		presentationEventMessage = null;
		presentationStatusMessage = null;
	}

	public PresentationDTO(PresentationEventMessage presentationEventMessage) {
		this.messageType = presentationEventMessage.getMessageType();
		this.presentationEventMessage = presentationEventMessage;
	}

	public PresentationDTO(PresentationStatusMessage presentationStatusMessage) {
		this.messageType = presentationStatusMessage.getMessageType();
		this.presentationStatusMessage = presentationStatusMessage;
	}

	public PresentationEventMessage getPresentationEventMessage() {
		return presentationEventMessage;
	}

	public void setPresentationEventMessage(
			PresentationEventMessage presentationEventMessage) {
		this.presentationEventMessage = presentationEventMessage;
	}

	public PresentationStatusMessage getPresentationStatusMessage() {
		return presentationStatusMessage;
	}

	public void setPresentationStatusMessage(
			PresentationStatusMessage presentationStatusMessage) {
		this.presentationStatusMessage = presentationStatusMessage;
	}

	public String getMessageType() {
		return messageType;
	}
	
}

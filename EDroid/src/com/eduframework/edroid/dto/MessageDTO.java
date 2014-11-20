package com.eduframework.edroid.dto;

import java.util.List;

import com.eduframework.edroid.model.Message;

public class MessageDTO {
	
	private Boolean isArray;
	private Message oneMessage;
	private List<Message> messages;

	public MessageDTO(Boolean isArray, Message oneMessage) {
		this.isArray = isArray;
		this.oneMessage = oneMessage;
		this.messages = null;
	}

	public MessageDTO(Boolean isArray, List<Message> messages) {
		this.isArray = isArray;
		this.messages = messages;
		this.oneMessage = null;
	}

	public Boolean getIsArray() {
		return isArray;
	}

	public void setIsArray(Boolean isArray) {
		this.isArray = isArray;
	}

	public Message getOneMessage() {
		return oneMessage;
	}

	public void setOneMessage(Message oneMessage) {
		this.oneMessage = oneMessage;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
	
}

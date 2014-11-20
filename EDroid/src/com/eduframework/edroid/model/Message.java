package com.eduframework.edroid.model;

public class Message extends EduMessage {
	
	public static final String MESSAGE_TYPE = "chatMessage";
	
	private final String message;
	private final String author;

	public Message() {
		super(true,MESSAGE_TYPE);
		this.author = null;
		this.message = null;
	}
	
	public Message(String author, String message) {
		super(true,MESSAGE_TYPE);
		this.author = author;
		this.message = message;
	}

	public String getText() {
		return message;
	}

	public String getAuthor() {
		return author;
	}

}

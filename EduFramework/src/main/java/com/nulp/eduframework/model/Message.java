package com.nulp.eduframework.model;

public class Message extends EduMessage {
	
	public static final String MESSAGE_TYPE = "chatMessage";
	
	private final String text;
	private final String author;

	public Message(String author, String text) {
		super(true,MESSAGE_TYPE);
		this.author = author;
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public String getAuthor() {
		return author;
	}

}

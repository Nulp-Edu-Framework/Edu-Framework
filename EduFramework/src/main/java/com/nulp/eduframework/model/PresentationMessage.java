package com.nulp.eduframework.model;

public class PresentationMessage extends EduMessage {

	public static final String MESSAGE_TYPE = "presentationMessage";
	
	private Integer preStep;
	private Integer slidesCount;

	public PresentationMessage(Integer preStep, Integer slidesCount) {
		super(true,MESSAGE_TYPE);
		this.preStep = preStep;
		this.slidesCount = slidesCount;
	}

	public Integer getPreStep() {
		return preStep;
	}

	public Integer getSlidesCount() {
		return slidesCount;
	}

}

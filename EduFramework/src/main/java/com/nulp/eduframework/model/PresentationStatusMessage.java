package com.nulp.eduframework.model;

public class PresentationStatusMessage extends EduMessage {

	public static final String MESSAGE_TYPE = "presentationStatusMessage";

	private Integer currentStep;
	private Integer stepCount;

	public PresentationStatusMessage(Integer currentStep, Integer stepCount) {
		super(true,MESSAGE_TYPE);
		this.currentStep = currentStep;
		this.stepCount = stepCount;
	}

	public Integer getCurrentStep() {
		return currentStep;
	}

	public Integer getStepCount() {
		return stepCount;
	}

}

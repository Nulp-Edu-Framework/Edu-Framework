package com.nulp.eduframework.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "CHAT")
public class LectureChat {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "currentStep")
	private Integer currentStep;
	
	@Column(name = "stepCount")
	private Integer stepCount;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "id", nullable = true)
	private List<LectureMessage> lectureMessages;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public Integer getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(Integer currentStep) {
		this.currentStep = currentStep;
	}

	public Integer getStepCount() {
		return stepCount;
	}

	public void setStepCount(Integer stepCount) {
		this.stepCount = stepCount;
	}
	
	public List<LectureMessage> getLectureMessages() {
		return lectureMessages;
	}

	public Integer nextStep () {
		Integer nextStep = currentStep + 1;
		return nextStep < stepCount ? nextStep : -1;
	}
	
	public Integer prevStep () {
		Integer prevStep = currentStep - 1;
		return prevStep >= 0 ? prevStep : -1;
	}

}

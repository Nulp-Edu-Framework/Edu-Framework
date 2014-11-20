package com.eduframework.edroid.service;

import org.atmosphere.wasync.Function;

import com.eduframework.edroid.dto.PresentationDTO;

public interface EduFrameworkPresentationService {
	public Boolean connectToPresentationStream (String eduSecureToken, String serverAddress, Function<PresentationDTO> onMessageFunction, Function<String> onErrorFunction);
	public Boolean nextSlide ();
	public Boolean prevSlide ();
	public Boolean restart ();
	public void leavePresentation ();
	public Integer getLectureId();
}

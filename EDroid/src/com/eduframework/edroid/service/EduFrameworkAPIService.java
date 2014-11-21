package com.eduframework.edroid.service;

import com.eduframework.edroid.util.OnFinishTask;

public interface EduFrameworkAPIService {
	public Boolean login(String userName, String password, OnFinishTask onFinish);
	Boolean getSecureToken(OnFinishTask onFinish);
	public Boolean getAllLectures(OnFinishTask onFinish);
	public Boolean loadPresentationContent(Integer lectureId, OnFinishTask onFinish);
	public String getSecureToken();
}

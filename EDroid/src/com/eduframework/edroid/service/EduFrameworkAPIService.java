package com.eduframework.edroid.service;

import com.eduframework.edroid.util.OnFinishTask;

public interface EduFrameworkAPIService {
	public Boolean login(String userName, String password);
	public String  getSecureToken();
	public Boolean getAllLectures(OnFinishTask onFinish);
	public Boolean loadPresentationContent(Integer lectureId, OnFinishTask onFinish);
}

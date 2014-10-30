package com.nulp.eduframework.service;

import java.util.List;

import com.nulp.eduframework.domain.LectureChat;

public interface LectureChatService {
	public List<LectureChat> lectureChatsList();
	public LectureChat getLectureChatById(Integer id);
	public void addLectureChat(LectureChat lectureChat);
}

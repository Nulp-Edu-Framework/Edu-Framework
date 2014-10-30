package com.nulp.eduframework.dao;

import java.util.List;

import com.nulp.eduframework.domain.LectureChat;

public interface LectureChatDao {
	public List<LectureChat> lectureChatsList();
	public LectureChat getLectureChatById(Integer id);
	public void addLectureChat(LectureChat lectureChat);
}

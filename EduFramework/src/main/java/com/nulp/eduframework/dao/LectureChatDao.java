package com.nulp.eduframework.dao;

import java.util.List;

import org.hibernate.Session;

import com.nulp.eduframework.domain.LectureChat;

public interface LectureChatDao {
	public List<LectureChat> lectureChatsList();
	public LectureChat getLectureChatById(Integer id);
	public void addLectureChat(LectureChat lectureChat);
	public List<LectureChat> lectureChatsList(Session session);
	public LectureChat getLectureChatById(Integer id, Session session);
	public void addLectureChat(LectureChat lectureChat, Session session);
}

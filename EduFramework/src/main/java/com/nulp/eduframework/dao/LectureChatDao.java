package com.nulp.eduframework.dao;

import java.util.List;

import org.hibernate.Session;

import com.nulp.eduframework.controller.dto.LectureDTO;
import com.nulp.eduframework.domain.LectureChat;

public interface LectureChatDao {
	public List<LectureChat> lectureChatsList(Session session);
	public List<LectureDTO> getLectureDTOList (Session session);
	public LectureChat getLectureChatById(Integer id, Session session);
	public void addLectureChat(LectureChat lectureChat, Session session);
	public void deleteLecture(LectureChat lecture, Session session);
}

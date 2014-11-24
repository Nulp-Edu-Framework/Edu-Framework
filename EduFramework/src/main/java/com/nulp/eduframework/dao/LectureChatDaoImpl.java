package com.nulp.eduframework.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.nulp.eduframework.controller.dto.LectureDTO;
import com.nulp.eduframework.domain.LectureChat;

@Repository
public class LectureChatDaoImpl extends EduFrameworkDao implements LectureChatDao {
	
	@SuppressWarnings("unchecked")
	public List<LectureChat> lectureChatsList(Session session) {
		session = checkSession(session);
		return session.createQuery("from LectureChat").list();
	}

	@Override
	public LectureChat getLectureChatById(Integer id, Session session) {
		session = checkSession(session);
		LectureChat lectureChat = (LectureChat) session.load(LectureChat.class, id);
		return lectureChat;
	}

	@Override
	public void addLectureChat(LectureChat lectureChat, Session session) {
		session = checkSession(session);
		session.saveOrUpdate(lectureChat);
		session.flush();
	}

	@SuppressWarnings("unchecked")
	public List<LectureDTO> getLectureDTOList(Session session) {
		session = checkSession(session);
		String queryStr = "select NEW com.nulp.eduframework.controller.dto.LectureDTO(lecture.id, lecture.name) from LectureChat as lecture";
		Query query = session.createQuery(queryStr);
		return query.list();
	}

	@Override
	public void deleteLecture(LectureChat lecture, Session session) {
		session = checkSession(session);
		session.delete(lecture);
		session.flush();
	}

}

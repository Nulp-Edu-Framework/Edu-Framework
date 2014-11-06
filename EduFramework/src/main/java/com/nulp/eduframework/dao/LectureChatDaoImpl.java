package com.nulp.eduframework.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
	}

}

package com.nulp.eduframework.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.nulp.eduframework.domain.LectureChat;
import com.nulp.eduframework.domain.LectureMessage;

@Repository
public class LectureMessageDaoImpl extends EduFrameworkDao implements LectureMessageDao {

	@Override
	public void addLectureMessage(LectureMessage message, Session session) {
		session = checkSession(session);
		session.saveOrUpdate(message);
	}

	@SuppressWarnings("unchecked")
	public List<LectureMessage> getAllMessagesByLecture(LectureChat lecture, Session session) {
		session = checkSession(session);
		Query query = session.createQuery("select lectureMessage from LectureMessage as lectureMessage where lectureMessage.lectureChat = :lecture");
		query.setParameter("lecture", lecture);
		return query.list();
	}

}

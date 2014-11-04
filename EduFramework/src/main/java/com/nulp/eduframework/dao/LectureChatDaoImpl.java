package com.nulp.eduframework.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.nulp.eduframework.domain.LectureChat;

@Repository
public class LectureChatDaoImpl implements LectureChatDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	public List<LectureChat> lectureChatsList() {
		Session session = sessionFactory.getCurrentSession();
		return session.createQuery("from LectureChat").list();
	}

	@Override
	public LectureChat getLectureChatById(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		LectureChat lectureChat = (LectureChat) session.load(LectureChat.class, id);
		return lectureChat;
	}

	@Override
	public void addLectureChat(LectureChat lectureChat) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(lectureChat);
	}

	@SuppressWarnings("unchecked")
	public List<LectureChat> lectureChatsList(Session session) {
		return session.createQuery("from LectureChat").list();
	}

	@Override
	public LectureChat getLectureChatById(Integer id, Session session) {
		LectureChat lectureChat = (LectureChat) session.load(LectureChat.class, id);
		return lectureChat;
	}

	@Override
	public void addLectureChat(LectureChat lectureChat, Session session) {
		session.saveOrUpdate(lectureChat);
	}

}

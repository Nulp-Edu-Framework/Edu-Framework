package com.nulp.eduframework.dao;

import java.util.List;

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
		return sessionFactory.getCurrentSession().createQuery("from LectureChat").list();
	}

	@Override
	public LectureChat getLectureChatById(Integer id) {
		LectureChat lectureChat = (LectureChat) sessionFactory.getCurrentSession().load(LectureChat.class, id);
		return lectureChat;
	}

	@Override
	public void addLectureChat(LectureChat lectureChat) {
		sessionFactory.getCurrentSession().saveOrUpdate(lectureChat);
	}

}

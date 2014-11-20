package com.nulp.eduframework.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EduFrameworkDao {
	
	@Autowired
	protected SessionFactory sessionFactory;

	protected Session checkSession(Session session){
		return session == null ? sessionFactory.getCurrentSession() : session;
	}
	
	
}

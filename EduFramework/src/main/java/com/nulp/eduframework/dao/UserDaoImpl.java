package com.nulp.eduframework.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.nulp.eduframework.domain.User;

@Repository
public class UserDaoImpl extends EduFrameworkDao implements UserDao {

	@Override
	public User getUserByUserName(String userName, Session session) {
		session = checkSession(session);
		Query query = session.createQuery("select user from User as user where user.username = :userName");
		query.setParameter("userName", userName);
		User user = (User) query.uniqueResult();
		return user;
	}
	
	@Override
	public User getUserBySecureToken(String secureToken, Session session) {
		session = checkSession(session);
		Query query = session.createQuery("select user from User as user where user.secureToken = :secureToken");
		query.setParameter("secureToken", secureToken);
		User user = (User) query.uniqueResult();
		return user;
	}
	
	@Override
	public void addUser(User user, Session session) {
		session = checkSession(session);
		session.saveOrUpdate(user);
	}

}

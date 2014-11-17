package com.nulp.eduframework.service;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nulp.eduframework.dao.UserDao;
import com.nulp.eduframework.domain.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserDao userDao;
	
	@Transactional
	public User getUserByUSerName(String userName) {
		return userDao.getUserByUserName(userName, null);
	}

	@Transactional
	public User getUserByUSerName(String userName, Session session) {
		return userDao.getUserByUserName(userName, session);
	}

	@Transactional
	public void addUser(User user, Session session) {
		userDao.addUser(user, session);
	}

	@Transactional
	public void addUser(User user) {
		userDao.addUser(user, null);
	}

}

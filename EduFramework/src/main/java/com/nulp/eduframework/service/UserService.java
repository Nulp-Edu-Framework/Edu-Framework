package com.nulp.eduframework.service;

import org.hibernate.Session;

import com.nulp.eduframework.domain.User;

public interface UserService {
	public User getUserByUSerName(String userName);
	public User getUserByUSerName(String userName, Session session);
	public void addUser(User user);
	public void addUser(User user, Session session);
}

package com.nulp.eduframework.dao;

import org.hibernate.Session;

import com.nulp.eduframework.domain.User;

public interface UserDao  {
	 public User getUserByUserName(String userName, Session session);
}

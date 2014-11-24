package com.nulp.eduframework.dao;

import java.util.List;

import org.hibernate.Session;

import com.nulp.eduframework.controller.dto.UserDTO;
import com.nulp.eduframework.domain.User;

public interface UserDao  {
	 public User getUserByUserName(String userName, Session session);
	 public User getUserBySecureToken(String secureToken, Session session);
	 public UserDTO getUserDTOBySecureToken(String secureToken, Session session);
	 public void addUser(User user, Session session);
	 public List<User> getUsersList(Session session);
	 public void deleteUser(User user, Session session);
}

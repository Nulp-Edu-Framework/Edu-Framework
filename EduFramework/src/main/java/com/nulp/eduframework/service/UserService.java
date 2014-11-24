package com.nulp.eduframework.service;

import java.util.List;

import org.hibernate.Session;

import com.nulp.eduframework.controller.dto.UserDTO;
import com.nulp.eduframework.domain.User;

public interface UserService {
	public User getUserByUSerName(String userName);
	public User getUserByUSerName(String userName, Session session);
	public void addUser(User user);
	public void addUser(User user, Session session);
	public void deleteUser(User user);
	public void deleteUser(User user, Session session);
	public UserDTO getUserByToken(String token);
	public UserDTO getUserByToken(String token, Session session);
	public List<User> getUsersList();
	public List<User> getUsersList(Session session);
}

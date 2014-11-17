package com.nulp.eduframework.service;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import com.nulp.eduframework.domain.User;

@Service
public class EduAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler  {

	@Autowired
	private UserService userService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
		User user = userService.getUserByUSerName(authentication.getName());
		if(user != null && user.getSecureToken() == null){
			String secureToken = UUID.randomUUID().toString() + user.getId().toString();
			user.setSecureToken(secureToken);
			userService.addUser(user);
		}
		super.onAuthenticationSuccess(request, response, authentication);
	}

}

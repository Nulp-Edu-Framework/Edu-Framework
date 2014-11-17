package com.nulp.eduframework.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Service;

import com.nulp.eduframework.domain.User;

@Service
public class EduLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

	@Autowired
	private UserService userService;

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		User user = userService.getUserByUSerName(authentication.getName());
		if (user != null) {
			//user.setSecureToken(null);
			//userService.addUser(user);
		}
		super.onLogoutSuccess(request, response, authentication);
	}

}

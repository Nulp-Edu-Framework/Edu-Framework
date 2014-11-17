package com.nulp.eduframework.service;

import org.atmosphere.cpr.AtmosphereResource;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nulp.eduframework.dao.UserDao;
import com.nulp.eduframework.domain.User;
import com.nulp.eduframework.util.Constants;

@Service
public class SecureServiceImpl implements SecureService {
	
	@Autowired
	private UserDao userDao;
	
	@Transactional
	public Boolean isAuthorized(AtmosphereResource atmosphereResource) {
		System.out.println("OLD VERSION OF THIS");
		String secureTokenHeader = atmosphereResource.getRequest().getHeader(Constants.SECURE_TOKEN_HEADER_NAME);
		return secureTokenHeader != null && !secureTokenHeader.isEmpty();
	}

	@Override
	public Boolean isAuthorized(AtmosphereResource atmosphereResource,Session session) {
		System.out.println("HEADER : " + atmosphereResource.getRequest().getHeader(Constants.SECURE_TOKEN_HEADER_NAME));
		User user = userDao.getUserBySecureToken(atmosphereResource.getRequest().getHeader(Constants.SECURE_TOKEN_HEADER_NAME), session);
		return user != null;
	}
}

package com.nulp.eduframework.service;

import org.atmosphere.cpr.AtmosphereResource;
import org.hibernate.Session;

public interface SecureService {
	public Boolean isAuthorized(AtmosphereResource atmosphereResource);
	public Boolean isAuthorized(AtmosphereResource atmosphereResource, Session session);
}

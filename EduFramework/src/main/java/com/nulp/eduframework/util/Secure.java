package com.nulp.eduframework.util;

import org.atmosphere.cpr.AtmosphereResource;

public class Secure {
	public static Boolean isAuthorized(AtmosphereResource atmosphereResource) {
		String secureTokenHeader = atmosphereResource.getRequest().getHeader(Constants.SECURE_TOKEN_HEADER_NAME);
		return secureTokenHeader != null && !secureTokenHeader.isEmpty();
	}
}

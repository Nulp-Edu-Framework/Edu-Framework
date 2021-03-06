package com.nulp.eduframework.controller;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.Meteor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

public class AtmosphereResolver implements HandlerMethodArgumentResolver{

	public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        Meteor m = Meteor.build(nativeWebRequest.getNativeRequest(HttpServletRequest.class));
        AtmosphereResource atmosphereResource = m.getAtmosphereResource();
        return atmosphereResource;
	}

	public boolean supportsParameter(MethodParameter methodParameter) {
		return AtmosphereResource.class.isAssignableFrom(methodParameter.getParameterType());
	}

}
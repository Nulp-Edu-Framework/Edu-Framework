package com.nulp.eduframework.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nulp.eduframework.service.UserService;

@Controller
@RequestMapping("/api/v1/secure")
public class SecureController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/securetoken", method = RequestMethod.GET)
	@ResponseBody public String getSecureToken(Principal principal) {
		String securetoken = userService.getUserByUSerName(principal.getName()).getSecureToken();
		return securetoken == null ? "user not authorized" : securetoken;
	}
}

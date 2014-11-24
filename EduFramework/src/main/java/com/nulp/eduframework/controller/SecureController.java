package com.nulp.eduframework.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.nulp.eduframework.controller.dto.UserDTO;
import com.nulp.eduframework.domain.User;
import com.nulp.eduframework.service.UserService;

@Controller
@RequestMapping("/api/v1/secure")
public class SecureController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/securetoken", method = RequestMethod.GET)
	@ResponseBody public String getSecureToken(Principal principal) {
		User user = userService.getUserByUSerName(principal.getName());
		String securetoken = user.getSecureToken();
		System.out.println(user.getUserRole().getRole());
		return securetoken == null ? "user not authorized" : securetoken;
	}
	
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	@ResponseBody public String getUserByToken(@RequestParam("token") String token) {
		UserDTO user = userService.getUserByToken(token);
		return user == null ? null :new Gson().toJson(user);
	}
}

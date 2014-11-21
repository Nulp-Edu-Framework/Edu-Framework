package com.nulp.eduframework.controller.dto;

public class UserDTO {
	private String userLogin;
	private String userFirstName;
	private String userLastName;
	
	public UserDTO(String userLogin, String userFirstName, String userLastName) {
		this.userLogin = userLogin;
		this.userFirstName = userFirstName;
		this.userLastName = userLastName;
	}
	
	public String getUserLogin() {
		return userLogin;
	}
	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}
	public String getUserFirstName() {
		return userFirstName;
	}
	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}
	public String getUserLastName() {
		return userLastName;
	}
	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}
	
}

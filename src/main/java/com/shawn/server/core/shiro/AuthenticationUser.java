package com.shawn.server.core.shiro;

import java.io.Serializable;

public class AuthenticationUser implements Serializable {
	
	private static final long serialVersionUID = -6354749619261807240L;
	private String username;
	private String password;
	private String salt;
	private Integer state; // 0-normal 1-offline

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getCredentialsSalt() {
		return username + salt;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
}

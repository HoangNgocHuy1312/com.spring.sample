package com.spring.sample.model;

import com.spring.sample.entity.Role;

@SuppressWarnings("serial")
public class CustomUserDetails extends org.springframework.security.core.userdetails.User {
	private UserModel user = null;

	public CustomUserDetails(UserModel user) {
		super(user.getEmail(), user.getPassword(), user.activated(), true, true, true,
				Role.mapRolesToAuthorities(user.getRole()));
		this.user = user;
	}

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

}

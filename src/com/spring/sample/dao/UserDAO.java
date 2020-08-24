package com.spring.sample.dao;

import com.spring.sample.entity.User;

public interface UserDAO extends GenericDAO<User, Integer> {
	public User findUser(User user);

	public User findUserByEmail(String email);
	
	public boolean existingEmail(String email, Integer id);

}

package com.spring.sample.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.spring.sample.entity.User;

public interface UserDAO extends GenericDAO<User, Integer> {
	public User findUser(User user);

	public User findUserByEmail(String email);
	
	public boolean existingEmail(String email, Integer id);

	public Page<User> following(User user, Pageable pageable);
	
	public Page<User> followers(User user, Pageable pageable);

}

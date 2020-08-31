package com.spring.sample.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.spring.sample.model.UserModel;

public interface UserService extends UserDetailsService, PersistentTokenRepository {
	public UserModel findUserByEmail(String email);
	
	public boolean existingEmail(String email, Integer id);

	public UserModel findUser(Integer id);

	public UserModel addUser(UserModel user) throws Exception;

	public UserModel editUser(UserModel userModel) throws Exception;

	public boolean deleteUser(UserModel userModel) throws Exception;

	public List<UserModel> findAll();

	public Page<UserModel> paginate(UserModel userModel);
}

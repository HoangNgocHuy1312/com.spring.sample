package com.spring.sample.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.spring.sample.model.UserModel;

public interface UserService extends UserDetailsService, PersistentTokenRepository {
	public Optional<UserModel> findUserByEmail(String email);

	public boolean existingEmail(String email, Integer id);

	public Optional<UserModel> findUser(Integer id);
	
	public UserModel getUserInfo(UserModel condition);

	public UserModel addUser(UserModel user) throws Exception;

	public UserModel editUser(UserModel userModel) throws Exception;

	public boolean deleteUser(UserModel userModel) throws Exception;

	public boolean follow(UserModel follower, UserModel followed) throws Exception;

	public boolean unfollow(UserModel follower, UserModel followed) throws Exception;
	
	public boolean isFollowing(UserModel follower, UserModel followed);
	
	public boolean createActivationDigest(UserModel userModel) throws Exception;
	
    public boolean createPasswordReset(final UserModel userModel) throws Exception;

//    public PasswordResetToken getPasswordResetToken(final String token);
//    
//    public Optional<UserModel> getUserByPasswordResetToken(final String token);
//
//    public Optional<UserModel> getUserByID(final long id);
//
    public boolean changeUserPassword(final UserModel userModel) throws Exception;
	
	public boolean active(UserModel userModel) throws Exception;

	public Page<UserModel> following(UserModel userModel);

	public Page<UserModel> followers(UserModel userModel);

	public List<UserModel> findAll();

	public Page<UserModel> paginate(UserModel userModel);
}

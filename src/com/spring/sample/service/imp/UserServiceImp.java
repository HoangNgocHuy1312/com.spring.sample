package com.spring.sample.service.imp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.github.sonus21.rqueue.core.RqueueMessageSender;
import com.spring.sample.dao.MicropostDAO;
import com.spring.sample.dao.RelationshipDAO;
import com.spring.sample.dao.UserDAO;
import com.spring.sample.entity.Relationship;
import com.spring.sample.entity.Role;
import com.spring.sample.entity.User;
import com.spring.sample.model.CustomUserDetails;
import com.spring.sample.model.UserModel;
import com.spring.sample.service.UserService;
import com.spring.sample.util.Constants;

@Service
@PropertySource("classpath:application.properties")
public class UserServiceImp implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImp.class);

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private RelationshipDAO relationshipDAO;

	@Autowired
	private MicropostDAO micropostDAO;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private @NonNull RqueueMessageSender rqueueMessageSender;

	private UserServiceImp() {
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public void setRelationshipDAO(RelationshipDAO relationshipDAO) {
		this.relationshipDAO = relationshipDAO;
	}

	public void setMicropostDAO(MicropostDAO micropostDAO) {
		this.micropostDAO = micropostDAO;
	}

	public Optional<UserModel> findUserByEmail(String email) {
		logger.info("Fetching the user by email in the database");
		try {
			return Optional.ofNullable(userDAO.findUserByEmail(email)).map((user) -> {
				UserModel userModel = new UserModel();
				BeanUtils.copyProperties(user, userModel);
				return userModel;
			});
		} catch (Exception e) {
			logger.error("An error occurred while fetching the user details by email from the database", e);
			return Optional.empty();
		}
	}

	@Override
	public boolean existingEmail(String email, Integer id) {
		logger.info("Checking the user by email in the database");
		try {
			return userDAO.existingEmail(email, id);
		} catch (Exception e) {
			logger.error("An error occurred while checking the user details by email from the database", e);
			return true;
		}
	}

	public Optional<UserModel> findUser(Integer id) {
		logger.info("Checking the user in the database");
		try {
			return Optional.ofNullable(userDAO.find(id)).map((user) -> {
				UserModel userModel = new UserModel();
				BeanUtils.copyProperties(user, userModel);
				return userModel;
			});
		} catch (Exception e) {
			logger.error("An error occurred while fetching the user details from the database", e);
			return null;
		}
	}

	public UserModel getUserInfo(UserModel condition) {
		logger.info("Fetching the user info in the database");
		try {
			User user = userDAO.find(condition.getId());
			UserModel userModel = new UserModel();
			if (user != null) {
				BeanUtils.copyProperties(user, userModel);

				userModel.setTotalMicropost(micropostDAO.count(Restrictions.eq("userId", condition.getId())));
				userModel.setTotalFollowing(relationshipDAO.count(Restrictions.eq("followerId", condition.getId())));
				userModel.setTotalFollowers(relationshipDAO.count(Restrictions.eq("followedId", condition.getId())));

				if (condition.getCurrentUserId() != null) {
					Relationship relationship = new Relationship();
					relationship.setFollowerId(condition.getCurrentUserId());
					relationship.setFollowedId(condition.getId());
					userModel.setFollowedByCurrentUser(relationshipDAO.isFollowing(relationship));
				}
			}
			return userModel;
		} catch (Exception e) {
			logger.error("An error occurred while fetching the user details from the database", e);
			return null;
		}
	}

	@Transactional
	public UserModel addUser(UserModel userModel) throws Exception {
		logger.info("Adding the user in the database");
		try {
			String token = UUID.randomUUID().toString();

			User condition = new User();
			condition.setName(userModel.getName());
			condition.setEmail(userModel.getEmail());
			condition.setPassword(passwordEncoder.encode(userModel.getPassword()));
			condition.setRole(Role.USER_ROLE);
			condition.unactivate();
			condition.setActivationDigest(token);
			User user = userDAO.makePersistent(condition);
			userModel = new UserModel();
			BeanUtils.copyProperties(user, userModel);

			rqueueMessageSender.enqueue(Constants.ACCOUNT_ACTIVATION_QUEUE, userModel);
			return userModel;
		} catch (Exception e) {
			logger.error("An error occurred while adding the user details to the database", e);
			throw e;
		}
	}

	@Transactional
	public UserModel editUser(UserModel userModel) throws Exception {
		logger.info("Updating the user in the database");
		try {
			User user = userDAO.find(userModel.getId(), true);
			if (StringUtils.hasText(userModel.getName())) {
				user.setName(userModel.getName());
			}
			if (StringUtils.hasText(userModel.getEmail())) {
				user.setEmail(userModel.getEmail());
			}
			if (StringUtils.hasText(userModel.getPassword())) {
				user.setPassword(passwordEncoder.encode(userModel.getPassword()));
			}
			userDAO.makePersistent(user);
			return userModel;
		} catch (Exception e) {
			logger.error("An error occurred while updating the user details to the database", e);
			throw e;
		}
	}

	@Transactional
	public boolean deleteUser(UserModel userModel) throws Exception {
		logger.info("Deleting the user in the database");
		try {
			User user = userDAO.find(userModel.getId(), true);
			userDAO.makeTransient(user);
			return true;
		} catch (Exception e) {
			logger.error("An error occurred while adding the user details to the database", e);
			throw e;
		}
	}

	public List<UserModel> findAll() {
		logger.info("Fetching all users in the database");
		List<UserModel> userModelList = new ArrayList<UserModel>();
		try {
			List<User> userList = userDAO.findAll();
			return userList.stream().map(user -> {
				UserModel userModel = new UserModel();
				BeanUtils.copyProperties(user, userModel);
				return userModel;
			}).collect(Collectors.toList());
		} catch (Exception e) {
			logger.error("An error occurred while fetching all users from the database", e);
		}
		return userModelList;
	}

	@Transactional
	public boolean follow(UserModel follower, UserModel followed) throws Exception {
		try {
			Relationship relationship = new Relationship();
			relationship.setFollowerId(follower.getId());
			relationship.setFollowedId(followed.getId());
			relationshipDAO.makePersistent(relationship);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	@Transactional
	public boolean unfollow(UserModel follower, UserModel followed) throws Exception {
		try {
			Relationship condition = new Relationship();
			condition.setFollowerId(follower.getId());
			condition.setFollowedId(followed.getId());
			Relationship relationship = relationshipDAO.load(condition);
			if (relationship != null) {
				relationshipDAO.makeTransient(relationship);
			}
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	public boolean isFollowing(UserModel follower, UserModel followed) {
		try {
			Relationship condition = new Relationship();
			condition.setFollowerId(follower.getId());
			condition.setFollowedId(followed.getId());
			return relationshipDAO.isFollowing(condition);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Transactional
	public boolean createActivationDigest(UserModel userModel) throws Exception {
		logger.info("Updating the user in the database");
		try {
			User user = userDAO.find(userModel.getId(), true);
			if (StringUtils.hasText(userModel.getActivationDigest())) {
				user.setActivationDigest(userModel.getActivationDigest());
				user.unactivate();
			}
			userDAO.makePersistent(user);
			return true;
		} catch (Exception e) {
			logger.error("An error occurred while updating the user details to the database", e);
			throw e;
		}
	}

	@Transactional
	public boolean createPasswordReset(final UserModel userModel) throws Exception {
		logger.info("Create password reset for the user in the database");
		try {
			final User user = userDAO.findUserByEmail(userModel.getEmail());
			if (user == null) {
				return false;
			}

			final String token = UUID.randomUUID().toString();
			user.setResetDigest(token);
			user.setResetSentAt(userDAO.getSystemTimestamp());
			userDAO.makePersistent(user);

			UserModel event = new UserModel();
			BeanUtils.copyProperties(user, event);
			rqueueMessageSender.enqueue(Constants.PASSWORD_RESET_QUEUE, userModel);
			return true;
		} catch (Exception e) {
			logger.error("An error occurred while create password reset for the user to the database", e);
			throw e;
		}
	}

	@Transactional
	public boolean active(UserModel userModel) throws Exception {
		logger.info("Active for the user in the database");
		try {
			User user = userDAO.find(userModel.getId(), true);
			user.activate();
			user.setActivatedAt(userDAO.getSystemTimestamp());
			userDAO.makePersistent(user);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	@Transactional
	public boolean changeUserPassword(final UserModel userModel) throws Exception {
		logger.info("Reset password for the user in the database");
		try {
			User user = userDAO.findUserByEmail(userModel.getEmail());
			if (user == null) {
				return false;
			}
			user.setPassword(passwordEncoder.encode(userModel.getPassword()));
			userDAO.makePersistent(user);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Page<UserModel> following(UserModel userModel) {
		logger.info("Fetching all following in the database");
		try {
			User condition = new User();
			condition.setId(userModel.getId());
			Page<User> users = userDAO.following(condition, userModel.getPageable());
			return users.map(user -> {
				UserModel model = new UserModel();
				BeanUtils.copyProperties(user, model);
				return model;
			});
		} catch (Exception e) {
			logger.error("An error occurred while fetching all following from the database", e);
			return null;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Page<UserModel> followers(UserModel userModel) {
		logger.info("Fetching all followers in the database");
		try {
			User condition = new User();
			condition.setId(userModel.getId());
			Page<User> users = userDAO.followers(condition, userModel.getPageable());
			return users.map(user -> {
				UserModel model = new UserModel();
				BeanUtils.copyProperties(user, model);
				return model;
			});
		} catch (Exception e) {
			logger.error("An error occurred while fetching all followers from the database", e);
			return null;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userDAO.findUserByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		UserModel userModel = new UserModel();
		BeanUtils.copyProperties(user, userModel);
		return new CustomUserDetails(userModel);
	}

	@Override
	@Transactional
	public void createNewToken(PersistentRememberMeToken token) {
		try {
			User user = userDAO.findUserByEmail(token.getUsername());
			user.setSeries(token.getSeries());
			user.setToken(token.getTokenValue());
			user.setLastUsed(token.getDate());

			userDAO.makePersistent(user);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	@Transactional
	public void updateToken(String series, String tokenValue, Date lastUsed) {
		try {
			User condition = new User();
			condition.setSeries(series);
			User user = userDAO.findUser(condition);
			user.setToken(tokenValue);
			user.setLastUsed(lastUsed);

			userDAO.makePersistent(user);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public PersistentRememberMeToken getTokenForSeries(String seriesId) {
		try {
			User condition = new User();
			condition.setSeries(seriesId);
			User user = userDAO.findUser(condition);
			if (user != null) {
				return new PersistentRememberMeToken(user.getEmail(), user.getSeries(), user.getToken(),
						user.getLastUsed());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	@Transactional
	public void removeUserTokens(String username) {
		try {
			User user = userDAO.findUserByEmail(username);
			user.setSeries(null);
			user.setToken(null);
			user.setLastUsed(null);

			userDAO.makePersistent(user);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Page<UserModel> paginate(UserModel userModel) {
		try {
			Page<User> users = userDAO.paginate(userModel.getPageable());
			return users.map(user -> {
				UserModel model = new UserModel();
				BeanUtils.copyProperties(user, model);
				return model;
			});
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

}

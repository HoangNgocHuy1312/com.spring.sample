package com.spring.sample.dao.imp;

import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;

import com.spring.sample.dao.UserDAO;
import com.spring.sample.entity.User;
import com.spring.sample.util.SearchQueryTemplate;

@Repository
public class UserDAOImp extends GenericDAOImp<User, Integer> implements UserDAO {
	private static final Logger logger = LoggerFactory.getLogger(UserDAOImp.class);

	public UserDAOImp() {
		super(User.class);
	}

	public User findUser(User user) {
		logger.info("Finding the user in the database");
		return DataAccessUtils.uniqueResult(findByExample(user));
	}

	public User findUserByEmail(String email) {
		logger.info("Finding the user by email in the database");
		return getHibernateTemplate().execute(session -> {
			Query<User> query = session.createQuery("FROM User WHERE email = :email", User.class);
			query.setParameter("email", email);
			return query.uniqueResult();
		});
	}

	public boolean existingEmail(String email, Integer id) {
		logger.info("Finding the user by email in the database");
		return getHibernateTemplate().execute(session -> {
			String sql = "SELECT COUNT(*) FROM User WHERE email = :email";
			if (id != null) {
				sql += " AND id <> :id";
			}
			Query<Long> query = session.createQuery(sql, Long.class);
			query.setParameter("email", email);
			if (id != null) {
				query.setParameter("id", id);
			}
			return query.uniqueResult();
		}) > 0;
	}

	@Override
	public Page<User> following(User user, Pageable pageable) {
		String sql = "SELECT following FROM User AS user INNER JOIN user.following AS following WHERE user.id = :userId";
		String countSql = "SELECT COUNT(*) FROM User AS user INNER JOIN user.following AS following WHERE user.id = :userId";
		SearchQueryTemplate searchQueryTemplate = new SearchQueryTemplate(sql, countSql, pageable);
		searchQueryTemplate.addParameter("userId", user.getId());
		searchQueryTemplate.addOrder(Direction.DESC, "user.createdAt");
		return paginate(searchQueryTemplate);
	}

	@Override
	public Page<User> followers(User user, Pageable pageable) {
		String sql = "SELECT followers FROM User AS user INNER JOIN user.followers AS followers WHERE user.id = :userId";
		String countSql = "SELECT COUNT(*) FROM User AS user INNER JOIN user.followers AS followers WHERE user.id = :userId";
		SearchQueryTemplate searchQueryTemplate = new SearchQueryTemplate(sql, countSql, pageable);
		searchQueryTemplate.addParameter("userId", user.getId());
		searchQueryTemplate.addOrder(Direction.DESC, "user.createdAt");
		return paginate(searchQueryTemplate);
	}

}

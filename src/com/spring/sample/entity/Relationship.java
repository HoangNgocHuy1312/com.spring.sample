package com.spring.sample.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Relationship extends BaseEntity implements Serializable {

	private Integer id;
	private Integer followerId;
	private Integer followedId;

	private User follower;
	private User followed;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFollowerId() {
		return followerId;
	}

	public void setFollowerId(Integer followerId) {
		this.followerId = followerId;
	}

	public Integer getFollowedId() {
		return followedId;
	}

	public void setFollowedId(Integer followedId) {
		this.followedId = followedId;
	}

	public User getFollower() {
		return follower;
	}

	public void setFollower(User follower) {
		this.follower = follower;
	}

	public User getFollowed() {
		return followed;
	}

	public void setFollowed(User followed) {
		this.followed = followed;
	}

}

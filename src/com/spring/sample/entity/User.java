package com.spring.sample.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
public class User extends BaseEntity implements Serializable {
	
	private Integer id;
	private String name;
	private String email;
	private String password;
	private String series;
	private String token;
	private Date lastUsed;
	private Integer role;
	private String activationDigest;
	private Integer activated;
	private Date activatedAt;
	private String resetDigest;
	private Date resetSentAt;

	private List<Micropost> microposts;
	
	private List<Relationship> activeRelationships;
	private List<Relationship> passiveRelationships;
	
	private List<User> following;
	private List<User> followers;

	public User() {

	}

	public User(Integer id) {
		this.id = id;
	}

	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getLastUsed() {
		return lastUsed;
	}

	public void setLastUsed(Date lastUsed) {
		this.lastUsed = lastUsed;
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

	public String getActivationDigest() {
		return activationDigest;
	}

	public void setActivationDigest(String activationDigest) {
		this.activationDigest = activationDigest;
	}

	public Integer getActivated() {
		return activated;
	}
	
	public boolean activated() {
		return activated == Activation.ACTIVATED.value;
	}

	public void setActivated(Integer activated) {
		this.activated = activated;
	}
	
	public void activate() {
		this.activated = Activation.ACTIVATED.value;
	}
	
	public void unactivate() {
		this.activated = Activation.UNACTIVATED.value;
	}
	
	public Date getActivatedAt() {
		return activatedAt;
	}

	public void setActivatedAt(Date activatedAt) {
		this.activatedAt = activatedAt;
	}

	public String getResetDigest() {
		return resetDigest;
	}

	public void setResetDigest(String resetDigest) {
		this.resetDigest = resetDigest;
	}

	public Date getResetSentAt() {
		return resetSentAt;
	}

	public void setResetSentAt(Date resetSentAt) {
		this.resetSentAt = resetSentAt;
	}

	public List<Micropost> getMicroposts() {
		return microposts;
	}

	public void setMicroposts(List<Micropost> microposts) {
		this.microposts = microposts;
	}

	public List<Relationship> getActiveRelationships() {
		return activeRelationships;
	}

	public void setActiveRelationships(List<Relationship> activeRelationships) {
		this.activeRelationships = activeRelationships;
	}

	public List<Relationship> getPassiveRelationships() {
		return passiveRelationships;
	}

	public void setPassiveRelationships(List<Relationship> passiveRelationships) {
		this.passiveRelationships = passiveRelationships;
	}

	public List<User> getFollowing() {
		return following;
	}

	public void setFollowing(List<User> following) {
		this.following = following;
	}

	public List<User> getFollowers() {
		return followers;
	}

	public void setFollowers(List<User> followers) {
		this.followers = followers;
	}

}

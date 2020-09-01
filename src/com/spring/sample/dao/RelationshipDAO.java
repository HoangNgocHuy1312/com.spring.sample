package com.spring.sample.dao;

import com.spring.sample.entity.Relationship;

public interface RelationshipDAO extends GenericDAO<Relationship, Integer> {

	public Relationship load(Relationship relationship);

	public boolean isFollowing(Relationship relationship);

}

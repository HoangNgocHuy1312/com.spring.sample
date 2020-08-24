package com.spring.sample.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.spring.sample.entity.Micropost;

public interface MicropostDAO extends GenericDAO<Micropost, Integer> {
	public Page<Micropost> paginate(Micropost micropost, Pageable pageable);
}

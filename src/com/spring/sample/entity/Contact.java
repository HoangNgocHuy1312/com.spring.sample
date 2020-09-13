package com.spring.sample.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Contact extends BaseEntity implements Serializable {

	private Integer id;
	private String title;
	private String message;

	public Contact() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}

package com.spring.sample.entity;

public enum Activation { 
	ACTIVATED(1), UNACTIVATED(0);
	
	public final int value;

	Activation(int value) {
		this.value = value;
	} 
}
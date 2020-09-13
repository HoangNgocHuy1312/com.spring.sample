package com.spring.sample.dao.imp;

import org.springframework.stereotype.Repository;

import com.spring.sample.dao.ContactDAO;
import com.spring.sample.entity.Contact;

@Repository
public class ContactDAOImp extends GenericDAOImp<Contact, Integer> implements ContactDAO {

	public ContactDAOImp() {
		super(Contact.class);
	}

}

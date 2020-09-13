package com.spring.sample.service.imp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.sample.dao.ContactDAO;
import com.spring.sample.entity.Contact;
import com.spring.sample.model.ContactModel;
import com.spring.sample.service.ContactService;

@Service
public class ContactServiceImp implements ContactService {

	private static final Logger logger = LoggerFactory.getLogger(ContactServiceImp.class);

	@Autowired
	ContactDAO contactDAO;

	private ContactServiceImp() {
	}

	public void setContactDAO(ContactDAO contactDAO) {
		this.contactDAO = contactDAO;
	}

	@Transactional
	public ContactModel addContact(ContactModel contactModel) throws Exception {
		logger.info("Adding the contact in the database");
		try {
			Contact condition = new Contact();
			BeanUtils.copyProperties(contactModel, condition);
			Contact contact = contactDAO.makePersistent(condition);
			contactModel = new ContactModel();
			BeanUtils.copyProperties(contact, contactModel);
			return contactModel;
		} catch (Exception e) {
			logger.error("An error occurred while adding the contact details to the database", e);
			throw e;
		}
	}

}

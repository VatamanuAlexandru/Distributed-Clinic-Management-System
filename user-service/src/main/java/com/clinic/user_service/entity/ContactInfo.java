package com.clinic.user_service.entity;

import com.clinic.common.PersistableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "_CONTACT_INFO")
public class ContactInfo extends PersistableEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "EMAIL_ADDRESS")
	private String emailAddress;

	@Column(name = "PHONE_NUMBER")
	private String phoneNumber;

}

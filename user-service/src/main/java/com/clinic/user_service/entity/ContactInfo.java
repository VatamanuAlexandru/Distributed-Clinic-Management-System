package com.clinic.user_service.entity;

import com.clinic.common.PersistableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "_CONTACT_INFO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactInfo extends PersistableEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "EMAIL_ADDRESS")
	private String emailAddress;

	@Column(name = "PHONE_NUMBER")
	private String phoneNumber;

}

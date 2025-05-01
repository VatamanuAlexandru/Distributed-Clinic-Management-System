package com.clinic.user_service.entity;

import java.time.LocalDateTime;

import com.clinic.common.PersistableEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "_PERSON")
@Getter
@Setter
public class Person extends PersistableEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "BIRTHDATE")
	private LocalDateTime birthdate;

	@Column(name = "PERSONAL_ID")
	private String personalId;

	@OneToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "CONTACT_INFO_ID")
	private ContactInfo contactInfo;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ADDRESS_INFO_ID")
	private AddressInfo addressInfo;

}

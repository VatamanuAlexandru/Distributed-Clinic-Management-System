package com.clinic.user_service.entity;

import com.clinic.common.PersistableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ADDRESS_INFO")
@Getter
@Setter
public class AddressInfo extends PersistableEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "STREET_NAME")
	private String streetName;

	@Column(name = "STREET_NUMBER")
	private String streetNumber;

	@Column(name = "BUILDING")
	private String building;

	@Column(name = "ENTRANCE")
	private String entrance;

	@Column(name = "FLAT_NUMBER")
	private String flatNumer;

	@Column(name = "POSTAL_CODE")
	private String postalCode;

	@Column(name = "COUNTRY")
	private String country;

	@Column(name = "COUNTY")
	private String county;

	@Column(name = "CITY")
	private String city;

}

package com.clinic.administrative.entity;

import com.clinic.common.NamedPersistableEntity;
import com.clinic.location.entity.City;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CLINIC")
@Getter
@Setter
@AllArgsConstructor
public class Clinic extends NamedPersistableEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "CITY_ID")
	private City city;

}

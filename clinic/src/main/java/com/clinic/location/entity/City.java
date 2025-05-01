package com.clinic.location.entity;

import com.clinic.common.NamedPersistableEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CITY")
@Getter
@Setter
@AllArgsConstructor
public class City extends NamedPersistableEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "COUNTY_ID")
	private County county;

}

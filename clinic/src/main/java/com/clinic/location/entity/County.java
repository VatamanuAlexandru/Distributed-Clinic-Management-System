package com.clinic.location.entity;

import java.util.List;

import com.clinic.common.NamedPersistableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "COUNTY")
@Getter
@Setter
@AllArgsConstructor
public class County extends NamedPersistableEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "CODE")
	private String code;

	@ManyToOne
	@JoinColumn(name = "COUNTRY_ID")
	private Country country;

	@OneToMany(mappedBy = "county")
	private List<City> cities;

}

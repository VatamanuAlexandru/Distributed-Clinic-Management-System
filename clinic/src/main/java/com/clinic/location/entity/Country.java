package com.clinic.location.entity;

import java.util.List;

import com.clinic.common.NamedPersistableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "COUNTRY")
@Getter
@Setter
@AllArgsConstructor
public class Country extends NamedPersistableEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "CODE")
	private String code;

	@OneToMany(mappedBy = "country")
	private List<County> counties;

}

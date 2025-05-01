package com.clinic.patient.entity;

import com.clinic.common.PersistableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "MEDICATION")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Medication extends PersistableEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TREATMENT_ID")
	private Treatment treatment;

	@Column(name = "NAME", nullable = false, unique = true)
	private String name;

	@Column(name = "DOSAGE", nullable = false)
	private String dosage;

	@Column(name = "FREQUENCY", nullable = false)
	private String frequency;

	@Column(name = "ADMINISTRATION_ROUTE", nullable = false)
	private String administrationRoute;

	@Column(name = "SIDE_EFFECTS", nullable = true)
	private String sideEffects;
}

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
import lombok.Setter;

@Entity
@Table(name = "INSURANCE")
@Getter
@Setter
@AllArgsConstructor
public class Insurance extends PersistableEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PATIENT_ID", nullable = false)
	private Patient patient;

	@Column(name = "INSURANCE_PROVIDER", nullable = false)
	private String provider;

	@Column(name = "PLAN_TYPE", nullable = false)
	private String planType;

}

package com.clinic.patient.entity;

import java.util.List;

import com.clinic.common.PersistableEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TREATMENT")
@Getter
@Setter
@AllArgsConstructor
public class Treatment extends PersistableEntity {
	private static final long serialVersionUID = 1L;

	@OneToOne
	@JoinColumn(name = "DIAGNOSIS_ID", nullable = false)
	private Diagnosis diagnosis;

	@Enumerated(EnumType.STRING)
	@Column(name = "TREATMENT_TYPE", nullable = false)
	private TreatmentType treatmentType;

	@Column(name = "DETAILS", nullable = false)
	private String details;

	@Column(name = "DURATION", nullable = true)
	private String duration;

	@OneToMany(mappedBy = "treatment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Medication> medications;

}

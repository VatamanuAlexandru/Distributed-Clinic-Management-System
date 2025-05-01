package com.clinic.patient.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.clinic.common.NamedPersistableEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "MEDICAL_HISTORY")
@Getter
@Setter
@AllArgsConstructor
public class MedicalHistory extends NamedPersistableEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PATIENT_ID")
	private Patient patient;

	@OneToMany(mappedBy = "medicalHistory", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Diagnosis> diagnosis;

	@OneToMany(mappedBy = "medicalHistory", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<MedicalTest> medicalTests;

	@Column(name = "DATE")
	private LocalDateTime date;

}

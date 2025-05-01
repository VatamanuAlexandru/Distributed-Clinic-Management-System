package com.clinic.patient.entity;

import java.util.List;

import com.clinic.common.PersistableEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PATIENT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Patient extends PersistableEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "PERSON_ID", unique = true)
	private Long personId;

	@OneToOne(mappedBy = "patient")
	private Insurance insurance;

	@OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<MedicalHistory> medicalHistories;

}

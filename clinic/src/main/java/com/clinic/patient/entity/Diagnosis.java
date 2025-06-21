package com.clinic.patient.entity;

import java.time.LocalDateTime;

import com.clinic.common.PersistableEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "DIAGNOSIS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Diagnosis extends PersistableEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEDICAL_HISTORY_ID", nullable = false)
	private MedicalHistory medicalHistory;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONDITION_ID", nullable = false)
	private MedicalCondition condition;

	@OneToOne(mappedBy = "diagnosis", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Treatment treatment;

	@Column(name = "DIAGNOSIS_DATE", nullable = false)
	private LocalDateTime diagnosisDate;

}

package com.clinic.patient.entity;

import com.clinic.common.PersistableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TEST_PARAMETER_RESULT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestParameterResult extends PersistableEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "PARAMETER_NAME")
	private String parameterName;

	@Column(name = "VALUE")
	private String value;

	@Column(name = "UNIT")
	private String unit;

	@Column(name = "REFERENCE_RANGE")
	private String referenceRange;

	@Column(name = "NOTES")
	private String notes;

	@ManyToOne
	@JoinColumn(name = "MEDICAL_TEST_ID")
	private MedicalTest medicalTest;
}

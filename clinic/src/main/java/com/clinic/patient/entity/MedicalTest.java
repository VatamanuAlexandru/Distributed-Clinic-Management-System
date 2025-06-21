package com.clinic.patient.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.clinic.common.PersistableEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "MEDICAL_TEST")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicalTest extends PersistableEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEDICAL_HISTORY_ID", nullable = false)
	private MedicalHistory medicalHistory;

	@Column(name = "TEST_NAME", nullable = false)
	private String testName;

	@Enumerated(EnumType.STRING)
	@Column(name = "CATEGORY", nullable = false)
	private TestCategory category;

	@Column(name = "RESULT", nullable = false)
	private String result;

	@OneToMany(mappedBy = "medicalTest", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TestParameterResult> parameterResults;

	@Column(name = "TEST_DATE", nullable = false)
	private LocalDateTime testDate;
}

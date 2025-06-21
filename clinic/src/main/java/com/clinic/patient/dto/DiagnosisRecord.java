package com.clinic.patient.dto;

import java.time.LocalDateTime;

import com.clinic.mapper.BaseRecord;
import com.clinic.mapper.ExcludeMapping;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DiagnosisRecord extends BaseRecord {
	private static final long serialVersionUID = 1L;

	@ExcludeMapping
	private MedicalHistoryRecord medicalHistory;
	@ExcludeMapping
	private MedicalConditionRecord condition;
	private TreatmentRecord treatment;
	private LocalDateTime diagnosisDate;
}

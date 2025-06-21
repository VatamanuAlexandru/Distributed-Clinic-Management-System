package com.clinic.patient.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.clinic.mapper.BaseRecord;
import com.clinic.mapper.ExcludeMapping;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MedicalHistoryRecord extends BaseRecord {
	private static final long serialVersionUID = 1L;

	private String name;
	@ExcludeMapping
	private PatientRecord patient;
	private LocalDateTime date;
	private List<MedicalTestRecord> medicalTests;
	private List<DiagnosisRecord> diagnosisRecords;

}

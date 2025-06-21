package com.clinic.patient.dto;

import java.util.List;

import com.clinic.common.dto.PersonRecord;
import com.clinic.mapper.BaseRecord;
import com.clinic.mapper.ExcludeMapping;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PatientRecord extends BaseRecord {
	private static final long serialVersionUID = 1L;

	@ExcludeMapping
	private PersonRecord person;

	private InsuranceRecord insuranceRecord;
	private List<MedicalHistoryRecord> medicalHistories;

}

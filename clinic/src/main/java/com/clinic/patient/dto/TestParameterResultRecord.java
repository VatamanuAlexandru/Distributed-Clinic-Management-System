package com.clinic.patient.dto;

import com.clinic.mapper.BaseRecord;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class TestParameterResultRecord extends BaseRecord {
	private static final long serialVersionUID = 1L;

	private String parameterName;
	private String value;
	private String unit;
	private String referenceRange;
	private String notes;
	private MedicalTestRecord medicalTest;
}

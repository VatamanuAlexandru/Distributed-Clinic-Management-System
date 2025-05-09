package com.clinic.patient.dto;

import com.clinic.mapper.BaseRecord;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class InsuranceRecord extends BaseRecord {
	private static final long serialVersionUID = 1L;

	private PatientRecord patient;
	private String provider;
	private String planType;
}

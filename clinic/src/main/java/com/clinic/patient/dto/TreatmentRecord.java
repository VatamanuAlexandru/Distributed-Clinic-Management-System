package com.clinic.patient.dto;

import java.util.List;

import com.clinic.mapper.BaseRecord;
import com.clinic.patient.entity.TreatmentType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class TreatmentRecord extends BaseRecord {
	private static final long serialVersionUID = 1L;

	private DiagnosisRecord diagnosis;
	private TreatmentType treatmentType;
	private String details;
	private String duration;
	private List<MedicationRecord> medications;

}

package com.clinic.doctor.dto;

import java.util.List;

import com.clinic.administrative.dto.OfficeRecord;
import com.clinic.common.dto.PersonRecord;
import com.clinic.mapper.BaseRecord;
import com.clinic.mapper.ExcludeMapping;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DoctorRecord extends BaseRecord {
	private static final long serialVersionUID = 1L;

	@ExcludeMapping
	private PersonRecord person;
	private MedicalTitleRecord medicalTitle;
	private MedicalRankRecord medicalRank;
	private Integer yearsOfExperience;
	private OfficeRecord office;
	private List<MedicalServiceRecord> services;

}

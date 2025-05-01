package com.clinic.doctor.dto;

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
	private String title;
	private String rank;
	private Integer yearsOfExperience;
	private OfficeRecord office;

}

package com.clinic.doctor.dto;

import com.clinic.administrative.dto.DepartmentRecord;
import com.clinic.mapper.BaseRecord;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MedicalServiceRecord extends BaseRecord {
	private static final long serialVersionUID = 1L;

	private String name;
	private String description;
	private Integer durationMinutes;
	private Double price;
	private DepartmentRecord department;

}

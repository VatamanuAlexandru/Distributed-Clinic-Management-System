package com.clinic.doctor.dto;

import com.clinic.mapper.BaseRecord;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MedicalRankRecord extends BaseRecord {
	private static final long serialVersionUID = 1L;

	private String name;
	private String description;

}

package com.clinic.administrative.dto;

import com.clinic.mapper.BaseRecord;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class OfficeRecord extends BaseRecord {
	private static final long serialVersionUID = 1L;

	private String name;
	private String roomNumber;

}

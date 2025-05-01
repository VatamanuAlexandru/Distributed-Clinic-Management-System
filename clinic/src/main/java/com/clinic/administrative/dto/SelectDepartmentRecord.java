package com.clinic.administrative.dto;

import com.clinic.mapper.SelectRecord;

public class SelectDepartmentRecord extends SelectRecord {
	private static final long serialVersionUID = 1L;

	public SelectDepartmentRecord(Long id, String name) {
		super(id, name);
	}

}

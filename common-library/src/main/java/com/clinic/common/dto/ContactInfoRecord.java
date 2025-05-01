package com.clinic.common.dto;

import com.clinic.mapper.BaseRecord;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ContactInfoRecord extends BaseRecord {
	private static final long serialVersionUID = 1L;

	private String emailAddress;

	private String phoneNumber;
}

package com.clinic.common.dto;

import java.time.LocalDateTime;

import com.clinic.mapper.BaseRecord;
import com.clinic.mapper.ExcludeMapping;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PersonRecord extends BaseRecord {
	private static final long serialVersionUID = 1L;

	private String lastName;
	private String firstName;
	private LocalDateTime birthdate;
	private String personalId;
	@ExcludeMapping
	private ContactInfoRecord contactInfo;
	@ExcludeMapping
	private AddressInfoRecord addressInfo;

}

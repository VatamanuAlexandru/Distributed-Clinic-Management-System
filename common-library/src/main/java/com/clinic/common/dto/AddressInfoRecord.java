package com.clinic.common.dto;

import com.clinic.mapper.BaseRecord;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AddressInfoRecord extends BaseRecord {
	private static final long serialVersionUID = 1L;

	private String streetName;
	private String streetNumber;
	private String building;
	private String entrance;
	private String flatNumer;
	private String postalCode;
	private String country;
	private String county;
	private String city;
}

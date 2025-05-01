package com.clinic.user_service.dto;

import com.clinic.mapper.BaseRecord;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminRegisterRequest extends BaseRecord {
	private static final long serialVersionUID = 1L;

	private String email;
	private String password;

}

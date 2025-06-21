package com.clinic.user_service.dto;

import com.clinic.common.dto.PersonRecord;
import com.clinic.mapper.BaseRecord;
import com.clinic.mapper.ExcludeMapping;

public class AuthRequest extends BaseRecord {
	private static final long serialVersionUID = 1L;

	private String email;
	@ExcludeMapping
	private String password;
	@ExcludeMapping
	private PersonRecord person;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public PersonRecord getPerson() {
		return person;
	}

	public void setPerson(PersonRecord person) {
		this.person = person;
	}

}

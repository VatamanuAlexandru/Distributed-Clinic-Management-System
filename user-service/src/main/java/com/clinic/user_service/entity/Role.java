package com.clinic.user_service.entity;

import org.springframework.security.core.GrantedAuthority;

import com.clinic.common.NamedPersistableEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "ROLE")
public class Role extends NamedPersistableEntity implements GrantedAuthority {
	private static final long serialVersionUID = 1L;

	public final static String ADMIN_IDENTIFIER = "ADMIN";
	public final static String DOCTOR_IDENTIFIER = "DOCTOR";
	public final static String PACIENT_IDENTIFIER = "PACIENT";

	@Override
	public String getAuthority() {
		return name;
	}
}

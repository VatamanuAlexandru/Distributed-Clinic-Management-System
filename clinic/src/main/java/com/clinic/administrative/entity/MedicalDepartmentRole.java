package com.clinic.administrative.entity;

import com.clinic.common.NamedPersistableEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "MEDICAL_DEPARTMENT_ROLE")
public class MedicalDepartmentRole extends NamedPersistableEntity {
	private static final long serialVersionUID = 1L;

}

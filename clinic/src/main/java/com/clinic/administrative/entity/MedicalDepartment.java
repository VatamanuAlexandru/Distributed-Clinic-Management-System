package com.clinic.administrative.entity;

import com.clinic.common.PersistableEntity;
import com.clinic.doctor.entity.Doctor;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "MEDICAL_DEPARTMENT")
@Getter
@Setter
@AllArgsConstructor
public class MedicalDepartment extends PersistableEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "DOCTOR_ID")
	private Doctor doctor;

	@ManyToOne
	@JoinColumn(name = "DEPARTMENT_ID")
	private Department department;

	@ManyToOne
	@JoinColumn(name = "ROLE_ID")
	private MedicalDepartmentRole role;

}

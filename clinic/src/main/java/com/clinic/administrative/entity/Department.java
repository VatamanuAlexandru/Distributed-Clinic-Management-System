package com.clinic.administrative.entity;

import java.util.List;

import com.clinic.common.NamedPersistableEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "DEPARTMENT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Department extends NamedPersistableEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "CLINIC_ID")
	private Clinic clinic;

	@OneToMany(mappedBy = "department")
	private List<MedicalDepartment> medicalDepartments;

	@OneToMany(mappedBy = "department")
	private List<Office> offices;

}

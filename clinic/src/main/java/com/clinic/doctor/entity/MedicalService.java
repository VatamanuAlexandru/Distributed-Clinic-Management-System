package com.clinic.doctor.entity;

import com.clinic.administrative.entity.Department;
import com.clinic.common.NamedPersistableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "MEDICAL_SERVICE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicalService extends NamedPersistableEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "DURATION_MINUTES")
	private Integer durationMinutes = 30;

	@Column(name = "PRICE")
	private Double price;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEPARTMENT_ID")
	private Department department;

}

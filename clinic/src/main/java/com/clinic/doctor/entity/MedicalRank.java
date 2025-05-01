package com.clinic.doctor.entity;

import com.clinic.common.NamedPersistableEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "MEDICAL_RANK")
public class MedicalRank extends NamedPersistableEntity {
	private static final long serialVersionUID = 1L;

}

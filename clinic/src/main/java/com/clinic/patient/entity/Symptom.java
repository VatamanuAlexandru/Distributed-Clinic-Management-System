package com.clinic.patient.entity;

import com.clinic.common.NamedPersistableEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "SYMPTOM")
public class Symptom extends NamedPersistableEntity {
	private static final long serialVersionUID = 1L;

}

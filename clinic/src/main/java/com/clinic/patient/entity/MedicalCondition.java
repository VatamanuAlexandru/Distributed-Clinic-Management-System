package com.clinic.patient.entity;

import java.util.List;

import com.clinic.common.NamedPersistableEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "MEDICAL_CONDITION")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicalCondition extends NamedPersistableEntity {
	private static final long serialVersionUID = 1L;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "CONDTION_SYMPTOM", joinColumns = @JoinColumn(name = "CONDITION_ID"), inverseJoinColumns = @JoinColumn(name = "SYMPTOM_ID"))
	private List<Symptom> symptoms;
}

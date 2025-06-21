package com.clinic.patient.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.patient.entity.MedicalCondition;

public interface MedicalConditionRepository extends JpaRepository<MedicalCondition, Long> {

	Optional<MedicalCondition> findByName(String name);

}

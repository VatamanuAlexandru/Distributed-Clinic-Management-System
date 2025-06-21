package com.clinic.patient.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.patient.entity.Medication;

public interface MedicationRepository extends JpaRepository<Medication, Long> {

}

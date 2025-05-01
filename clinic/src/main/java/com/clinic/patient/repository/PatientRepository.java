package com.clinic.patient.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.patient.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {

}

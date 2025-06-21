package com.clinic.patient.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.patient.entity.Diagnosis;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {

}

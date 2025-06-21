package com.clinic.patient.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.patient.entity.Treatment;

public interface TreatmentRepository extends JpaRepository<Treatment, Long> {

}

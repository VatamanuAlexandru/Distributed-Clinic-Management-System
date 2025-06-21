package com.clinic.patient.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.patient.entity.MedicalHistory;

public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Long> {

	List<MedicalHistory> findByPatientId(Long patientId);
}

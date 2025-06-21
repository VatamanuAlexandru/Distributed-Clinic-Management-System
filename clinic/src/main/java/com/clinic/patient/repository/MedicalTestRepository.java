package com.clinic.patient.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.patient.entity.MedicalTest;

public interface MedicalTestRepository extends JpaRepository<MedicalTest, Long> {

    List<MedicalTest> findByMedicalHistory_Patient_Id(Long patientId);

}

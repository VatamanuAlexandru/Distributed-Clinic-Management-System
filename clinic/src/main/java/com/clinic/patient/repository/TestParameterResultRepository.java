package com.clinic.patient.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinic.patient.entity.TestParameterResult;

@Repository
public interface TestParameterResultRepository extends JpaRepository<TestParameterResult, Long> {

	List<TestParameterResult> findByMedicalTestId(Long testId);
}
package com.clinic.doctor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.doctor.entity.MedicalService;

public interface MedicalServiceRepository extends JpaRepository<MedicalService, Long> {

	
	List<MedicalService> findByDepartmentId(Long departmentId);
}

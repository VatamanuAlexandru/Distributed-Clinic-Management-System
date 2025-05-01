package com.clinic.administrative.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.administrative.entity.MedicalDepartment;

public interface MedicalDepartmentRepository extends JpaRepository<MedicalDepartment, Long> {

}
